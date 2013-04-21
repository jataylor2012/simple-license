package org.xtremeturmoil.test;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xtremeturmoil.simplelicense.model.SimpleLicence;
import org.xtremeturmoil.standalone.MakeKeys;
import org.xtremeturmoil.standalone.MakeLicense;
import org.xtremeturmoil.standalone.ReadLicense;

public class TestMakeLicense {

	private Date now;
	private final static String COMPANY = "MyCompany";
	private final static int VALID_FOR = 30;
	private final static int UNITS = 10;

	@Before
	public void init() {
		MakeKeys mk = new MakeKeys("", "test");
		now = new Date();
		try {
			mk.create();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMakeLicense() {
		try {
			MakeLicense mkLicence = new MakeLicense(VALID_FOR, COMPANY, UNITS, now);
			mkLicence.setKey("test.priv");
			byte[] cipherlicence = mkLicence.create();
			mkLicence.write(COMPANY+".license", cipherlicence);
			
			ReadLicense rlLicense = new ReadLicense("test.pub", COMPANY+".license");
			SimpleLicence sl = rlLicense.get();
			Assert.assertNotNull(sl);
			Assert.assertEquals(COMPANY, sl.getCompanyName());
			Assert.assertEquals(VALID_FOR, sl.getValidForDays());
			Assert.assertEquals(UNITS, sl.getNumberOfUnits());
			Assert.assertEquals(now, sl.getStart());
			Assert.assertTrue(sl.check());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMakeInvalidLicense() {
		try {
			MakeLicense mkLicence = new MakeLicense(VALID_FOR, COMPANY, UNITS, new SimpleDateFormat("dd/mm/yyyy").parse("01/01/2012"));
			mkLicence.setKey("test.priv");
			byte[] cipherlicence = mkLicence.create();
			mkLicence.write(COMPANY+".license", cipherlicence);
			
			ReadLicense rlLicense = new ReadLicense("test.pub", COMPANY+".license");
			SimpleLicence sl = rlLicense.get();
			Assert.assertNotNull(sl);
			Assert.assertEquals(COMPANY, sl.getCompanyName());
			Assert.assertEquals(VALID_FOR, sl.getValidForDays());
			Assert.assertEquals(UNITS, sl.getNumberOfUnits());
			Assert.assertFalse(sl.check());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
