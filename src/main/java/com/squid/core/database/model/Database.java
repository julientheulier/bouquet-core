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
package com.squid.core.database.model;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.metadata.MetadataEngine;

/**
 * the Database object is the top-level object associated to a JDBC connection. It contains a list of Schema.
 *
 */
public interface Database 
extends DatabaseProduct, LiveObject
{
	
	public DatabaseFactory getFactory();
	
	/**
	 * invalidate the whole database
	 */
	public void setStale();
	
	public String getUrl();

	public void setUrl(String url);

	public String getName();

	public void setName(String name);

	@JsonManagedReference("database")
	public List<Schema> getSchemas();
	
	public Schema addSchema(String name);

	/**
	 * return the schema who's name is name or null if not found
	 * @param name
	 * @return
	 */
	public Schema findSchema(String name);
	
	/**
	 * look for a table by its name and schema holder
	 * @param schemaName
	 * @param tableName
	 * @return
	 * @throws DatabaseServiceException 
	 */
	public Table findTable(String schemaName, String tableName) throws ExecutionException;


	/**
	 * add a table by its name and schema holder
	 * @param schemaName
	 * @param tableName
	 * @param data
	 * @return
	 * @throws DatabaseServiceException
	 */
	public Table addTable(String schemaName, String tableName, List<Column> columns) throws ExecutionException;

	public MetadataEngine getEngine();

}
