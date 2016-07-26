package com.lfalch.korome;


import static com.lfalch.korome.Colour.WHITE;
import static org.lwjgl.opengl.GL11.*;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

/**
 * Texture
 *
 * @author Lucas Falch <lucas@lfalch.com>
 * @since October 13, 2012
 * @last_modified January 07, 2014
 */

public class Texture implements Closeable{

	private final int id, width, height;

	public Texture(String file) throws IOException{
		this.id = glGenTextures();

		InputStream in = new FileInputStream(file);
		PNGDecoder decoder = new PNGDecoder(in);
		width = decoder.getWidth();
		height = decoder.getHeight();
		ByteBuffer buffer = BufferUtils.createByteBuffer(4 * width * height);
		decoder.decode(buffer, width * 4, Format.RGBA);
		buffer.flip();
		in.close();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width,
			height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glBindTexture(GL_TEXTURE_2D, 0);

		System.out.println("Generated #" + id + " ./" + file);
	}

	//Drawing af en roteret rektangel.
	public void draw(double theta, double x, double y, double width, double height){
		//Finder den halve diameter
		double halfdiameter = Math.hypot(width/2, height/2);

		//Finder vinklerne der skal drejes for at kunne nå hen hvert punkt
		double low = Math.atan2(height/2, width/2);
		double high = Math.atan2(height/2, -(width/2));

		//Finder hvert punkt i rektanglen, hvor ul er øverste-venstre, ur er øverste-højre, ll er nederste-venstre og lr er nederste-højre.
		Vector ul = new Vector(x + Math.cos(theta-high) * halfdiameter, y + Math.sin(theta-high) * halfdiameter);
		Vector ur = new Vector(x + Math.cos(theta-low ) * halfdiameter, y + Math.sin(theta-low ) * halfdiameter);
		Vector ll = new Vector(x + Math.cos(theta+high) * halfdiameter, y + Math.sin(theta+high) * halfdiameter);
		Vector lr = new Vector(x + Math.cos(theta+low ) * halfdiameter, y + Math.sin(theta+low ) * halfdiameter);

		//Tegner den roterede rektangel
		bind();
		glBegin(GL_QUADS);{
			glTexCoord2d(0, 0);
			glVertex2d(ul.x, ul.y);
			glTexCoord2d(1, 0);
			glVertex2d(ur.x, ur.y);
			glTexCoord2d(1, 1);
			glVertex2d(lr.x, lr.y);
			glTexCoord2d(0, 1);
			glVertex2d(ll.x, ll.y);
		}glEnd();
		WHITE.glSet();
		unbind();
	}

	public static ByteBuffer getByteBuffer(String file) throws IOException{
		InputStream in = null;
		in = new FileInputStream(file);
		PNGDecoder decoder = new PNGDecoder(in);
		int width = decoder.getWidth();
		int height = decoder.getHeight();
		ByteBuffer buffer = BufferUtils.createByteBuffer(4 * width * height);
		decoder.decode(buffer, width * 4, Format.RGBA);
		buffer.flip();
		in.close();
		return buffer;
	}

	public static IntBuffer getIntBuffer(String file) throws IOException{
		return getByteBuffer(file).asIntBuffer();
	}

	@Override
	public void close() {
		glDeleteTextures(id);
	}

	public void bind() {
		if (id < 1) {
			System.err.println("Texture not loaded correctly!");
		} else
			glBindTexture(GL_TEXTURE_2D, id);
	}

	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}
}
