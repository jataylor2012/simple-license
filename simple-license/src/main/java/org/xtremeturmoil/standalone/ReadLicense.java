package org.xtremeturmoil.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.xtremeturmoil.simplelicense.model.SimpleLicence;

/**
 * There is no stand-alone utility for reading a license as you
 * are expected to integrate into your application.
 * @author jataylor2012
 *
 */
public class ReadLicense {

	private String key;
	private String file;
	public static final String PUBLIC = "pub";
	public static final String PRIVATE = "priv";
	private String type;
	private byte[] byteKey;

	/**
	 * Remember, if you encrypted with a private key you should decrypt 
	 * with a public key and vice versa.
	 * @param key
	 * @param file
	 */
	public ReadLicense(String key, String file) {
		this.key = key;
		this.file = file;
	}
	
	public ReadLicense(String type, byte[] byteKey, String file) {
		this.type = type;
		this.byteKey = byteKey;
		this.file = file;
	}
	
	/**
	 * Returns a decrypted representation of the current license.
	 * @return
	 */
	public SimpleLicence get() {
		SimpleLicence licence = null;
		BufferedReader in = null;
		try {
			byte[] cipherText = FileUtils.readFileToByteArray(new File(file));
			UseKeys decrypt = getUseKeys();
			byte[] plainText = decrypt.decrypt(cipherText);
			licence = new SimpleLicence(new String(plainText));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return licence;
	}
	
	private UseKeys getUseKeys() {
		if(this.type!=null) {
			return new UseKeys(this.type, this.byteKey);
		} else {
			return new UseKeys(key);
		}
	}
}
