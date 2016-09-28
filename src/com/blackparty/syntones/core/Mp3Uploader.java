package com.blackparty.syntones.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Mp3Uploader {

	// palihug ko himo properties file para adto ra mo basa ang app kung asa man
	// gusto nato i-save ang uploaded mp3 file
	// sa desktop nalang ni
	// private String uploadDirectory = System.getProperty("user.dir");

	// code to save mp3 here...
	public String upload(File file, long songId, long artistId)throws Exception {
		File pfile = new File("127.0.0.1/songUploaded");
//		if (!file.exists()) {
//			if (file.mkdir()) {
//				System.out.println("Directory is created!");
//			}
//		}
		FTPUploader ftpUploader = new FTPUploader("127.0.0.1", "yeyah", "yeyah");
		String fileName = ftpUploader.uploadFile(file.toPath().toString(),artistId + "-" + songId + ".mp3", "/songUploaded/");
		ftpUploader.disconnect();
		System.out.println("Done");

		return fileName;

	}

}
/*
 * get songId = filepathsong = /songUploaded/1-1.mp3
 */