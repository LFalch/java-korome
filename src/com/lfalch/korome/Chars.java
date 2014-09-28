package com.lfalch.korome;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex2d;
import java.io.IOException;

//Don't touch it, it just... works :D
public class Chars {
	private static final int width = 16, height = 16;
	
	public static void drawString(String s, double x, double y) throws IOException{
		int add = 0;
		for(char c: s.toCharArray()){
			drawID(x+add, y, getID(c));
			add += 15;
		}
	}
	
	private static int getID(char c){
		return c;
	}
	
	private static void drawID(double x, double y, int id) throws IOException{
		int iTexX = id % 16;
		int iTexY = (id - iTexX) / 16;
		
		float one16th = 1.0f/16.0f;
		
		float texX = iTexX * one16th;
		float texY = iTexY * one16th;
		
		Texture chars = Draw.getTexture("chrs");
		
		chars.bind();
		glBegin(GL_QUADS);
		glTexCoord2d(texX, texY);
		glVertex2d(x, y);
		glTexCoord2d(texX + one16th, texY);
		glVertex2d(x+width, y);
		glTexCoord2d(texX + one16th, texY+one16th);
		glVertex2d(x+width, y+height);
		glTexCoord2d(texX, texY+one16th);
		glVertex2d(x, y+height);
		glEnd();
		Texture.unbind();
		Colour.WHITE.glSet();
	}
}
