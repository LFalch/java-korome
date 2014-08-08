package com.lfalch.korome;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class Colour {
	public float red,green,blue;
	
	public static Colour RED, GREEN, BLUE, YELLOW, BLACK, WHITE, LIGHT_GREY, GREY, DARK_GREY, PINK, ORANGE, MAGENTA, CYAN;
	private static Colour set = null;
	
	public Colour(float red, float green, float blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public void glSet(){
		glSet(1f);
	}
	
	public void glSet(float alpha){
		set = this;
		GL11.glColor4f(red, green, blue, alpha);
	}
	
	public static Colour getPrevSet(){
		return set;
	}
	
	static{
		RED = 	new Colour(1f, 0f, 0f);
		GREEN = new Colour(0f, 1f, 0f);
		BLUE = 	new Colour(0f, 0f, 1f);
		
		YELLOW = 	new Colour(1f, 1f, 0f);
		LIGHT_GREY= new Colour(.75f, .75f, .75f);
		GREY = 		new Colour(.5f, .5f, .5f);
		DARK_GREY = new Colour(.25f, .25f, .25f);
		PINK = 		new Colour(1f, .6863f, .6863f);
		ORANGE = 	new Colour(1f, .78f, 0f);
		MAGENTA = 	new Colour(1f, 0f, 1f);
		CYAN = 		new Colour(0f, 1f, 1f);
		
		BLACK = new Colour(0f, 0f, 0f);
		WHITE = new Colour(1f, 1f, 1f);
	}
	
	public static Color toColor(Colour c){
		return new Color(c.red, c.green, c.blue);
	}
	
	public static Colour toColour(Color c){
		return new Colour(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
	}
}
