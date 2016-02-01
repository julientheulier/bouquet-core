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
package com.squid.core.database.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.model.DatabaseFactory;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;

/**
 * specific vendor metadata support extension we can inject into MetadataEngine
 * @author sergefantino
 *
 */
public interface VendorMetadataSupport {


	 public ResultSet getIndexInfo(Connection conn, String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException;

	 public ResultSet getPrimaryKeys(Connection conn, String catalog, String schema, String table) throws SQLException;

	 //T128: Metadata per DB plugin 
	 public List<ColumnData> getColumns(Connection conn, String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException;
	 
	 public List<Schema> getSchemas(DatabaseFactory df, Connection conn) throws ExecutionException; 
	 
	 /**
	  * identify vendor specific system schemas
	  * @param name
	  * @return
	  */
	 public boolean isSystemSchema(String name);
	 
	 public List<Schema> getCatalogs(DatabaseFactory df, Connection conn) throws ExecutionException; 
	 public List<Table> getTables(DatabaseFactory df, Connection conn, String catalog, String name, String tableName) throws ExecutionException;
	 public ResultSet getImportedKeys(Connection conn, String catalog, String name, String name2) throws ExecutionException; //should be List<ForeignKeyData>

	 /**
	  * allow specific vendor driver to cure JDBC data
	  * @param data
	  */
	public ColumnData normalizeColumnData(ColumnData data);

    public boolean handleSurrogateCharacters();

	int[] normalizeColumnType(ResultSet rs) throws SQLException;



}
