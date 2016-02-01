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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.model.Database;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;

/**
 * A Schema is the one way to organize Tables in the Database.
 *
 */
public class SchemaImpl implements Schema {
	
	private Database db = null;
	private String catalog = null;
	private String name = "";
	private boolean system = false;
	
	private ArrayList<Table> tables = null;
	
	public SchemaImpl() {
	}
	
	@Override
	public boolean isSystem() {
		return system;
	}
	
	@Override
	public void setSystem(boolean system) {
		this.system = system;
	}
	
	@Override
	public boolean isStale() {
		return db!=null?db.isStale():true;
	}

	public Database getDatabase() {
		return db;
	}

	public void setDatabase(Database db) {
		if (this.db!=null) {
			this.db.getSchemas().remove(this);
		}
		this.db = db;
		this.db.getSchemas().add(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	// need a lock to avoid using getTables() results while populating it
	private transient boolean gateKeeper = false;
	
	public List<Table> getTables() throws DatabaseServiceException {
		if (tables==null) {
			initTables();
		}
		if (gateKeeper) {
			synchronized (this) {
				// just wait
			}
		}
		return tables;
	}
	
	@Override
	public void addTable(Table table) throws DatabaseServiceException {
		getTables().add(table);
	}

	@Override
	public boolean removeTable(Table table) throws DatabaseServiceException {
		return getTables().remove(table);
	}
	
	private void initTables() throws DatabaseServiceException {
		synchronized (this) {
			if (this.tables==null) {
				this.gateKeeper = true;// make sure no-one can use the list before it is properly populated
				this.tables = new ArrayList<Table>();
				populateSchema();
				this.gateKeeper = false;// ok, release it
			}
		}
	}

	/**
	 * override to populate the schema tables. The method call is guaranteed to be synchronized and performed once.
	 * @throws DatabaseServiceException 
	 * @throws SQLException 
	 */
	protected void populateSchema() throws DatabaseServiceException {
	}

	/**
	 * return the schema who's name is name or null if not found
	 * @param name
	 * @return
	 * @throws DatabaseServiceException 
	 */
	public Table findTable(String name) throws DatabaseServiceException {
		if (name==null) name="";
		for (Table table : getTables()) {
			if (table.getName().equals(name)) {
				return table;
			}
		}
		// not found
		return null;
	}

	public boolean isNullSchema() {
		return this.name=="";
	}

	@Override
	public String toString() {
		return "Schema [db=" + db + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
		//result = prime * result + ((db == null) ? 0 : db.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchemaImpl other = (SchemaImpl) obj;
		if (catalog == null) {
			if (other.catalog != null)
				return false;
		} else if (!catalog.equals(other.catalog))
			return false;
		/*
		if (db == null) {
			if (other.db != null)
				return false;
		} else if (!db.equals(other.db))
			return false;
			*/
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
