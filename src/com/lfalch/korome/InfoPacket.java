package com.lfalch.korome;

public class InfoPacket {
	private int mx, my, keyevents[];
	private double delta;
	
	public InfoPacket(int mousex, int mousey, double delta, int[] keys) {
		super();
		mx = mousex;
		my = mousey;
		keyevents = keys;
		this.delta = delta;
	}

	public int getMouseX() {
		return mx;
	}

	public int getMouseY() {
		return my;
	}

	public double getDelta() {
		return delta;
	}
	
	public int[] getKeyEvents() {
		return keyevents;
	}
	
	public int getKeyEvent(int index) {
		if(keyevents.length <= index)
			return 0;
		return keyevents[index];
	}
}
