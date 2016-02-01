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

import java.io.IOException;
import java.io.Reader;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultJDBCDataFormatter
implements IJDBCDataFormatter
{

	private DataFormatter formatter = null;

	static final Logger logger = LoggerFactory.getLogger(DefaultJDBCDataFormatter.class);

	public DefaultJDBCDataFormatter(DataFormatter prefs, Connection connection) {
		// note: the connection information is useful for some driver (Oracle)
		formatter = prefs;
	}

	public void setDataFormatter(DataFormatter prefs) {
		if (prefs!=null) {
			formatter.setDateFormat(prefs.getDateFormat());
			formatter.setDecimalSeparator(prefs.getDecimalSeparator());
			formatter.setMaximumFractionDigits(prefs.getMaximumFractionDigits());
			formatter.setTimestampFormat(prefs.getTimestampFormat());
			formatter.setUseGrouping(prefs.isUseGrouping());
		}
	}

	public final IDataFormatter getFormatter() {
		return formatter;
	}

	public void loadClasses(){
		
	};

	public void setCustomClassLoader(ClassLoader classloader){
		
	};

	public String formatJDBCObject(final Object jdbcObject, final int colType) throws SQLException {
    	String value = "";


		switch (colType)
		{
			case Types.BIT:
				if (jdbcObject != null) {
					value = String.valueOf(jdbcObject);
				}
			break;
			case Types.BOOLEAN:
				if (jdbcObject!=null) {
					value = new Boolean(jdbcObject.toString()).toString();
				}
			break;
			case Types.BIGINT:
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.REAL:
			case Types.NUMERIC:
				if (jdbcObject != null) {
					value = "" + formatter.formatDecimal(jdbcObject);
				}
			break;
			case Types.INTEGER:
			case Types.TINYINT:
			case Types.SMALLINT:
				if (jdbcObject!=null) {
					value = "" +  formatter.formatDecimal(jdbcObject);
				}
			break;
			case Types.CLOB:
				if (jdbcObject != null) {
					try {
						value = read((Clob)jdbcObject);
					} catch (SQLException sqle) {
						value ="";
					} catch (IOException ioe) {
						value ="";
					}
				}
			break;
			case Types.ARRAY:
				try {
					ResultSet rs = ((Array)jdbcObject).getResultSet();
					int arrayType = ((Array)jdbcObject).getBaseType();
					boolean isNotNew=false;
				    while (rs.next()) {
				    	String current = formatJDBCObject(rs.getObject(2), arrayType);
				    	if ("".equals(current.trim())==false) {
					    	if (isNotNew) {
					    		value =value+",";
					    	} else {
						    	isNotNew = !isNotNew;
					    	}
					    	value = value + current;
				    	}
				    }
				    if ("".equals(value)==false) {
				    	value = "{"+ value+"}";
				    }
				} catch(SQLException sqle) {
					value="";
				}
			break;
			case Types.JAVA_OBJECT:
				if (jdbcObject != null) {
					value = String.valueOf(jdbcObject);
				}
			break;
			case Types.DATE:
				if (jdbcObject != null) {
					value = formatter.formatDate(jdbcObject);
				}
			break;
			case Types.TIME:
				if (jdbcObject != null) {
					value = ((Time)jdbcObject).toString();
				}
			break;
			case Types.TIMESTAMP:
				if (jdbcObject != null) {
					value = formatter.formatTimestamp(jdbcObject);
				}
			break;
			case Types.LONGVARCHAR:
			case Types.VARCHAR:
			case Types.CHAR:
				if (jdbcObject != null) {
					value = jdbcObject.toString();
				} else {
					value = "NULL";
				}
			break;
			case Types.BINARY:
				value = new String((byte[]) jdbcObject);
				break;
			default:
				value = "";
		}
		if (value == null)
		{
			value = "";
		}
		return value;
	}


	public Object unboxJDBCObject(final Object jdbcObject, final int colType) throws SQLException {
		if (colType==Types.CHAR && jdbcObject!=null) {
			return StringUtils.stripEnd((String)jdbcObject," ");
		}
		return jdbcObject;
	}

	protected String read(Clob c) throws SQLException, IOException
	{
		StringBuffer sb = new StringBuffer( (int) c.length());
		Reader r = c.getCharacterStream();
		char[] cbuf = new char[2048];
		int n = 0;
		while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
			if (n > 0) {
				sb.append(cbuf, 0, n);
			}
		}
		return sb.toString();
	}


	public void setData(PreparedStatement pstmt, int position, Object data,
			int colType, int size, int precision) throws SQLException {
		if (colType==Types.NVARCHAR) {
			pstmt.setString(position, (String)data);
		} else {
			pstmt.setObject(position, data, colType);
		}
	}

	
	public boolean displaysWarnings() {
		return IJDBCDataFormatter.DEFAULT_DISPLAYS_WARNING;
	}

	
	public int getFetchSize() {
		return IJDBCDataFormatter.DEFAULT_FETCH_SIZE;
	}

	public String getColumnFormat(int colType, int precision, int scale) {
		String stringFormat = null;
		switch (colType)
		{

			case Types.DATE:
				stringFormat = formatter.getDateFormat();
			break;
			case Types.TIME:
			break;
			case Types.TIMESTAMP:
				stringFormat = formatter.getTimestampFormat();
			break;
		}
		return stringFormat;
	}

}
