package com.lfalch.korome.packets;


public class Handshake implements IPacket {
	private static final long serialVersionUID = 1684938824132421480L;
	public int version;
	public static int PROTOCOL_VERSION = 100;
	
	public Handshake(int version){
		this.version = version;
	}
}
