package org.xtremeturmoil.standalone;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.io.FileUtils;
import org.xtremeturmoil.helpers.AddProvider;

/**
 * Class for building a private/public key pair.
 * @author jataylor2012
 *
 */
public class MakeKeys {

	private String output;
	private String name;

	/**
	 * Stand-alone utility for building public/private key pair.
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length>=2) {
			String output = args[0];
			String name = args[1];
			MakeKeys mk = new MakeKeys(output, name);
			try {
				mk.create();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Usage: c:/tmp/ keyFriendlyName");
		}
	}

	/**
	 * Output is the key output directory e.g. c:/tmp/
	 * Name is the name of the key e.g. Test
	 * @param output
	 * @param name
	 */
	public MakeKeys(String output, String name) {
		this.output = output;
		this.name = name;
		init();
	}
	
	private void init() {
		AddProvider.INSTANCE.init();
	}
	
	/**
	 * Create public and private keys and write to file.
	 * @throws NoSuchAlgorithmException
	 */
	public void create() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair key = keyGen.generateKeyPair();
		PrivateKey privateKey = key.getPrivate();
		PublicKey publicKey = key.getPublic();
		
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		
		try {
			FileUtils.writeByteArrayToFile(new File(output+name+".pub"), x509EncodedKeySpec.getEncoded());
			FileUtils.writeByteArrayToFile(new File(output+name+".priv"), pkcs8EncodedKeySpec.getEncoded());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
