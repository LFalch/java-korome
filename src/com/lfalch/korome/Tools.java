package com.lfalch.korome;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;


public class Tools {
	public static byte[] trimByteArray(byte[] bytes) {
	    int i = bytes.length - 1;
	    while (i >= 0 && bytes[i] == 0)
	    {
	        --i;
	    }

	    return Arrays.copyOf(bytes, i + 1);
	}
	
	public static Object deserialise(byte[] bytes) throws ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream in = null;
		try{
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			
			in.close();
			bis.close();

			return o;
		}
		catch (IOException e) {
			e.printStackTrace();
			try{
				in.close();
			}catch(Exception e1){}
			try{
				bis.close();
			}catch(Exception e1){}
		}
		return null;
	}
	
	public static byte[] serialise(Object o){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try{
			out = new ObjectOutputStream(bos);
			out.writeObject(o);
			byte[] bytes = bos.toByteArray();
			
			out.close();
			bos.close();

			return bytes;
		}
		catch (IOException e) {
			e.printStackTrace();
			try{
				out.close();
			}catch(Exception e1){}
			try{
				bos.close();
			}catch(Exception e1){}
		}
		return null;
	}
}
