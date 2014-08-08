package com.lfalch.korome;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.io.IOException;
import java.util.HashMap;

public class Draw {
	private static HashMap<String, Texture> cache;
	private static int w, h;
	
	public static void initialise(int width, int height){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		w = width;
		h = height;
		
		cache = new HashMap<String, Texture>();
	}
	
	public static Texture getTexture(String fileName) throws IOException{
		Texture texture;
		if(cache.containsKey(fileName))
			texture = cache.get(fileName);
		else
			texture = newTexture(fileName);
		return texture;
	}
	
	private static Texture newTexture(String fileName) throws IOException{
		Texture newTex = null;
		newTex = new Texture("resources/" + fileName + ".png");
		cache.put(fileName, newTex);
		
		return newTex;
	}
	
	public static void clearCache(){
		for(Texture texture: cache.values())
			texture.delete();
		cache = new HashMap<String, Texture>();
	}
	
	public static void beforeDraw(Vector focusObject){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glPushMatrix();
		glTranslated(-focusObject.x + w/2, -focusObject.y + h/2, 0d);
	}
	
	public static void drawSprite(String texture, double theta, double x, double y, double width, double height) throws IOException{
		Texture tex = getTexture(texture);
		
		tex.draw(theta, x, y, width, height);
	}
	
	public static void drawCircle(double radius, double x, double y){
		glBegin(GL_POLYGON);
		for (double rad = 0; rad < Math.PI * 2; rad += 1d/radius)
			glVertex2d(x + Math.cos(rad) * radius, y + Math.sin(rad) * radius);
		glEnd();
		Colour.WHITE.glSet();
	}
	
	public static void drawSprite(String texture, double x, double y, double width, double height) throws IOException{
		drawSprite(texture, 0d, x, y, width, height);
	}
}
