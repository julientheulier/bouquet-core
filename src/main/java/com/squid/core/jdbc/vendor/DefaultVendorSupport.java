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
package com.squid.core.jdbc.vendor;

import java.sql.Connection;
import java.util.Map;

import com.squid.core.database.impl.DataSourceReliable;
import com.squid.core.database.metadata.GenericMetadataSupport;
import com.squid.core.database.metadata.VendorMetadataSupport;
import com.squid.core.database.model.DatabaseProduct;
import com.squid.core.database.statistics.IDatabaseStatistics;
import com.squid.core.database.statistics.UnsupportedDatabaseStatistics;
import com.squid.core.jdbc.formatter.DataFormatter;
import com.squid.core.jdbc.formatter.DefaultJDBCDataFormatter;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;

public class DefaultVendorSupport implements IVendorSupport {
	
	private static final VendorMetadataSupport GENERIC = new GenericMetadataSupport();
	
	public static final String VENDOR_ID = "GENERIC";

	@Override
	public String getVendorId() {
		return VENDOR_ID;
	}

	@Override
	public String getVendorVersion() {
		return "Unknown";
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.jdbc.vendor.IVendorSupport#getJdbcUrlTemplate()
	 */
	@Override
	public JdbcUrlTemplate getJdbcUrlTemplate() {
		JdbcUrlTemplate template = new JdbcUrlTemplate(getVendorId(), "jdbc:"+getVendorId().toLowerCase()+"://[hostname]:{port}/{database}");
		template.add(new JdbcUrlParameter("hostname", false));
		template.add(new JdbcUrlParameter("port", true));
		template.add(new JdbcUrlParameter("database", true));
		return template;
	}
	
	/**
	 * this is the default implementation. Override to take into account driver peculiarities.
	 */
	@Override
	public String buildJdbcUrl(Map<String, String> arguments) throws IllegalArgumentException {
		String url = "jdbc:"+getVendorId().toLowerCase()+"://";
		String hostname = arguments.get("hostname");
		if (hostname==null) throw new IllegalArgumentException("cannot build JDBC url, missing mandatory argument 'hostname'");
		url += hostname;
		String port = arguments.get("port");
		if (port!=null && !port.equals("")) {
			// check it's an integer
			try {
				int p = Integer.valueOf(port);
				url += ":"+Math.abs(p);// just in case
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("cannot build JDBC url, 'port' value must be a valid port number");
			}
		}
		String database = arguments.get("database");
		if (database!=null) {
			url += "/" + database;
		}
		// validate ?
		return url;
	}

	@Override
	public boolean isSupported(DatabaseProduct product) {
		return false;
	}

	@Override
	public IJDBCDataFormatter createFormatter(DataFormatter prefs,
			Connection connection) {
		return new DefaultJDBCDataFormatter(prefs, connection);
	}
	
	@Override
	public IDatabaseStatistics createDatabaseStatistics(DataSourceReliable ds) {
		return new UnsupportedDatabaseStatistics(ds);
	}
	
	@Override
	public VendorMetadataSupport getVendorMetadataSupport() {
		return GENERIC;
	}

}
