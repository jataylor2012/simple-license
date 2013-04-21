package org.xtremeturmoil.helpers;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Uses enum constructor to add provider as
 * guaranteed to only run once.
 * @author jataylor2012
 *
 */
public enum AddProvider {

	INSTANCE;
	
	private AddProvider() {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public void init() {
		//Nothing to do here.
	}

}
