/*******************************************************************************
 * Copyright © Squid Solutions, 2016
 *
 * This file is part of Open Bouquet software.
 *  
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 *
 * There is a special FOSS exception to the terms and conditions of the 
 * licenses as they are applied to this program. See LICENSE.txt in
 * the directory of this program distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * Squid Solutions also offers commercial licenses with additional warranties,
 * professional functionalities or services. If you purchase a commercial
 * license, then it supersedes and replaces any other agreement between
 * you and Squid Solutions (above licenses and LICENSE.txt included).
 * See http://www.squidsolutions.com/EnterpriseBouquet/
 *******************************************************************************/
package com.squid.core.jdbc.formatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


public class DataFormatter implements IDataFormatter {
	
	private SimpleDateFormat DATE_FORMATTER = null;
	private SimpleDateFormat TIMESTAMP_FORMATTER = null;

//    private String timestampFormat = "dd-MM-yyyy HH:mm:ss.SSS";
//    private String dateFormat = "dd-MM-yyyy";

	private String timestampFormat = "yyyy-MM-dd HH24:mm:ss.SSS";
  	private String dateFormat = "yyyy-MM-dd";


	private DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("0.##########");//(DecimalFormat)DecimalFormat.getInstance();
    
    public DataFormatter() {
    	DATE_FORMATTER = new SimpleDateFormat(dateFormat);
    	TIMESTAMP_FORMATTER = new SimpleDateFormat(timestampFormat);
    	setUseGrouping(false);
    	setDecimalSeparator('.');
    	DEFAULT_DECIMAL_FORMAT.setMaximumFractionDigits(10);
    }
    
    /**
     * if init=true, force the decimal format to match SQL requirements.
     * @param init
     */
    public DataFormatter(boolean init) {
    	DATE_FORMATTER = new SimpleDateFormat(dateFormat);
    	TIMESTAMP_FORMATTER = new SimpleDateFormat(timestampFormat);
    	if (init) {
        	setUseGrouping(false);
        	setDecimalSeparator('.');
        	DEFAULT_DECIMAL_FORMAT.setMaximumFractionDigits(10);
    	}
    }

    public DataFormatter(Properties pref) {
        if (pref != null) {
            load(pref);
        }
    }

	public DataFormatter(DataFormatter prefs) {
		this.setDateFormat(prefs.getDateFormat());
		this.setDecimalSeparator(prefs.getDecimalSeparator());
		this.setMaximumFractionDigits(prefs.getMaximumFractionDigits());
		this.setTimestampFormat(prefs.getTimestampFormat());
		this.setUseGrouping(prefs.isUseGrouping());
	}
	
	public void applyDecimalPattern(String pattern) {
		DEFAULT_DECIMAL_FORMAT.applyPattern(pattern);
	}
	
	public void applyDatePattern(String pattern) {
        DATE_FORMATTER.applyPattern(pattern);
    }
	
	public void applyTimestampPattern(String pattern) {
        TIMESTAMP_FORMATTER.applyPattern(pattern);
    }

	public void load(Properties pref) {
		if (pref.getProperty(IDataFormatterPrefs.DATE_PREF)!=null) {
			this.setDateFormat(pref.getProperty(IDataFormatterPrefs.DATE_PREF));
		}
		if (pref.getProperty(IDataFormatterPrefs.TIMESTAMP_PREF)!=null) {
			this.setTimestampFormat(pref.getProperty(IDataFormatterPrefs.TIMESTAMP_PREF));
		}
		if (pref.getProperty(IDataFormatterPrefs.DECIMAL_PREF)!=null) {
			this.setDecimalSeparator(pref.getProperty(IDataFormatterPrefs.DECIMAL_PREF).charAt(0));
		}
		if (pref.getProperty(IDataFormatterPrefs.USEGROUP_PREF)!=null) {
			this.setUseGrouping(pref.getProperty(IDataFormatterPrefs.USEGROUP_PREF).equals("T"));
		}
	}
	
	public void save(Properties pref) {
		pref.setProperty(IDataFormatterPrefs.DATE_PREF, this.getDateFormat());
		pref.setProperty(IDataFormatterPrefs.TIMESTAMP_PREF, this.getTimestampFormat());
		pref.setProperty(IDataFormatterPrefs.DECIMAL_PREF, Character.toString(this.getDecimalSeparator()));
		pref.setProperty(IDataFormatterPrefs.USEGROUP_PREF, this.isUseGrouping()?"T":"F");
	}

	/* (non-Javadoc)
	 * @see com.squid.core.jdbc.formatter.IDataFormatter#getTimestampFormat()
	 */
	
	public String getTimestampFormat() {
		return timestampFormat;
	}

	
	public void setTimestampFormat(final String timestampFormat) {
		this.timestampFormat = timestampFormat;
		TIMESTAMP_FORMATTER = new SimpleDateFormat(timestampFormat);
	}

	/* (non-Javadoc)
	 * @see com.squid.core.jdbc.formatter.IDataFormatter#getDateFormat()
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(final String dateFormat) {
		this.dateFormat = dateFormat;
		DATE_FORMATTER = new SimpleDateFormat(dateFormat);
	}

	/* (non-Javadoc)
	 * @see com.squid.core.jdbc.formatter.IDataFormatter#getDecimalSeparator()
	 */
	public char getDecimalSeparator() {
		return DEFAULT_DECIMAL_FORMAT.getDecimalFormatSymbols().getDecimalSeparator();
	}

	public void setDecimalSeparator(final char decimalSeparator) {
    	DecimalFormatSymbols x = DEFAULT_DECIMAL_FORMAT.getDecimalFormatSymbols();
    	x.setDecimalSeparator(decimalSeparator);
    	DEFAULT_DECIMAL_FORMAT.setDecimalFormatSymbols(x);
	}

	public boolean isUseGrouping() {
		return DEFAULT_DECIMAL_FORMAT.isGroupingUsed();
	}

	public void setUseGrouping(final boolean useGrouping) {
		DEFAULT_DECIMAL_FORMAT.setGroupingUsed(useGrouping);
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.jdbc.formatter.IDataFormatter#formatDecimal(java.lang.Object)
	 */
	public String formatDecimal(final Object number) {
		return DEFAULT_DECIMAL_FORMAT.format(number);
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.jdbc.formatter.IDataFormatter#formatDate(java.lang.Object)
	 */
	public String formatDate(final Object date) {
		return DATE_FORMATTER.format(date);
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.jdbc.formatter.IDataFormatter#formatTimestamp(java.lang.Object)
	 */
	public String formatTimestamp(final Object date) {
		return TIMESTAMP_FORMATTER.format(date);
	}
	
	public Date parseDate(String date) throws ParseException {
		return DATE_FORMATTER.parse(date);
	}
	
	public Date parseTimestamp(String timestamp) throws ParseException {
		return TIMESTAMP_FORMATTER.parse(timestamp);
	}
    
	public void setMaximumFractionDigits(final int digits) {
		DEFAULT_DECIMAL_FORMAT.setMaximumFractionDigits(digits);
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.jdbc.formatter.IDataFormatter#getMaximumFractionDigits()
	 */
	public int getMaximumFractionDigits() {
		return DEFAULT_DECIMAL_FORMAT.getMaximumFractionDigits();
	}
	
}
