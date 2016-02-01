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
package com.squid.core.database.lazy;

import java.util.concurrent.ExecutionException;

import com.squid.core.database.metadata.MetadataEngine;
import com.squid.core.database.model.Column;
import com.squid.core.database.model.Database;
import com.squid.core.database.model.DatabaseFactory;
import com.squid.core.database.model.ForeignKey;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;
import com.squid.core.database.model.impl.DatabaseManager;

/**
 * This is a special factory to implement lazy behavior. The factory must be created for a given DataSource object, and will then populate database objects according to that DS.
 *
 */
public class LazyDatabaseFactory implements DatabaseFactory {

	private DatabaseManager ds;
	private MetadataEngine engine;

	public LazyDatabaseFactory(DatabaseManager ds) {
		//throws DatabaseServiceException {
		this.ds = ds;
		this.engine = new MetadataEngine(this);
	}

	@Override
	public DatabaseManager getManager() {
		return ds;
	}

	@Override
	public MetadataEngine getEngine() {
		return engine;
	}

	@Override
	public Database createDatabase() throws ExecutionException {
		return new LazyDatabase(this);
	}

	@Override
	public Schema createSchema() {
		return new LazySchema(this);
	}

	@Override
	public Table createTable() {
		return new LazyTable(this);
	}
	
	@Override
	public Column createColumn() {
		return new Column();
	}
	
	@Override
	public ForeignKey createForeignKey() {
		return new ForeignKey();
	}

}
