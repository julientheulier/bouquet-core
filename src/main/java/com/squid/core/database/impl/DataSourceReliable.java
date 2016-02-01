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
package com.squid.core.database.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.squid.core.sql.render.ISkinFeatureSupport;

public interface DataSourceReliable extends DataSource {


	 void releaseSemaphore();

	/**
	   * <p>Attempts to establish a connection with the data source that
	   * this {@code DataSource} object represents.
	   *
	   * @return  a connection to the data source
	   * @exception SQLException if a database access error occurs
	   * @throws java.sql.SQLTimeoutException  when the driver has determined that the
	   * timeout value specified by the {@code setLoginTimeout} method
	   * has been exceeded and has at least tried to cancel the
	   * current database connection attempt
	   */
	  Connection getConnectionBlocking() throws SQLException;
	  
	  
	  /**
	   * <p>List all useful features that can be supported or not by the data source that
	   * this {@code DataSource} object represents.
	   * 
	   */
	  public interface FeatureSupport extends ISkinFeatureSupport {
			public String TEMPROLLUP = "temprollup";
			public String ROLLUP = "rollup";
			public String READONLY = "readonly";
			public String AUTOCOMMIT = "autocommit";
			public String ROLLBACK = "rollback";
			public String SELECT = "SelectStatement";
			public String GROUPBY_ALIAS = SELECT+".GROUPBY.ALIAS";// can we use alias in the group-by section?
			public String ORDERED_ANALYTICAL_FUNCTIONS = SELECT+".ORDERED.ANALYTICAL.FUNCTIONS";// can we use ordered analytical functions?
			
		}
	  
	  public void close();

}
