package com.lfalch.korome;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.lfalch.korome.packets.Disconnect;
import com.lfalch.korome.packets.Handshake;
import com.lfalch.korome.packets.IPacket;
import com.lfalch.korome.packets.Packets;
import com.lfalch.korome.packets.Success;
import com.lfalch.korome.packets.VarMod;


public class Client {
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Handler<String> logger;
	private Handler<DoubleObject<String, Object>> varModder;
	private Handler<Exception> crashHandler;
	
	public Thread listener;
	
	private class Break extends Throwable{private static final long serialVersionUID = 6175309303027039644L;}

	public Client(ServerSocket serversocket, Handler<DoubleObject<String, Object>> variableModder, Handler<String> logger, Handler<Exception> crasher) throws IOException {
		connection = serversocket.accept();
		
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		
		this.varModder = variableModder;
		this.logger = logger;
		
		try {
			Object handshake = input.readObject();
			if(handshake instanceof Handshake)
				if(((Handshake) handshake).version == Handshake.PROTOCOL_VERSION){
					send(new Handshake(Handshake.PROTOCOL_VERSION));
					throw new Break();
				}
			send(new Disconnect(Disconnect.Reason.INVALID_VERSION));
			log("Client had an invalid version, disconnecting them.");
			running = false;//The listener thread should then just clean up the streams.
		}catch (Break e){}
		catch (ClassNotFoundException e) {
			send(new Disconnect(Disconnect.Reason.SYNTAX_ERROR));
			log("Class not found, disconnecting client.");
			running = false;//The listener thread should then just clean up the streams.
		}
		
		listener = new Thread(new Listener(), getIP() + "_listener");
		listener.start();
	}
	
	public void send(IPacket packet) throws IOException{
		output.writeObject(packet);
		output.flush();
	}
	
	public String getIP(){
		return connection.getInetAddress().getHostAddress();
	}
	public boolean running = true;
	
	private class Listener implements Runnable{
		@Override
		public void run() {
			while (running)
				try {
					Object rawPacket = input.readObject();
					
					if(!(rawPacket instanceof IPacket)){
						send(new Disconnect(Disconnect.Reason.SYNTAX_ERROR));
						disconnect();
					}else{
						IPacket packet = (IPacket) rawPacket;
						
						switch(Packets.valueOf(packet.getClass().getSimpleName().toUpperCase())){
						case VARMOD:
							VarMod variable2Mod = (VarMod) packet;
							modifyVariable(variable2Mod.id, variable2Mod.var);
							
							send(new Success());
							break;
						case SUCCESS:
							break; //Ignored -- just means something is good
						default:
							log("Syntax error, disconnecting client.");
							send(new Disconnect(Disconnect.Reason.SYNTAX_ERROR));
						case DISCONNECT:
							disconnect();
							log("Disconnected.");
							break;
						}
					}
				}
				catch (IOException e) {
					crash(e);
				}
				catch (ClassNotFoundException e) {
					log("Syntax error, disconnecting client.");
					try {
						send(new Disconnect(Disconnect.Reason.SYNTAX_ERROR));
						disconnect();
					}
					catch (IOException e2) {
						crash(e2);
					}
				}
				try {
					disconnect();
				}
				catch (IOException e) {
					crash(e);
				}
		}
	}
	
	private void modifyVariable(String identifier, Object variable){
		varModder.handle(new DoubleObject<String, Object>(identifier, variable));
	}
	
	private void log(String message){
		logger.handle(message);
	}
	
	public void crash(Exception e){
		try {
			disconnect();
		}catch (IOException e1) {log("Disconnect failed under crash.");}
		crashHandler.handle(e);
	}

	public void disconnect() throws IOException {
		running = false;
		connection.close();
		output.close();
		input.close();
	}
}
