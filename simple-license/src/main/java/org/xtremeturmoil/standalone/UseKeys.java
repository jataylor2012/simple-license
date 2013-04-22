package org.xtremeturmoil.standalone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.xtremeturmoil.helpers.AddProvider;

/**
 * Used to encrypt or decrypt with public/private keys.
 * @author jataylor2012
 *
 */
public class UseKeys {


	private String keyLocation;
	private PrivateKey privateKey;
	private PublicKey publicKey;


	/**
	 * Stand-alone application for test encrypt/decrypt.
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length!=2) {
			System.err.println("Usage: keyLocation plainText");
		} else {
			UseKeys ek = new UseKeys(args[0]);
			FileWriter out = null;
			try {
				byte[] output = ek.encrypt(args[1].getBytes());
				out = new FileWriter("output.txt");
				out.write(new String(output));
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(out!=null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	

	public byte[] encrypt(byte[] plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		Cipher rsaCipher = Cipher.getInstance("RSA");
		if(privateKey!=null) {
			rsaCipher.init(Cipher.ENCRYPT_MODE, privateKey);
		} else {
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		}
		
		byte[] ciphertext = null;
		ciphertext = rsaCipher.doFinal(plainText);
		return ciphertext;
	}
	
	public byte[] decrypt(byte[] cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		Cipher rsaCipher = Cipher.getInstance("RSA");
		if(privateKey!=null) {
			rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
		} else {
			rsaCipher.init(Cipher.DECRYPT_MODE, publicKey);
		}
		
		byte[] plainText = null;
		plainText = rsaCipher.doFinal(cipherText);
		return plainText;
	}


	/**
	 * Use a key from a file.
	 * @param keyLocation
	 */
	public UseKeys(String keyLocation) {
		this.keyLocation = keyLocation;
		init();
	}
	
	/**
	 * Use a key from a byte array. Type is priv or pub.
	 * @param type
	 * @param key
	 */
	public UseKeys(String type, byte[] key) {
		try {
			initFromByteArray(type, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initFromByteArray(String type, byte[] key) throws InvalidKeySpecException, NoSuchAlgorithmException {
		if(type.equals("priv")) {
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
					key);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(privateKeySpec);
		} else {
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
					key);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(publicKeySpec);
		}
	}
	
	private void init() {
		AddProvider.INSTANCE.init();
		if(keyLocation.endsWith(".priv")) {
			initialisePrivateKey();
		} else {
			initialisePublicKey();
		}
	}
	
	private void initialisePrivateKey() {
		File filePrivateKey = new File(keyLocation);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePrivateKey);
			byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
			fis.read(encodedPrivateKey);
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
					encodedPrivateKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(privateKeySpec);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void initialisePublicKey() {
		File filePublicKey = new File(keyLocation);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePublicKey);
			byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
			fis.read(encodedPublicKey);
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
					encodedPublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(publicKeySpec);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String publicKeyToJava() {
		StringBuilder sb = new StringBuilder();
		byte[] key = publicKey.getEncoded();
		sb.append("byte[] key = new byte[");
		sb.append(key.length);
		sb.append("]");
		sb.append(Arrays.toString(key).replace('[', '{').replace(']', '}'));
		sb.append(";");
		return sb.toString();
	}

}
