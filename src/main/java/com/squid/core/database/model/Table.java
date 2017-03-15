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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.domain.IDomain;

/**
 * A Table object; it contains a list of Columns; and it is owned by a single Schema.
 * @author sfantino
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public interface Table 
extends LiveObject
{

	public String getName();

	public void setName(String name);
	
	public TableType getType();
	
	public void setType(TableType type);
	
	public Object getVendorType();
	
	public void setVendorType(Object type);
	
	/**
	 * return the generic domain associated with the table
	 * @return
	 */
	@JsonIgnore
	public IDomain getDomain();

	public String getCatalog();

	public void setCatalog(String catalog);

	public String getDescription();

	public void setDescription(String description);

	@JsonBackReference("schema")
	public Schema getSchema();

	public void setSchema(Schema schema);
	
	// columns
	
	@JsonManagedReference("table")
	public List<Column> getColumns() throws ExecutionException;
	
	public void addColumn(Column column);

	/**
	 * look for a column with the given name; return null if cannot find
	 * @param name
	 * @return the column or null
	 * @throws ExecutionException if an error occurs while interacting with the database
	 */
	public Column findColumnByName(String name) throws ExecutionException;
	
	// foreign keys
	
	@JsonManagedReference("primary")
	public List<ForeignKey> getForeignKeys() throws ExecutionException;

	/**
	 * return the foreignKey with name, or null if it is not defined
	 * @param name
	 * @return
	 * @throws DatabaseServiceException 
	 */
	public ForeignKey findForeignKeyByName(String name) throws ExecutionException;
	
	public ForeignKey addForeignKey(ForeignKey fk);
	
	// indexes
	/*
	public List<Index> getIndexes();

	public Index findIndexByName(String name);
	
	public Index addIndex(Index index);
	*/
	
	public Index getPrimaryKey();
	
	public void setPrimaryKey(Index pk);

	public void refresh();

}
