package com.lfalch.korome.packets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Packet {
	public final static String version = "0.1";
	private String content;
	private PacketMethod method;
	private PacketAttribute[] attributes;
	
	public Packet(PacketMethod method, PacketAttribute... attributes){
		StringBuilder con = new StringBuilder("FGMP/" + version + " ");
		con.append(method + "\n");
		for(PacketAttribute attribute : attributes){
			con.append(attribute + "\n");
		}
		con.append("END");
		
		content = con.toString();
		this.method = method;
		this.attributes = attributes;
	}
	
	
	private DatagramPacket received;
	public Packet(DatagramPacket dataPacket) throws InvalidPacketException{
		content = new String(dataPacket.getData());
		
		received = dataPacket;
		
		String[] ss = content.split("\n");
		
		method = PacketMethod.valueOf(ss[0].substring(6+version.length()));
		attributes = new PacketAttribute[ss.length - 2];
		
		for(int i = 0; i < attributes.length; i++){
			attributes[i] = method.generateAttribute(ss[i+1]);
		}
	}
	
	public String getContent(){
		return content;
	}
	
	public PacketMethod getMethod() {
		return method;
	}
	
	public static Packet receive(DatagramSocket socket) throws IOException, InvalidPacketException{
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		
		buf = trim(buf);
		packet.setData(buf);
		packet.setLength(buf.length);
		
		return new Packet(packet);
	}
	
	public static byte[] trim(byte[] bytes)
	{
	    int i = bytes.length - 1;
	    while (i >= 0 && bytes[i] == 0)
	    {
	        --i;
	    }

	    return Arrays.copyOf(bytes, i + 1);
	}

	public InetAddress getAddress() {
		if(received != null)
			return received.getAddress();
		throw new NullPointerException("This Packet isn't created from a DatagramPacket");
	}

	public int getPort() {
		if(received != null)
			return received.getPort();
		throw new NullPointerException("This Packet isn't created from a DatagramPacket");
	}

	public void send(DatagramSocket socket, InetAddress address, int port) throws IOException {
		byte[] data = content.getBytes();
		
		DatagramPacket p = new DatagramPacket(data, data.length, address, port);
		
		socket.send(p);
	}
}
