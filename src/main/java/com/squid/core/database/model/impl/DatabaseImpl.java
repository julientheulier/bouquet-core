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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.metadata.MetadataEngine;
import com.squid.core.database.model.*;

public class DatabaseImpl extends DatabaseProductImpl implements Database {

    private String url;
    private String name = "";
    private boolean stale = false;
    private ArrayList<Schema> schemas = new ArrayList<Schema>();
    
    private DatabaseFactory factory = null;
    
    public DatabaseImpl(DatabaseFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public DatabaseFactory getFactory() {
		return factory;
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
                schema = factory.createSchema();
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
        Table table = this.factory.createTable();
        for(Column col : columns) {
            table.addColumn(col);
        }

        return schema.findTable(tableName);
    }

    public Table insertTable(String schemaName, String tableName, List<Column> columns) throws ExecutionException {
        Schema schema = findSchema(schemaName);
        if (schema==null) return null;
        Table table = this.factory.createTable();
        for(Column col : columns) {
            table.addColumn(col);
        }

        return schema.findTable(tableName);
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
        Database other = (Database) obj;
        if (name == null) {
            if (other.getName() != null)
                return false;
        } else if (!name.equals(other.getName()))
            return false;
        if (url == null) {
            if (other.getUrl() != null)
                return false;
        } else if (!url.equals(other.getUrl()))
            return false;
        return true;
    }

    @Override
    public MetadataEngine getEngine() {
    	return factory.getEngine();
    }

}
