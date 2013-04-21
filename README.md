simple-license
==============

A simple licensing implementation with bouncy castle using public/private keys.

Usage (Can be found in unit tests):


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

