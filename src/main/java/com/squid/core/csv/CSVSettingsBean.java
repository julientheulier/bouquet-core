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
package com.squid.core.csv;

import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Properties;

import com.squid.core.jdbc.formatter.DataFormatter;

public class CSVSettingsBean
extends DataFormatter
{
	private static final String SETTING_PREF= "com.squid.thirdparty.opencsv.prefs";

	public static final String SEPARATOR_PREF= SETTING_PREF+"/SEPARATOR";
	public static final String QUOTECHAR_PREF= SETTING_PREF+"/QUOTECHAR";
	public static final String ESCAPECHAR_PREF= SETTING_PREF+"/ESCAPECHAR";
	public static final String USEENCLOSING_PREF= SETTING_PREF+"/USEQUOTE";
	public static final String INSERTHEADER_PREF= SETTING_PREF+"/INSERTHEADER";
	public static final String FILEENCODING_PREF= SETTING_PREF+"/FILEENCODING";
	public static final String DOUBLEENCLOSE_PREF= SETTING_PREF+"/DOUBLEENCLOSE";

	private String filepath = "";

	private char separator = DEFAULT_SEPARATOR;

	private char quotechar = DEFAULT_QUOTE_CHARACTER;

	private char escapechar = DEFAULT_ESCAPE_CHARACTER;

	private String lineEnd = DEFAULT_LINE_END;

	private boolean useEnclosing = DEFAULT_USE_ENCLOSING;

	private boolean insertHeader = DEFAULT_INSERT_HEADER;

	private String fileEncoding = Charset.defaultCharset().name();
	
	private boolean doubleEnclose = DEFAULT_DOUBLE_ENCLOSE;

	/** Default use of the enclosing character.
	 * DO NOT CHANGE he default boolean pref to true as it may cause a bug :
	 * when default is true & this pref not changed, the csv wizard export shows by default false the first time (but the csv pref wizard works on it side)
	 */
	public static final boolean DEFAULT_INSERT_HEADER = true;

	/** Default use of the enclosing character.
	 * DO NOT CHANGE he default boolean pref to true as it may cause a bug :
	 * when default is true & this pref not changed, the csv wizard export shows by default false the first time (but the csv pref wizard works on it side)
	 */
	public static final boolean DEFAULT_USE_ENCLOSING = true;

	/** Default line terminator uses platform encoding. */
	public static final String DEFAULT_LINE_END = "\n";

	/** The escape constant to use when you wish to suppress all escaping. */
	public static final char NO_ESCAPE_CHARACTER = '\u0000';

	/** The quote constant to use when you wish to suppress all quoting. */
	public static final char NO_QUOTE_CHARACTER = '\u0000';

	/**
	 * The default quote character to use if none is supplied to the
	 * constructor.
	 */
	public static final char DEFAULT_QUOTE_CHARACTER = '"';

	/** The default separator to use if none is supplied to the constructor. */
	public static final char DEFAULT_SEPARATOR = ',';

	/** The character used for escaping quotes. */
	public static final char DEFAULT_ESCAPE_CHARACTER = '"';
	
	/**
	 * Allow to specify how to escape the enclosing char encountered in an enclosed string: double it or use the escape character
	 */
	public static final boolean DEFAULT_DOUBLE_ENCLOSE = false;

	public CSVSettingsBean() {
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	public char getQuotechar() {
		return quotechar;
	}

	public void setQuotechar(char quotechar) {
		this.quotechar = quotechar;
	}

	public char getEscapechar() {
		return escapechar;
	}

	public void setEscapechar(char escapechar) {
		this.escapechar = escapechar;
	}

	public String getLineEnd() {
		return lineEnd;
	}

	public void setLineEnd(String lineEnd) {
		this.lineEnd = lineEnd;
	}

	public boolean isUseEnclosing() {
		return useEnclosing;
	}

	public void setUseEnclosing(boolean useEnclosing) {
		this.useEnclosing = useEnclosing;
	}

	public boolean isInsertHeader() {
		return insertHeader;
	}

	public void setInsertHeader(boolean insertHeader) {
		this.insertHeader = insertHeader;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}
	
	public boolean isDoubleEnclose() {
		return doubleEnclose;
	}

	public void setDoubleEnclose(boolean doubleEnclose) {
		this.doubleEnclose = doubleEnclose;
	}

	public void setPreferences(Properties props) {
		if (props!=null) {
			Enumeration<Object> propertiesKeys  =  props.keys();
			while (propertiesKeys.hasMoreElements()) {
				String key = (String)propertiesKeys.nextElement();
				String value =  (String)props.get(key);
				if (key.equals(ESCAPECHAR_PREF)) {
					setEscapechar(value.length()==1?value.charAt(0):NO_ESCAPE_CHARACTER);
				} else if (key.equals(FILEENCODING_PREF)) {
					setFileEncoding(value);
				} else if (key.equals(INSERTHEADER_PREF)) {
					setInsertHeader(new Boolean(value).booleanValue());
				} else if (key.equals(QUOTECHAR_PREF)) {
					setQuotechar(value.length()==1?value.charAt(0):NO_QUOTE_CHARACTER);
				} else if (key.equals(SEPARATOR_PREF)) {
					setSeparator(value.charAt(0));
				} else if (key.equals(USEENCLOSING_PREF)) {
					setUseEnclosing(new Boolean(value).booleanValue());
				} else if (key.equals(DOUBLEENCLOSE_PREF)) {
					setDoubleEnclose(new Boolean(value).booleanValue());
				}
			}
		}

	}
}
