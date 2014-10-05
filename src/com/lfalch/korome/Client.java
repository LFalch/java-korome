package com.lfalch.korome;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Client {
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private HashMap<String, Object> variables;
	private Handler logger;
	
	public boolean crash = false;
	public Exception crashCause;
	public Thread listener;

	public Client(ServerSocket serversocket) throws IOException {
		connection = serversocket.accept();
		
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		
		variables = new HashMap<String, Object>();
		
		listener = new Thread(new Listener(), getIP() + "_listener");
		listener.start();
	}
	
	public void send(int type, Object packet) throws IOException{
		output.writeInt(type);
		output.writeObject(packet);
		output.flush();
	}
	
	public String getIP(){
		return connection.getInetAddress().getHostAddress();
	}
	public static final int PROTOCOL_VERSION = 1000, PING = 0, HANDSHAKE = 1, MSG = 2, VAR_MOD = 5, CONFIRM = 10;
	
	public boolean running = true;
	
	private class Listener implements Runnable{
		@Override
		public void run() {
			while (running && !crash)
				try {
					int type = input.readInt();
					Object packet = input.readObject();
					
					if(logger != null)
						logger.handle("[" + type + "]: " + packet.toString());
					
					switch (type) {
					case PING:
						send(PING, packet);
						break;
					case HANDSHAKE:
						if (packet instanceof Integer)
							if ((Integer) packet == PROTOCOL_VERSION) {
								send(HANDSHAKE, PROTOCOL_VERSION);
								break;
							}
						send(HANDSHAKE, "REJECT");
						disconnect();
						break;
					case MSG:
						break;
					case VAR_MOD:
						if(!(packet instanceof Object[]))
							disconnect();
						Object[] oArray = (Object[]) packet;
						if(oArray.length < 2 || !(oArray[0] instanceof String))
							disconnect();
						
						variables.put((String) oArray[0], oArray[1]);
						send(CONFIRM, VAR_MOD);
						break;
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (ClassNotFoundException e) {
					crash = true;
					crashCause = e;
				}
				try {
					disconnect();
				}
				catch (IOException e) {
					crash = true;
					crashCause = e;
				}
		}

		public void disconnect() throws IOException {
			running = false;
			connection.close();
			output.close();
			input.close();
		}
	}

	public void addLogger(Handler handler) {
		logger = handler;
	}
}
