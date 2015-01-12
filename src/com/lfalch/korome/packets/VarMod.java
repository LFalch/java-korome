package com.lfalch.korome.packets;


public class VarMod implements IPacket {
	private static final long serialVersionUID = 2125556373509051959L;
	public String id;
	public Object var;
	
	public VarMod(String identifier, Object variable){
		id = identifier;
		var = variable;
	}
}
