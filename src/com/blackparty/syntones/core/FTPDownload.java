package com.blackparty.syntones.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPDownload {
	
	FTPClient ftp = null;
	
	public FTPDownload(String host, String user, String pwd) throws Exception{
		ftp = new FTPClient();
		int reply;
		ftp.connect(host);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new Exception("Exception in connecting to FTP Server");
		}
		ftp.login(user, pwd);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
		
	}
	public String downloadFile(String fileName)
			throws Exception {
		String remoteFile = "/songUploaded/"+fileName;
		File downloadFile = new File("C:/Users/YLaya/Desktop/downloaded/"+fileName);
		OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile));
        InputStream inputStream = ftp.retrieveFileStream(remoteFile);
        byte[] bytesArray = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(bytesArray)) != -1) {
            outputStream2.write(bytesArray, 0, bytesRead);
        }

        boolean success = ftp.completePendingCommand();
        if (success) {
            System.out.println("File has been downloaded successfully.");
        }
        outputStream2.close();
        inputStream.close();

		return "C:/Users/YLaya/Desktop/downloaded/"+fileName;
	}
	
	public void disconnect(){
		if (this.ftp.isConnected()) {
			try {
				this.ftp.logout();
				this.ftp.disconnect();
			} catch (IOException f) {
				// do nothing as file is already saved to server
			}
		}
	}
	
}
