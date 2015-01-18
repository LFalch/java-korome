package com.lfalch.korome;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import com.lfalch.korome.packets.InvalidPacketException;
import com.lfalch.korome.packets.Packet;
import com.lfalch.korome.packets.PacketMethod;

@Deprecated
public class Client extends Thread{
	public Client() throws UnknownHostException, SocketException {
		super();
		
		address = InetAddress.getByName("localhost");
		socket = new DatagramSocket();
	}
	
	InetAddress address;
	int port = 45565;
	DatagramSocket socket;
	@Override
	public void run() {
		try {
			Packet p = new Packet(PacketMethod.LOGIN, PacketMethod.LOGIN.generateAttribute("Id", "Words! Yes!"));
			p.send(socket, address, port);
			
			p = Packet.receive(socket);
			
			System.out.println(p.getAddress().getHostAddress() + ":" + p.getPort() + " " + p.getContent());
			
			p = new Packet(PacketMethod.DISCONNECT, PacketMethod.DISCONNECT.generateAttribute("Reason", "User disconnect"));
			
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}catch (InvalidPacketException e) {
			e.printStackTrace();
		}
		socket.close();
	}
}
