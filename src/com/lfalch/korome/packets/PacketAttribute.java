package com.lfalch.korome.packets;

public abstract class PacketAttribute{
	public abstract String getName();
	public abstract String getValue();
	
	@Override
	public final String toString() {
		return getName() + ":" + getValue();
	}
}