package com.lfalch.korome;

import static org.lwjgl.input.Keyboard.getEventKey;
import static org.lwjgl.input.Keyboard.getEventKeyState;
import static org.lwjgl.input.Keyboard.next;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glPopMatrix;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Util;

public class Game {
	protected static Settings settings;
	
	protected boolean fullscreen, vsync;
	protected int width, height, fps;
	private double maxDelta;
	
	private volatile boolean running;
	
	public String title = "Korome Game", version = "korome A";
	public final static String E_VERSION = "A";
	
	public Game() throws LWJGLException, IOException {
		settings = new Settings("settings.properties");
		
		fullscreen = Boolean.parseBoolean(settings.get("fullscreen", "false"));
		
		vsync = Boolean.parseBoolean(settings.get("vsync", "false"));
		
		fps = Integer.parseInt(settings.get("fps", "60"));
		
		maxDelta = Double.parseDouble(settings.get("maximum-delta", ".02"));
		
		String[] resolution = settings.get("resolution", "1280x720").split("x");
		
		width = Integer.valueOf(resolution[0]);
		
		height = Integer.valueOf(resolution[1]);
		
		defaultFocus = new Vector(width/2, height/2);
		setFocus(defaultFocus);
		
		setDisplayMode();
		Display.setTitle(title);
		Display.setResizable(false);
		Display.setIcon(new ByteBuffer[] {Texture.getByteBuffer("resources/logo.png"),
	        Texture.getByteBuffer("resources/logo_16.png")});
		Display.setVSyncEnabled(vsync);
		Display.setInitialBackground(0f, 0f, 0f);
		Display.create();
		AL.create();
		Draw.initialise(width, height);
		Play.initialise();
		
		Mouse.create();
		Keyboard.create();
		
		lastTime = Sys.getTime() * 1000 / Sys.getTimerResolution();
		deltas = new ArrayList<Double>();
	}
	
	public static void beforeMain(){
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/lib/natives");
		if(!Single.single())
			System.exit(101);
	}
	
	public static void main(Game game) {
		try{
			game.run();
		}catch(Exception e){
			game.crash(e);
			game.running = false;
		}
		
		if (game != null)
			try {
				game.cleanup();
			} catch (Exception ex) {
				ex.printStackTrace();
				String error = ex.getClass() + ": " + ex.getMessage();
				for (StackTraceElement ste : ex.getStackTrace())
					error += "\n     at " + ste.toString();
				JOptionPane.showMessageDialog(null,
						"A fatal error has occoured:\n" + error, "Fatal Error",
						JOptionPane.ERROR_MESSAGE);
			}
	}
	
	public void run() throws IOException {
		running = true;
		while(running){
			if(Display.isCloseRequested())
				running = false;
			else {
				Display.update();
				logic();
				setFocus(defaultFocus);
				render();
				hudRender();
				
				Display.sync(fps);
			}
		}
	}
	
	private Vector focus;
	final Vector defaultFocus;
	
	public void setFocus(Vector focus){
		this.focus = focus;
	}
	
	public void render() throws IOException {
		Draw.beforeDraw(focus);
	}
	
	public void hudRender() throws IOException{
		glPopMatrix();
	}

	public void crash(Exception e){
		try {
			System.setErr(new PrintStream("error.log"));
		} catch (FileNotFoundException fnfE) {}
		System.err.println(title + " error log:\nVERSION: \"" + version + "\"\nENGINE VERSION : \"" + E_VERSION + "\"");
		e.printStackTrace();
		String err = e.getClass().getName() + ": " + e.getMessage();
		for (StackTraceElement ste : e.getStackTrace())
			err += "\n     at " + ste.toString();
		Sys.alert(title, title + " has crashed and the game will exit.\n\n" + err + "\n\nPlease send us the error log to help us fix this for later.");
	}
	
	private long lastTime, lastFrame;
	
	double highestDelta = 0d;
	double lowestDelta = 1d;
	double deltaSum = 0d;
	
	private long frames = 0, lastF = 0;
	protected boolean first = true;
	
	protected InfoPacket info;
	
	List<Double> deltas;
	public void logic() {
		int mx = Mouse.getX();
		int my = height - Mouse.getY();
		
		long time = Sys.getTime() * 1000 / Sys.getTimerResolution();
		double delta = (time-lastTime) / 1000d;
		lastTime = time;
		frames++;
		
		deltas.add(delta);
		deltaSum += delta;
	
		highestDelta = Math.max(highestDelta, delta);
		lowestDelta = Math.min(lowestDelta, delta);
			
		if(lastFrame+1000 <= time){
			if(!first)
				Display.setTitle(title + " FPS: " + (frames - lastF));
			lastF = frames;
			lastFrame = time;
		}
		
		if(first)
			first = false;
		
		//To make sure things don't just fling themselves to a completely different place
		if(delta>maxDelta)
			delta = maxDelta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			running = false;
		
		int[] events = new int[Keyboard.getNumKeyboardEvents()];
		byte i = 0;
		try{
			while(next()){
				if(getEventKeyState())
					events[i] = getEventKey();
				i++;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Crash avoided.\naioobe");
			events = new int[]{0};
		}
		
		info = new InfoPacket(mx, my, delta, events);
	}
	
	public void cleanup() {
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		Draw.clearCache();
		Play.clearCache();
		
		int errorflag = glGetError();

		if (errorflag != GL_NO_ERROR)
			System.err.println(Util.translateGLErrorString(errorflag));

		AL.destroy();
		Display.destroy();
		Mouse.destroy();
		Keyboard.destroy();
		
		try{
			settings.close();
		}catch (IOException e) {
			System.err.println("Error while saving settings");
			e.printStackTrace();
		}
		
		double averageDelta = deltaSum / frames;
		
		System.out.println("Average delta: " + averageDelta);
		System.out.println("Highest delta: " + highestDelta);
		System.out.println("Lowest delta: " + lowestDelta);
		System.out.println("Average FPS: " + (1/averageDelta));
		System.out.println("Total frames: " + frames);
		System.out.println("Runtime: " + (deltaSum));
		
		try {
			PrintStream deltaLog = new PrintStream("deltas.log");
			for(Double d: deltas)
				deltaLog.println(d);
			deltaLog.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setDisplayMode() throws LWJGLException{
		if((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen))
			return;
		
		DisplayMode target = null;
		
		if(fullscreen) {
			DisplayMode[] available = Display.getAvailableDisplayModes();
			
			for (DisplayMode mode : available)
				if (mode.getWidth() == width && mode.getHeight() == height && mode.isFullscreenCapable())
					if (target == null)
						target = mode;
					else if (mode.getBitsPerPixel() > target.getBitsPerPixel() || mode.getFrequency() > target.getFrequency())
						target = mode;
		}else
			target = new DisplayMode(width, height);
		
		if (target == null) {
			System.err.println("Failed to find value mode: "+width+"x"+height+" fullscreen." + "\n Running without fullscreen");
			target = new DisplayMode(width, height);
			fullscreen = false;
		}
		

		Display.setDisplayMode(target);
		Display.setFullscreen(fullscreen);
		
		System.out.println(target.toString() + "  F: " + fullscreen);
	}
}
