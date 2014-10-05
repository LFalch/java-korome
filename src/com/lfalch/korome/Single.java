package com.lfalch.korome;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Single
 * 
 * @author Lucas Falch <lucas@lfalch.com>
 * @since October 14, 2012
 */

public class Single {
	
	private static File f;
	private static FileChannel channel;
	private static FileLock lock;
	
	public static boolean single() {
		try{
			try{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.out.println("Couldn't get that look and feel");
			}
			
			f = new File(System.getProperty("user.home") + "/game.lck");
			
			if(f.exists())
				f.delete();
			
			channel = new RandomAccessFile(f, "rw").getChannel();
			lock = channel.tryLock();
			
			if(lock == null){
				channel.close();
				JOptionPane.showMessageDialog(null, "To prevent crashes, only one instance is allowed", "Sorry!", JOptionPane.PLAIN_MESSAGE);
				return false;
			}
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						if(lock != null){
							lock.release();
							channel.close();
							f.delete();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}));
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, "Couldn't start proccess\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
