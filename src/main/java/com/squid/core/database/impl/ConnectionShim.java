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
package com.squid.core.database.impl;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author sergefantino
 *
 */
public class ConnectionShim implements Connection {
	
	private Connection proxy = null;

	/**
	 * 
	 */
	public ConnectionShim(Connection proxy) {
		this.proxy = proxy;
	}
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return proxy.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return proxy.isWrapperFor(iface);
	}

	public Statement createStatement() throws SQLException {
		return proxy.createStatement();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return proxy.prepareStatement(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return proxy.prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		return proxy.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		try {
			proxy.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			// ignore
		}
	}

	public boolean getAutoCommit() throws SQLException {
		return proxy.getAutoCommit();
	}

	public void commit() throws SQLException {
		proxy.commit();
	}

	public void rollback() throws SQLException {
		proxy.rollback();
	}

	public void close() throws SQLException {
		proxy.close();
	}

	public boolean isClosed() throws SQLException {
		return proxy.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return proxy.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		try {
			proxy.setReadOnly(readOnly);
		} catch (SQLException e) {
			// ignore
		}
	}

	public boolean isReadOnly() throws SQLException {
		return proxy.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		proxy.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return proxy.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		proxy.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return proxy.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return proxy.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		proxy.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return proxy.createStatement(resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return proxy.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return proxy.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return proxy.getTypeMap();
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		proxy.setTypeMap(map);
	}

	public void setHoldability(int holdability) throws SQLException {
		proxy.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return proxy.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return proxy.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return proxy.setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		proxy.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		proxy.releaseSavepoint(savepoint);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return proxy.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return proxy.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return proxy.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return proxy.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return proxy.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return proxy.prepareStatement(sql, columnNames);
	}

	public Clob createClob() throws SQLException {
		return proxy.createClob();
	}

	public Blob createBlob() throws SQLException {
		return proxy.createBlob();
	}

	public NClob createNClob() throws SQLException {
		return proxy.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return proxy.createSQLXML();
	}

	public boolean isValid(int timeout) throws SQLException {
		return proxy.isValid(timeout);
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		proxy.setClientInfo(name, value);
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		proxy.setClientInfo(properties);
	}

	public String getClientInfo(String name) throws SQLException {
		return proxy.getClientInfo(name);
	}

	public Properties getClientInfo() throws SQLException {
		return proxy.getClientInfo();
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return proxy.createArrayOf(typeName, elements);
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return proxy.createStruct(typeName, attributes);
	}

	public void setSchema(String schema) throws SQLException {
		proxy.setSchema(schema);
	}

	public String getSchema() throws SQLException {
		return proxy.getSchema();
	}

	public void abort(Executor executor) throws SQLException {
		proxy.abort(executor);
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		proxy.setNetworkTimeout(executor, milliseconds);
	}

	public int getNetworkTimeout() throws SQLException {
		return proxy.getNetworkTimeout();
	}

}
