package com.lfalch.korome.packets;

public class InvalidPacketException extends Exception {
	private static final long serialVersionUID = -8936480557443546058L;

	public InvalidPacketException() {
		super("Packet syntax was not satisfied");
	}

	public InvalidPacketException(String message) {
		super(message);
	}
}
