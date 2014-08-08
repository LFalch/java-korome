package com.lfalch.korome;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex2d;
import java.io.IOException;


public class HUDDraw {
	public static void number(double x, double y, double number) throws IOException{
		if(Double.isNaN(number)){
			eDraw(x, y);
			return;
		}else if(Double.isInfinite(number)){
			eDraw(x, y);
			eDraw(x+movement, y);
			return;
		}
		//Just making sure the number isn't something weird
		/* TODO
		 * Add better symbols for infinite and NaN
		 */
			
		String numberString = "" + number;
		int add = 0;
		for(char c: numberString.toCharArray()){
			switch(c){
			case '.':
				box(x+2+add, y+12, 2, 2);
				break;
			case '-':
				box(x+1+add, y+8, 5, 2);
				break;
			case 'e':
			case 'E':
				eDraw(x+add, y);
				break;
			default:
				singleDraw(x+add, y, Integer.valueOf("" + c));
				break;
			}
			add += movement;
		}
		Colour.WHITE.glSet();
	}
	
	public static void statusBar(double x, double y, double width, double height, float status, Colour background, Colour bar){
		background.glSet();
		box(x, y, width, height);
		bar.glSet();
		box(x, y, width*status, height);
	}
	
	public static void drawString(double x, double y, String s) throws IOException{
		Chars.drawString(s, x, y);
	}
	
	public static void box(double x, double y, double w, double h){
		glBegin(GL_QUADS);
		glVertex2d(x, y);
		glVertex2d(x+w, y);
		glVertex2d(x+w, y+h);
		glVertex2d(x, y+h);
		glEnd();
		Colour.WHITE.glSet();
	}
	
	private static int numberWidth = 6, numberHeight = 14, movement = 7;
	private static void singleDraw(double x, double y, int number) throws IOException{
		Texture numbers = Draw.getTexture("numbers");
		
		numbers.bind();
		glBegin(GL_QUADS);
		glTexCoord2d(getTexCoord(number), 0);
		glVertex2d(x, y);
		glTexCoord2d(getTexCoord(number) + 0.1f, 0);
		glVertex2d(x+numberWidth, y);
		glTexCoord2d(getTexCoord(number) + 0.1f, 1);
		glVertex2d(x+numberWidth, y+numberHeight);
		glTexCoord2d(getTexCoord(number), 1);
		glVertex2d(x, y+numberHeight);
		glEnd();
		Texture.unbind();
	}
	
	private static void eDraw(double x, double y) throws IOException{
		Texture e = Draw.getTexture("e");
		
		e.bind();
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2d(x, y);
		glTexCoord2d(1, 0);
		glVertex2d(x+numberWidth, y);
		glTexCoord2d(1, 1);
		glVertex2d(x+numberWidth, y+numberHeight);
		glTexCoord2d(0, 1);
		glVertex2d(x, y+numberHeight);
		glEnd();
		Texture.unbind();
	}
	
	private static float getTexCoord(int part){
		return 0.1f * part;
	}
}
