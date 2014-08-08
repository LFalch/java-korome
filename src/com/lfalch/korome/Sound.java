package com.lfalch.korome;

import static org.lwjgl.openal.AL10.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.lwjgl.openal.Util;
import org.lwjgl.util.WaveData;

public class Sound {
	
	private int buffer, source;
	
	public Sound(String file, boolean loop) throws FileNotFoundException{
		WaveData data = WaveData.create(new BufferedInputStream(new FileInputStream("resources/" + file + ".wav")));
		
		buffer = alGenBuffers();
		Util.checkALError();
		
		alBufferData(buffer, data.format, data.data, data.samplerate);
		Util.checkALError();
		
		data.dispose();
		
		source = alGenSources();
		alSourcei(source, AL_BUFFER, buffer);
		Util.checkALError();
		
		if(loop)
			alSourcei(source, AL_LOOPING, AL_TRUE);
		
		System.out.println("Generated #"+source+" " + file + " loop: " + loop);
	}
	
	public void play(){
		alSourcePlay(source);
	}
	
	public void pause(){
		alSourcePause(source);
	}
	
	public void stop(){
		alSourceStop(source);
	}
	
	public void delete(){
		alDeleteBuffers(buffer);
		alDeleteSources(source);
	}
}
