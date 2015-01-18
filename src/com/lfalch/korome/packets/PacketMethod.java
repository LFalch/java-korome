package com.lfalch.korome.packets;

public enum PacketMethod{
	LOGIN("Id", "Pass"), HANDSHAKE("Sess"), DISCONNECT("Sess", "Reason"), VAR("Sess", "Name", "Get", "Set"), MSG("Sess", "Body"), MOVE("Sess", "Coord", "Obj");
	
	private PacketMethod(String... attributes){
		allowedAttributes = attributes;
	}
	
	private String[] allowedAttributes;
	
	public PacketAttribute generateAttribute(String line) throws InvalidPacketException{
		String name = line.split(":")[0].trim();
		String value = line.split(":")[1].trim();
		
		for(String allowedAttribute: allowedAttributes)
			if(allowedAttribute.equals(name))
				return PacketAttributeFactory.generatePacketAttribute(name, value);
		throw new InvalidPacketException("Attribute '" + name + "' does not exist for method '" + this + "'");
	}
	
	public PacketAttribute generateAttribute(String name, String value) throws InvalidPacketException{
		for(String allowedAttribute: allowedAttributes)
			if(allowedAttribute.equals(name))
				return PacketAttributeFactory.generatePacketAttribute(name, value);
		throw new InvalidPacketException("Attribute '" + name + "' does not exist for method '" + this + "'");
	}
	
	public String toString() {
		return super.toString();
	}
	
	private static final class PacketAttributeFactory extends PacketAttribute{
		public static PacketAttribute generatePacketAttribute(String name, String value){
			return (PacketAttribute) new PacketAttributeFactory(name, value);
		}
		
		String n,m;
		private PacketAttributeFactory(String name, String value){
			n = name;
			m = value;
		}
		@Override
		public String getName() {
			return n;
		}
		@Override
		public String getValue() {
			return m;
		}
	}
}