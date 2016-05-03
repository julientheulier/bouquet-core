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
package com.squid.core.database.model.impl;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicInteger;

import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.database.impl.DataSourceReliable;
import com.squid.core.database.model.Database;
import com.squid.core.database.statistics.IDatabaseStatistics;
import com.squid.core.jdbc.formatter.DataFormatter;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;
import com.squid.core.jdbc.vendor.IVendorSupport;
import com.squid.core.sql.render.SQLSkin;

/**
 * responsible for database interaction and accompanying objects
 * @author sergefantino
 *
 */
public class DatabaseManager {
    
    static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    protected JDBCConfig config; 

    protected DataSourceReliable ds = null;
    protected Database db = null;
    protected Database inMemoryDB = null;
    protected IVendorSupport vendor = null;	
    protected IDatabaseStatistics stats = null;
	protected SQLSkin skin = null;
	protected Datastore perfdb = null;
	

	public static final AtomicInteger queryCnt = new AtomicInteger();

	public DatabaseManager() {
		super();
	}
	
	public DataSourceReliable getDatasource() {
		return this.ds;
	}
	
	public void close() {
		if (ds!=null) {
			ds.close();
		}
	}

	public Database getDatabase() {
		return db;
	}
	
	public IDatabaseStatistics getStatistics() {
		return stats;
	}
	
	
	public IJDBCDataFormatter getDataFormatter(Connection connection) {
		return this.getDataFormatter(new DataFormatter(), connection);
	}

	public IJDBCDataFormatter getDataFormatter(DataFormatter formatter, Connection connection){
		return this.vendor.createFormatter(formatter, connection);
	};

	public SQLSkin getSkin() {
		if (db!=null) {
			// safe is to use the database skin since we are sure it has been correctly initialized
			return db.getSkin();
		} else {
			// don't know the database yet, just return the default one
			throw new RuntimeException("Internal error, cannot request the SQLSkin before initializing the database");
		}
	}

	
	public JDBCConfig getConfig(){
		return this.config;
	}
}
