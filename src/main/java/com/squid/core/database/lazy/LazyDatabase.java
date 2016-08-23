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

import java.io.IOException; 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.metadata.MetadataEngine;
import com.squid.core.database.model.Column;
import com.squid.core.database.model.Database;
import com.squid.core.database.model.DatabaseFactory;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;
import com.squid.core.database.model.impl.DatabaseProductImpl;
import com.squid.core.sql.DDLStatementUtils;
import com.squid.core.sql.render.RenderingException;

/**
 * This is the lazy implementation for the Database
 *
 */
public class LazyDatabase extends DatabaseProductImpl implements Database {
	
	private String url;
	private String name = "";
	
	private LazyDatabaseFactory  df;
	private boolean stale = false;
	
	private ArrayList<Schema> schemas = new ArrayList<Schema>();

	protected LazyDatabase(LazyDatabaseFactory df) throws ExecutionException {
		this.df = df;
		// actually we do it right in the creator in order to make sure that the connection is valid...
		this.df.getEngine().populateDatabase(this);
		this.df.getEngine().populateSchemas(this);
	}
	
	@Override
	public DatabaseFactory getFactory() {
		return df;
	}
	
	public boolean isStale() {
		return stale;
	}

	public void setStale() {
		this.stale = true;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Schema> getSchemas() {
		return schemas;
	}
	
	@Override
	public Schema addSchema(String name) {
		synchronized (this) {
			Schema schema = findSchema(name);
			if (schema==null) {
				schema = df.createSchema();
				schema.setName(name);
				schema.setDatabase(this);
				this.schemas.add(schema);
			}
			return schema;
		}
	}

	/**
	 * return the schema who's name is name or null if not found
	 * @param name
	 * @return
	 */
	public Schema findSchema(String name) {
		if (name==null) name="";
		for (Schema schema : getSchemas()) {
			if (schema.getName().equals(name)) {
				return schema;
			}
		}
		// not found
		return null;
	}
	
	/**
	 * look for a table by its name and schema holder
	 * @param schemaName
	 * @param tableName
	 * @return
	 * @throws DatabaseServiceException 
	 */
	public Table findTable(String schemaName, String tableName) throws ExecutionException {
		Schema schema = findSchema(schemaName);
		if (schema==null) return null;
		return schema.findTable(tableName);
	}

	/**
	 * add for a table by its name and schema holder
	 * @param schemaName
	 * @param tableName
	 * @param data
	 * @return
	 * @throws DatabaseServiceException
	 */
	public Table addTable(String schemaName, String tableName, List<Column> columns) throws ExecutionException {

		Schema schema = findSchema(schemaName);
		if (schema==null) return null;

		Table table = this.df.createTable();
		table.setName(tableName);
		table.setSchema(schema);

		table.toString();
		DDLStatementUtils util = new DDLStatementUtils();

		//createTableStatement.
		try{
			util.genCode(this, table);
		}catch(IOException e){

		}catch(RenderingException e){

		}
		if(columns != null){
			for(Column col : columns) {
				table.addColumn(col);
			}
		}

		schema.addTable(table);
		return schema.findTable(tableName);
	}

	@Override
	public MetadataEngine getEngine() {
		return df.getEngine();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		LazyDatabase other = (LazyDatabase) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
