package com.lfalch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class KoromeServerTestClient {
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		Socket connection = new Socket(InetAddress.getByName("127.0.0.1"), 1337);
		ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
		
		
		Object packet = "";
		while(!packet.equals("REJECT")){
			System.out.println("waiting");
			int type = input.readInt();
			packet = input.readObject();
			
			System.out.println(type + ":" + packet);
			
			output.writeInt(type);
			output.writeObject(packet);
			output.flush();
		}
		
		output.close();
		input.close();
		connection.close();
	}
}
