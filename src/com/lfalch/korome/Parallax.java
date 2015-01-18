package com.lfalch.korome;

import java.io.IOException;

@Deprecated
public class Parallax {
	double scalar;
	String tex;
	int width, height;
	double oX, oY;
	
	@Deprecated
	public Parallax(String parallaxTexture, double scalar, double originalX, double originalY) throws IOException{
		Texture t = Draw.getTexture(parallaxTexture);
		this.scalar = scalar;
		tex = parallaxTexture;
		width = t.getWidth();
		height = t.getHeight();
		oX = originalX;
		oY = originalY;
	}
	
	@Deprecated
	public void draw(double x, double y) throws IOException{
		double tX = x;
		double tY = y;
		
		tX *= -scalar;
		tY *= -scalar;
		
		Draw.drawSprite(tex, oX+x, oY+y, width, height);
	}
}
