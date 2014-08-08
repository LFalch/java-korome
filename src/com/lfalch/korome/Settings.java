package com.lfalch.korome;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings implements Closeable {
	Properties prop, read;
	File file;
	
	public Settings(String fileName) throws IOException{
		prop = new Properties();
		file = new File(fileName);
		
		if(file.exists()){
			FileInputStream in = new FileInputStream(file);
			prop.load(in);
			read = (Properties) prop.clone();
			in.close();
		}else
			read = new Properties();
	}
	
	public String get(String property, String defaultProperty){
		String ret = prop.getProperty(property, defaultProperty);
		prop.setProperty(property, ret);
		
		return ret;
	}
	
	@Override
	public void close() throws IOException {
		if (!read.equals(prop)){
			FileOutputStream out = new FileOutputStream(file);
			prop.store(out, null);
			out.close();
		}
	}
}
