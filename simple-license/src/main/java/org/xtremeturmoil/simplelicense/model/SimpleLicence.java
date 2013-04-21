package org.xtremeturmoil.simplelicense.model;

import java.util.Date;

/**
 * Simple License Model implementation.
 * @author jataylor2012
 *
 */
public class SimpleLicence {

	private int validForDays;
	private String companyName;
	private int numberOfUnits;
	private Date start;
	private final static long ONE_DAY = 60000*60*24;

	public SimpleLicence(int validForDays, String companyName, int numberOfUnits, Date start) {
		this.validForDays = validForDays;
		this.companyName = companyName;
		this.numberOfUnits = numberOfUnits;
		this.start = start;
	}
	
	public SimpleLicence(String serialized) {
		String[] values = serialized.split("\\|");
		this.validForDays = Integer.valueOf(values[0]);
		this.companyName = values[1];
		this.numberOfUnits = Integer.valueOf(values[2]);
		this.start = new Date(Long.valueOf(values[3]));
	}

	public int getValidForDays() {
		return validForDays;
	}

	public void setValidForDays(int validForDays) {
		this.validForDays = validForDays;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(int numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}
	
	@Override
	public String toString() {
		return validForDays+"|"+companyName+"|"+numberOfUnits+"|"+start.getTime();
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	/**
	 * Is this licence in date?
	 * @return
	 */
	public boolean check() {
		Date now = new Date();
		Date end = new Date(start.getTime()+(validForDays*ONE_DAY));
		return now.before(end);
	}

}
