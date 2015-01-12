package com.lfalch.korome.packets;


public class Disconnect implements IPacket{
	private static final long serialVersionUID = 8442795958218218335L;

	public enum Reason{
		LEFt, INVALID_VERSION, SYNTAX_ERROR, KICKED, SERVER_CLOSE;
	}
	
	public Disconnect(Reason disconnectReason){
		this.reason = disconnectReason;
	}
	
	Reason reason;
}
