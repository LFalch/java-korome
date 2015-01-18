package com.lfalch.korome;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class Play {
	
	private static HashMap<String, Sound> cache;
	
	public static void initialise(){
		cache = new HashMap<String, Sound>();
	}
	
	public static Sound getSound(String name, boolean loop) throws FileNotFoundException{
		Sound sound;
		if(cache.containsKey(name))
			sound = cache.get(name);
		else
			sound = newSound(name, loop);
		return sound;
	}
	
	private static Sound newSound(String fileName, boolean loop) throws FileNotFoundException{
		Sound newSnd;
		newSnd = new Sound(fileName, loop);
		cache.put(fileName, newSnd);
		
		return newSnd;
	}
	
	public static void clearCache(){
		for(Sound sound: cache.values())
			sound.delete();
		cache = new HashMap<String, Sound>();
	}
	
	public static void play(String name) throws FileNotFoundException{
		Sound snd = getSound(name, false);
		snd.play();
	}
	
	public static void stop(String name) throws FileNotFoundException{
		Sound snd = getSound(name, false);
		snd.stop();
	}
	
	public static void stopAll(){
		for(Sound s: cache.values())
			s.stop();
	}
	
	public static void loop(String name) throws FileNotFoundException{
		Sound snd = getSound(name, true);
		snd.play();
	}
}
