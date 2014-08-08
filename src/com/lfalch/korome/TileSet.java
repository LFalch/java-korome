package com.lfalch.korome;

import static com.lfalch.korome.Colour.WHITE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex2d;
import java.io.IOException;


public class TileSet{
	private Texture tex;
	private double w, h;
	
	public TileSet(String texture, int tilesX, int tilesY) throws IOException{
		tex = Draw.getTexture(texture);
		w = tilesX;
		h = tilesY;
	}
	
	public void drawTile(double x, double y, int tilePosX, int tilePosY, double width, double height){
		tex.bind();
		glBegin(GL_QUADS);{
			glTexCoord2d(tilePosX/w, tilePosY/h);
			glVertex2d(x, y);
			glTexCoord2d((tilePosX+1d)/w, tilePosY/h);
			glVertex2d(x+width, y);
			glTexCoord2d((tilePosX+1d)/w, (tilePosY+1)/h);
			glVertex2d(x+width, y+height);
			glTexCoord2d(tilePosX/w, (tilePosY+1)/h);
			glVertex2d(x, y+height);
		}glEnd();
		WHITE.glSet();
		Texture.unbind();
	}
	
	public static TileSet power, data;
	static{
		try {
			power = new TileSet("power", 3, 3);
			data = new TileSet("data", 3, 3);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
