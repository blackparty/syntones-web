package com.blackparty.syntones.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SongEncrypt {
	private static final String salt = "t784";
	private static final String cryptPassword = "873147cbn9x5'2 79'79314";
	private static final String pathEncrypted = "C:/Users/YLaya/Desktop/downloaded/";
	
	public boolean encryptSong(String path, File file){
		try {
			FileInputStream fis = new FileInputStream(path);
			String fname = file.getName();
			FileOutputStream fos = new FileOutputStream(pathEncrypted.concat(file.getName()+".crypt"));
			byte[] key = (salt + cryptPassword).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			SecretKeySpec sks = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, sks);
			CipherOutputStream cos = new CipherOutputStream(fos, cipher);
			int b;
	        byte[] d = new byte[8];
	        while((b = fis.read(d)) != -1) {
	            cos.write(d, 0, b);
	        }
	        
	        cos.flush();
	        cos.close();
	        fis.close();
	        System.out.print("Done encrypting!");
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
