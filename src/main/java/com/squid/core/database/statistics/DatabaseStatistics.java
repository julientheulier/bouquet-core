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
package com.squid.core.database.statistics;

import java.sql.Connection; 
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.impl.DataSourceReliable;
import com.squid.core.database.model.Column;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;

public abstract class DatabaseStatistics implements IDatabaseStatistics {

	private DataSourceReliable ds;

	private HashMap<Column, ColumnStatistics> columns = new HashMap<Column, ColumnStatistics>();
	private HashMap<Table, ObjectStatistics> tables = new HashMap<Table, ObjectStatistics>();

	public DatabaseStatistics(DataSourceReliable ds) {
		this.ds = ds;
	}

	protected abstract void computeTablesStatistics(Schema schema, Connection connection) throws ExecutionException;

	protected abstract void computeColumnsStatistics(Table table, Connection connection) throws ExecutionException;


	@Override
    public ColumnStatistics getStatistics(Column column) throws ExecutionException {
	    ColumnStatistics c = columns.get(column);
		if (c==null) {
			synchronized (this) {
				c = columns.get(column);
				if (c==null) {
					try {
						Connection connection = null;
						try {
							connection = this.ds.getConnectionBlocking();
							computeColumnsStatistics(column.getTable(),connection);
							c = columns.get(column);
							if (c==null) {
							    columns.put(column, new ColumnStatistics());// if not known, avoid to recompute
							}
						} finally {
							if (connection!=null){
								connection.close();
								ds.releaseSemaphore();
							}

						}
					} catch (SQLException e) {
						return null;
					}
				}
			}
			return c;
		} else {
			return c;
		}
	}

	@Override
    public float getSize(Column column) throws ExecutionException {
		ObjectStatistics c = getStatistics(column);
		return c!=null?c.getSize():-1;
	}

	@Override
    public ObjectStatistics getStatistics(Table table) throws ExecutionException, SQLException {
		Connection connection = null;
		try {
			connection = this.ds.getConnectionBlocking();
			return getStatistics(table, connection);
		} finally {
			try {
				if (connection!=null) {
					connection.close();
					ds.releaseSemaphore();
				}
				} catch (SQLException e) {
				return null;
			}
		}
	}

	@Override
    public float getSize(Table table) throws ExecutionException, SQLException {
		ObjectStatistics c = getStatistics(table);
		return c!=null?c.getSize():-1;
	}

	protected ObjectStatistics getStatistics(Table table, Connection connection) throws ExecutionException, SQLException {
		ObjectStatistics c = tables.get(table);
		if (c==null) {
			synchronized (this) {
				c = tables.get(table);
				if (c==null) {
					try {
						connection = this.ds.getConnectionBlocking();
						computeTablesStatistics(table.getSchema(),connection);
						c = tables.get(table);
					} finally {
						try {
							if (connection!=null) {
								connection.close();
								ds.releaseSemaphore();
							}
							} catch (SQLException e) {
							// ignore
						}
					}
				}
			}
			return c;
		} else {
			return c;
		}
	}

	protected void putStatistics(Table table, ObjectStatistics stats) {
		tables.put(table, stats);
	}

	protected void putStatistics(Column column, ColumnStatistics stats) {
		columns.put(column, stats);
	}
	
	@Override
	public boolean isPartitionTable(Table table) {
	    return false;
	}
	
	@Override
	public PartitionInfo getPartitionInfo(Table table) {
	    return null;
	}

}
