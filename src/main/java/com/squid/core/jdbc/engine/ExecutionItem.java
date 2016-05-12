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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.squid.core.database.impl.DataSourceReliable;
import com.squid.core.database.model.Database;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;

import javax.sql.DataSource;


public class ExecutionItem implements IExecutionItem {

	private Database database;
	private DataSource datasource;
	private Connection connection;
	private ResultSet resultset;
	private IJDBCDataFormatter formatter = null;
	private int id = 0;
	private Date executionDate = new Date();// default is now
	
	public ExecutionItem(Database database, DataSource datasource, Connection connection, ResultSet resultset, IJDBCDataFormatter formatter, int id) {
		super();
		this.database = database;
		this.datasource = datasource;
		this.connection = connection;// this is the actual DS connection
		this.resultset = resultset;
		this.formatter = formatter;
		this.id = id;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public Database getDatabase() {
		return this.database;
	}

	public DataSource getDataSource() {
		return this.datasource;
	}


	public ResultSet getResultSet() {
		return resultset;
	}

	public IJDBCDataFormatter getDataFormatter() {
		return formatter;
	}
	
	@Override
	public Date getExecutionDate() {
	    return executionDate;
	}
	
	public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }
	
	@Override
	public void close() throws SQLException {
		try {
			if (resultset!=null) {
				Statement stat = resultset.getStatement();
				try {
					resultset.close();
					stat.close();
				} catch (SQLException e) {
					stat.close();
				}
			}
			if (connection!=null && !connection.isClosed()) {
				if (!connection.getAutoCommit()) {// ticket:2922
					connection.commit();
                }
				// return the connection to the pool
				//
				connection.close();
				((DataSourceReliable) datasource).releaseSemaphore();
			}
		} finally {
			resultset = null;
			connection = null;
		}
	}

}
