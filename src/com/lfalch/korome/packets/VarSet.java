package com.lfalch.korome.packets;

import java.util.HashMap;

public class VarSet implements IPacket {
	private static final long serialVersionUID = 6860838460774978435L;
	public HashMap<String, Object> variables;
	public VarSet(){
		variables = new HashMap<String, Object>(2);
	}
	
	public void add(String identifier, Object variable){
		variables.put(identifier, variable);
	}
}
