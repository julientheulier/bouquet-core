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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.model.Database;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;

/**
 * The LazySchema knows how to populate itself...
 * 
 * @author sfantino
 *
 */
public class LazySchema implements Schema {

	private Database db = null;
	private String catalog = null;
	private String name = "";
	private boolean system = false;
	
	private ArrayList<Table> tables = null;
	
	private LazyDatabaseFactory df;

	private int table_state = 0;

	protected LazySchema(LazyDatabaseFactory df) {
		this.df = df;
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

	@Override
	public Database getDatabase() {
		return db;
	}

	@Override
	public void setDatabase(Database db) {
		if (this.db!=null) {
			this.db.getSchemas().remove(this);
		}
		this.db = db;
		this.db.getSchemas().add(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCatalog() {
		return catalog;
	}

	@Override
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	@Override
	public List<Table> getTables() throws ExecutionException {
		if (table_state==1) {
			return getTables_internal();
		} else {
			synchronized (this) {
				if (table_state==0) {
					table_state = 2;
					try {
						this.df.getEngine().populateSchema(this);
					} catch (DatabaseServiceException e) {
						table_state=0;// reset to initial state so we can retry ?
						throw e;
					}
					table_state = 1;
				} else {
					// already done...
				}
				return getTables_internal();
			}
		}
	}

	private List<Table> getTables_internal() {
		if (tables==null) {
			initTables();
		}
		return tables;
	}
	
	public void addTable(Table table) {
		getTables_internal().add(table);
	}
	
	@Override
	public boolean removeTable(Table table) {
		return getTables_internal().remove(table);
	}
	
	protected void initTables() {
		this.tables = new ArrayList<Table>();
	}

	/**
	 * return the schema who's name is name or null if not found
	 * @param name
	 * @return
	 * @throws DatabaseServiceException 
	 */
	@Override
	public Table findTable(String name) throws ExecutionException {
		if (name==null) name="";
		for (Table table : getTables()) {
			if (table.getName().equals(name)) {
				return table;
			}
		}
		// not found
		return null;
	}

	@Override
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
		result = prime * result + ((db == null) ? 0 : db.hashCode());
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
		LazySchema other = (LazySchema) obj;
		if (catalog == null) {
			if (other.catalog != null)
				return false;
		} else if (!catalog.equals(other.catalog))
			return false;
		if (db == null) {
			if (other.db != null)
				return false;
		} else if (!db.equals(other.db))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
 
}
