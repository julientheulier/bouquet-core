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
import com.squid.core.database.metadata.VendorMetadataSupport;
import com.squid.core.database.model.DatabaseProduct;
import com.squid.core.database.statistics.IDatabaseStatistics;
import com.squid.core.jdbc.formatter.DataFormatter;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;

/**
 * Specific vendor support
 * @author sergefantino
 *
 */
public interface IVendorSupport {
	
	/**
	 * return the unique vendor identifier
	 * @return
	 */
	public String getVendorId();
	
	/**
	 * return a template to build jdbc url suitable for this driver
	 * @return
	 */
	public JdbcUrlTemplate getJdbcUrlTemplate();
	
	/**
	 * build a valid JDBC url given a map of arguments
	 * @param parameters. The arguments are expected to correspond to the parameters provides by the JdbcUrlTemplate.
	 * @return
	 */
	public String buildJdbcUrl(Map<String, String> arguments) throws IllegalArgumentException;


	/**
	 * return the version of the plugin (git commit)
	 * @return
	 */
	public String getVendorVersion();

	/**
         * return true if the database product is supported
         * @param product
         * @return
         */
	public boolean isSupported(DatabaseProduct product);
	
	/**
	 * instanciate a new data formatter
	 * @param prefs
	 * @param connection
	 * @return
	 */
	public IJDBCDataFormatter createFormatter(DataFormatter prefs, Connection connection);
	
	
	/**
	 * instanciate a database statistics engine
	 * @param def
	 * @return
	 */
	public IDatabaseStatistics createDatabaseStatistics(DataSourceReliable ds);
	
	/**
	 * get singleton implementation of vendor specific metadata support - or null if plain JDBC
	 * @return
	 */
	public VendorMetadataSupport getVendorMetadataSupport();

}
