package org.xtremeturmoil.test;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xtremeturmoil.standalone.MakeKeys;
import org.xtremeturmoil.standalone.UseKeys;

public class TestDecrypt {
	
	@Before
	public void init() {
		MakeKeys mk = new MakeKeys("", "test");
		try {
			mk.create();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEncryption() {
		String plainText = "Hello, this is some text. Today I made an a simple abstraction layer around bouncy castle and used to" +
				"create a simple license api.";
		UseKeys encrypt = new UseKeys("test.priv");
		UseKeys decrypt = new UseKeys("test.pub");
		try {
			byte[] cipherText = encrypt.encrypt(plainText.getBytes());
			byte[] decrypted = decrypt.decrypt(cipherText);
			Assert.assertEquals(plainText, new String(decrypted));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
