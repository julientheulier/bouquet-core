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

import java.util.concurrent.ExecutionException;

import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.metadata.MetadataEngine;
import com.squid.core.database.model.impl.DatabaseManager;


/**
 * A simple factory to create Database/Schema/Table/Column object. It can create some sub-classes implementing a specific behavior (for example lazy objects linked to a JDBC connection to populate the Database as needed).
 * 
 * @author sfantino
 *
 */
public interface DatabaseFactory {
	
	public DatabaseManager getManager();
	
	public MetadataEngine getEngine();

	/**
	 */
	Column createColumn();

	/**
	 * @throws DatabaseServiceException 
	 */
	Database createDatabase() throws ExecutionException;

	/**
	 */
	Schema createSchema();

	/**
	 */
	Table createTable();
	
	/**
	 */
	ForeignKey createForeignKey();

} //DatabaseFactory
