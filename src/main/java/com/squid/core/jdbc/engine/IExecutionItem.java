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
package com.squid.core.jdbc.engine;

import java.sql.ResultSet;
import java.util.Date;

import com.squid.core.database.model.Database;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;

import javax.sql.DataSource;


/**
 * legacy interface: now it is just a wrapper around the ResultSet
 *  
 */
public interface IExecutionItem {
	
	/**
	 * get the source database
	 */
	Database getDatabase();

	DataSource getDataSource();

	/**
	 * Get result
	 * @return
	 */
	ResultSet getResultSet();
    
    /**
     * Close this object
     */
    void close();

    /**
     * get the data formatter associated with the connection
     * @return
     */
	IJDBCDataFormatter getDataFormatter();

	/**
	 * get internal ID that can identify the query
	 * @return
	 */
	int getID();
	
	/**
	 * get the date when we executed the query (start date)
	 * @return
	 */
	Date getExecutionDate();
}
