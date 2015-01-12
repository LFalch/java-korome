package com.lfalch.korome;

import java.io.Serializable;

public final class DoubleObject<O1, O2> implements Serializable{
	private static final long serialVersionUID = -2986090914544188948L;
	O1 o1;
	O2 o2;
	
	public DoubleObject(O1 object1, O2 object2){
		o1 = object1;
		o2 = object2;
	}
}
