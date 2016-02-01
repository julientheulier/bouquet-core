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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.squid.core.database.impl.DatabaseServiceException;

/**
 * A Schema is the one way to organize Tables in the Database.
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public interface Schema
extends LiveObject
{

	@JsonBackReference("database")
	public Database getDatabase();

	public void setDatabase(Database db);

	public String getName();

	public void setName(String name);

	public String getCatalog();

	public void setCatalog(String catalog);
	
	/**
	 * true if this is a system schema
	 * @return
	 */
	public boolean isSystem();

	public void setSystem(boolean system);

	@JsonManagedReference("schema")
	public List<Table> getTables() throws ExecutionException;
	
	public void addTable(Table table) throws ExecutionException;
	
	public boolean removeTable(Table table) throws ExecutionException;

	/**
	 * return the schema who's name is name or null if not found
	 * @param name
	 * @return
	 * @throws DatabaseServiceException 
	 */
	public Table findTable(String name) throws ExecutionException;

	public boolean isNullSchema();

}
