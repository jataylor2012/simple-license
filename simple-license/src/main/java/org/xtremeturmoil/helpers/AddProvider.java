package org.xtremeturmoil.helpers;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public enum AddProvider {

	INSTANCE;
	
	private AddProvider() {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public void init() {
		//Nothing to do here.
	}

}
