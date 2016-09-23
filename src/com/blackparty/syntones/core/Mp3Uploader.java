package com.blackparty.syntones.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Mp3Uploader {

	// palihug ko himo properties file para adto ra mo basa ang app kung asa man
	// gusto nato i-save ang uploaded mp3 file
	// sa desktop nalang ni

	private String uploadDirectory = "D:/Our_Files1/Eric/School/Thesis/Syntones/Songs/Uploaded/";

	// code to save mp3 here...
	public void upload(File file,long songId,long artistId) throws IOException{
		uploadDirectory = uploadDirectory.concat(artistId+"/");
		File newFile = new File(uploadDirectory);
		if(!newFile.exists()){
			newFile.mkdir();
		}
		byte data[] = Files.readAllBytes(file.toPath());
		FileOutputStream out = new FileOutputStream(uploadDirectory+songId+".mp3");
		out.write(data);
		out.close();
	}

}
