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
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SongEncryptDecrypt {
	private static final String salt = "t784";
	private String username;
	private String pathEncrypted ;


	public boolean encryptSong(String path, String username) {
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(path);
			String fname = file.getName();
			FileOutputStream fos = new FileOutputStream(
					pathEncrypted.concat(file.getName() + ".crypt"));
			byte[] key = (salt + username).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			SecretKeySpec sks = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, sks);
			CipherOutputStream cos = new CipherOutputStream(fos, cipher);
			int b;
			byte[] d = new byte[8];
			while ((b = fis.read(d)) != -1) {
				cos.write(d, 0, b);
			}

			cos.flush();
			cos.close();
			fis.close();
			System.out.print("Done encrypting!");
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException
				| NoSuchAlgorithmException | NoSuchPaddingException e) {
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

	public boolean decrypt(File path, String username) {
		try {
			
			FileInputStream fis = new FileInputStream(path);
			String pathString = path.toString();
			if (pathString.endsWith(".mp3.crypt")) {
				pathString = pathString.substring(0, pathString.length() - 10);
			}
			File tempMp3 = File.createTempFile(path.getName().substring(0, path.getName().length() - 10), ".mp3",path.getParentFile());
	        tempMp3.deleteOnExit();
			
			byte[] key = (salt + username).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			SecretKeySpec sks = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, sks);
			CipherInputStream cis = new CipherInputStream(fis, cipher);
			

			FileOutputStream fos = new FileOutputStream(tempMp3);
			int b;
			byte[] d = new byte[8];
			while ((b = cis.read(d)) != -1) {
				fos.write(d, 0, b);
			}
			fos.flush();
			fos.close();
			cis.close();
			System.out.print("Done decrypting!");
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}