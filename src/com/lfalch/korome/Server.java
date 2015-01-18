package com.lfalch.korome;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import com.lfalch.korome.packets.InvalidPacketException;
import com.lfalch.korome.packets.Packet;
import com.lfalch.korome.packets.PacketMethod;

public class Server extends Thread{
	
	private volatile boolean running;
	
	public Server() throws SocketException {
		super();
		
		serverSocket = new DatagramSocket(45565);
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.start();
		}
		catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	DatagramSocket serverSocket;
	@Override
	public void run() {
		running = true;
		
		while(running){
			try {
				Packet pa = Packet.receive(serverSocket);
				
				InetAddress address = pa.getAddress();
				int port = pa.getPort();
				System.out.println(address.getHostAddress() + ":" + port + " " + pa.getContent());
				
				pa = new Packet(PacketMethod.MSG, PacketMethod.MSG.generateAttribute("Body", "I got your " + pa.getMethod() + "-message"));
				
				pa.send(serverSocket, address, port);
			}
			catch (IOException e) {
				e.printStackTrace();
			}catch (InvalidPacketException e) {
				e.printStackTrace();
			}
		}
		serverSocket.close();
		System.out.println("Stopped running");
	}
}
