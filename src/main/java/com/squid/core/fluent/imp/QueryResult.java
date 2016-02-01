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
package com.squid.core.fluent.imp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.database.model.impl.DatabaseManager;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.render.ISelectPiece;

public class QueryResult {

	static final Logger logger = LoggerFactory.getLogger(QueryResult.class);
	
    /**
     * define the binding for an expression in the resultset
     * @author sergefantino
     *
     */
    public class Binding {

        public int index;
        public int type;// SQL type

        public Binding(int index, int type) {
            super();
            this.index = index;
            this.type = type;
        }

        public int getIndex() {
            return index;
        }

        public int getType() {
            return type;
        }

    }

    /**
     * inner class to read the resultset
     * @author sergefantino
     *
     */
    public class Record {

        /**
         * read the value bound to the given expression
         * @param expr
         * @return
         * @throws SQLScopeException if the expression is not bound
         * @throws SQLException
         */
        public Object read(ExpressionAST expr) throws SQLScopeException, SQLException {
            Binding binding = getBinding(expr);
            if (binding==null) {
                throw new SQLScopeException("undefined expression "+expr);
            }
            Object value = result.getObject(binding.index);
            Object unbox = formatter.unboxJDBCObject(value, binding.type);
            if(logger.isDebugEnabled()){logger.debug(("read(ExpressionAST expr): "+unbox));}
            return unbox;
        }

        /**
         * go to next record
         * @return
         * @throws SQLException
         */
        public boolean next() throws SQLException {
            return result.next();
        }

        /**
         * return the expression binding == (index in resultset,SQL type)
         * @param expr
         * @return the binding or null if the expression is not bound
         * @throws SQLScopeException
         * @throws SQLException
         */
        public Binding getBinding(ExpressionAST expr) throws SQLException {
            Binding binding = cache.get(expr);
            if (binding==null) {
                binding = evalBinding(expr);
                if (binding!=null) {
                    cache.put(expr, binding);
                }
            }
            return binding;
        }

    }

    private Connection connection;
    private SelectImpl select;
    private ResultSet result;
    private IJDBCDataFormatter formatter;
    private ResultSetMetaData metadata;

    // even though the Record object is stateless, we better share the cache
    private HashMap<ExpressionAST, Binding> cache = new HashMap<ExpressionAST, QueryResult.Binding>();

    public QueryResult(DatabaseManager service, Connection connection,
            SelectImpl select, ResultSet result, IJDBCDataFormatter formatter,
            int queryNum) throws SQLException 
    {
        this.connection = connection;
        this.select = select;
        this.result = result;
        this.formatter = formatter;
        //
        this.metadata = result.getMetaData();
    }

    public IJDBCDataFormatter getFormatter() {
        return formatter;
    }

    /**
     * return the underlying resultset so you are good to go by yourself!
     * @return
     */
    public ResultSet getResultSet() {
        return this.result;
    }

    public Record getRecord() {
        return new Record();
    }

    protected Binding evalBinding(ExpressionAST expr) throws SQLException {
        Object binding = select.getScope().get(expr);
        if (binding==null || !(binding instanceof ISelectPiece)) {
            return null;
        }
        ISelectPiece piece = (ISelectPiece)binding;
        int index = result.findColumn(piece.getAlias());
        int type = metadata.getColumnType(index);
        return new Binding(index,type);
    }

    public void close() throws SQLException {
        try {
            if (result!=null) {
                Statement stat = result.getStatement();
                try {
                    result.close();
                    stat.close();
                } catch (SQLException e) {
                    stat.close();
                }
            }
            if (connection!=null && !connection.isClosed()) {
                if (!connection.getAutoCommit()) {// ticket:2922
                    connection.commit();
                }
                connection.close();// return the connection to the pool
            }
        } finally {
            result = null;
            connection = null;
        }
    }

}
