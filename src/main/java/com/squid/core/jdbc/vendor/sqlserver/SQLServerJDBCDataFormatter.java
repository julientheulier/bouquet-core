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
package com.squid.core.jdbc.vendor.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;

import com.squid.core.jdbc.formatter.DataFormatter;
import com.squid.core.jdbc.formatter.DefaultJDBCDataFormatter;


public class SQLServerJDBCDataFormatter
extends DefaultJDBCDataFormatter {

	public SQLServerJDBCDataFormatter(DataFormatter formatter, Connection connection) {
		super(formatter, connection);
	}

	@Override
	public Object unboxJDBCObject(final Object column, final int colType) throws SQLException {
		switch (colType) {
		case 1:// CHAR
			if (column!=null) {
				return StringUtils.stripEnd((String)column," ");
			} else {
				return null;
			}
		case -9 : //NVARCHAR
		case -15 : //NCHAR
			if (column==null) {
				return null;
			} else {
				return column.toString();
			}
		case Types.BIT :
			if (column instanceof Boolean && column!=null) {
				if (((Boolean) column).booleanValue()==true) {
					return 1;
				} else {
					return 0;
				}
			}
			if (column!=null) {
				return column.toString();
			} else {
				return null;
			}
		}
		return column;
	}

	@Override
	public String formatJDBCObject(final Object column, final int colType) throws SQLException {
		if (column== null) {
			return "";
		}
		switch (colType) {
		case -9 : //NVARCHAR
		case -15 : //NCHAR
		case Types.BIT :
			return super.formatJDBCObject(this.unboxJDBCObject(column, colType), java.sql.Types.VARCHAR);
		}
		return super.formatJDBCObject(column,colType);
	}

	@Override
	public void setData(PreparedStatement pstmt, int position, Object data,
			int colType, int size, int precision) throws SQLException, NullPointerException {
		// TODO Auto-generated method stub
		if (colType==Types.DATE || colType==Types.TIMESTAMP || colType==Types.TIME) {
			if (data!=null) {
				pstmt.setDate(position, new java.sql.Date(((java.util.Date)data).getTime()));
			} else {
				pstmt.setDate(position, null);
			}
		} else {
			super.setData(pstmt, position, data, colType, size, precision);
		}
	}
}
