package com.blackparty.syntones.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopy {
	
	private String destination = "D:/data/temp/";
	
	public boolean copyFileUsingFileStreams(File source) {
		InputStream input = null;
		OutputStream output = null;
		//
		System.out.println("file name: "+source.getName());
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(destination+source.getName()+".mp3");
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
			input.close();
			output.close();
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
