package com.lfalch.korome;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.lwjgl.Sys;

public class Server {
	Settings settings;
	double maxDelta;
	int port, backlog;
	
	public String title = "Korome_MP", version = "1.0";
	private long lastTime;
	private ArrayList<Double> deltas;
	private int frames;
	private double deltaSum;
	private boolean running;
	
	private ServerSocket server;
	
	public Server() throws IOException{
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/lib/natives");
		Sys.initialize();
		
		settings = new Settings("settings.properties");
		
		maxDelta = Double.parseDouble(settings.get("maximum-delta", ".02"));
		port = Integer.parseInt(settings.get("port", "43566"));
		backlog = Integer.parseInt(settings.get("backlog", "2"));
		
		lastTime = Sys.getTime() * 1000 / Sys.getTimerResolution();
		deltas = new ArrayList<Double>();
	}
	
	public void logMsg(String msg){
		String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.printf("<%s>: %s\n", time, msg);
	}
	
	public void run(){
		running = true;
		
		try {
			server = new ServerSocket(port, backlog);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(running){
			try {
				logMsg("Waiting for client to connect.");
				Client clientLink = new Client(server);
				logMsg("Connection with client established (IP: " + clientLink.getIP() + ")");
				clientLink.addLogger(new Handler(){
					@Override
					public void handle(Object obj) {
						String msg = (String) obj;
						logMsg(msg);
					}
				});
				logMsg("Logger set");
				clientLink.send(Client.PING, Maths.class);
				logMsg("Test PING sent");
			}
			catch (IOException e) {
				crash(e);
			}
			logic();
			serverLogic();
		}
		cleanup();
	}
	
	public void logic() {
		long time = Sys.getTime() * 1000 / Sys.getTimerResolution();
		double delta = (time-lastTime) / 1000d;
		lastTime = time;
		frames++;
		
		deltas.add(delta);
		deltaSum += delta;
	
		//To make sure things don't just fling themselves to a completely different place
		if(delta>maxDelta)
			delta = maxDelta;
	}
	
	public void serverLogic() {
		
	}
	
	public void crash(Exception e){
		try {
			System.setErr(new PrintStream("error.log"));
		} catch (FileNotFoundException fnfE) {}
		System.err.println(title + " error log:\nVERSION: \"" + version + "\"\nENGINE VERSION : \"" + Game.E_VERSION + "\"");
		e.printStackTrace();
		String err = e.getClass().getName() + ": " + e.getMessage();
		for (StackTraceElement ste : e.getStackTrace())
			err += "\n     at " + ste.toString();
		Sys.alert(title, title + " has crashed and the game will exit.\n\n" + err + "\n\nPlease send us the error log to help us fix this for later.");
	}
	
	public void cleanup() {
		//Cool server networky awesome stuff (sadly they're all closing now)
		try{
			server.close();
		}catch(IOException e){
			System.err.println("Error while closing streams");
			e.printStackTrace();
		}
		
		
		//Boring game stuff :P
		try{
			settings.close();
		}catch (IOException e) {
			System.err.println("Error while saving settings");
			e.printStackTrace();
		}
		
		double averageDelta = deltaSum / frames;
		
		System.out.println("Average delta: " + averageDelta);
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
}
