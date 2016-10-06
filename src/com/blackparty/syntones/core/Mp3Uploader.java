package com.blackparty.syntones.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Mp3Uploader {

	// code to save mp3 here...
	public String upload(File file,long songId,long artistId) throws Exception{
		
		FTPUploader ftpUploader = new FTPUploader("127.0.0.1", "yeyah", "yeyah");
		String fileName = ftpUploader.uploadFile(file.toPath().toString(), artistId+"-"+songId+".mp3", "/songUploaded/");
		ftpUploader.disconnect();
		System.out.println("Done");
//		uploadDirectory = uploadDirectory.concat(artistId+"/");
//		File newFile = new File(uploadDirectory);
//		if(!newFile.exists()){
//			newFile.mkdir();
//		}
//		byte data[] = Files.readAllBytes(file.toPath());
//		FileOutputStream out = new FileOutputStream(uploadDirectory+songId+".mp3");
//		out.write(data);
//		out.close();
		return fileName;
	}

}
