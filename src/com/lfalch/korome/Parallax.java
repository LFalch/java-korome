package com.lfalch.korome;

import java.io.IOException;

public class Parallax {
	double scalar;
	String tex;
	int width, height;
	
	public Parallax(String parallaxTexture, double scalar) throws IOException{
		Texture t = Draw.getTexture(parallaxTexture);
		this.scalar = scalar;
		tex = parallaxTexture;
		width = t.getWidth();
		height = t.getHeight();
	}
	
	public void draw(double x, double y) throws IOException{
		x *= -scalar;
		y *= -scalar;
		
		Draw.drawSprite(tex, x, y, width, height);
	}
}
