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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.concurrent.CancellableCallable;
import com.squid.core.concurrent.ExecutionManager;
import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.impl.DataSourceReliable.FeatureSupport;
import com.squid.core.jdbc.engine.ExecutionItem;
import com.squid.core.jdbc.engine.IExecutionItem;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;
import com.squid.core.sql.render.ISkinFeatureSupport;

/**
 * handles query execution in order to support non-standard JDBC query cancellation
 * @author sergefantino
 *
 */
public class ExecuteQueryTask implements CancellableCallable<IExecutionItem>{

    static final Logger logger = LoggerFactory
            .getLogger(ExecuteQueryTask.class);
    
    
    private DatabaseManager ds;
    private int queryNum;
    private String sql;

    // keep a pointer on the statement so we can call cancel on it
    private Connection connection;
    private Statement statement;
    
    private boolean prepared = false;
    private volatile boolean abort = false;

    public ExecuteQueryTask(DatabaseManager ds, int queryNum, String sql) {
        this.ds = ds;
        this.queryNum = queryNum;
        this.sql = sql;
    }
    
    public void cancel() {
        if (statement!=null) {
            try {
                statement.cancel();
	            logger.info("cancel SQLQuery#" + queryNum);
                this.abort = true;// signal that client should abort asap
            } catch (SQLException e) {
	            logger.error("failed to cancel SQLQuery#" + queryNum);
            }
        }
    }
    
    /**
     * return true if cancel() has been called - this is useful since canceling the SQL statement may not work, or client code may be already reading the resultset
     * 
     * @return
     */
    public boolean isInterrupted() {
    	return abort;
    }
    
    /**
     * optionally prepare the call and make sure to allocate a connection from the pool
     * @throws DatabaseServiceException 
     */
    public void prepare() throws SQLException {
    	if (!prepared) {
	    	prepared = true;// if failed, don't call it again
	        try {
	            connection = ds.getDatasource().getConnectionBlocking();
	            statement = connection.createStatement();
	        } catch (Exception e) {
	            if (connection!=null) {
	                try {
	                    connection.close();
                        ds.getDatasource().releaseSemaphore();
	                    connection = null;
	                } catch (SQLException ee) {
	                    // ignore
	                }
	            }
	            logger.error(e.toString());
	            logger.error("SQLQuery#" + queryNum + " failed to prepare");
	            throw e;
	        }
    	}
    }
    
    public IExecutionItem call() throws SQLException, ExecutionException {
        long now = System.currentTimeMillis();
        try {
        	prepare();
        	//
            if (connection==null) {
            	throw new SQLException("SQLQuery#" + queryNum + " failed: cannot create connection");
            }
            if (statement==null) {
            	throw new SQLException("SQLQuery#" + queryNum + " failed: cannot create statement");
            }
            // ok to start
        	ExecutionManager.INSTANCE.registerTask(this);
        	//
            try {
                logger.info("running SQLQuery#" + queryNum + " on " + ds.getDatabase().getUrl()
                        + ":\n" + sql +"\nHashcode="+sql.hashCode());
                // make sure auto commit is false (for cursor based ResultSets and postgresql)
				if(ds.getSkin().getFeatureSupport(FeatureSupport.AUTOCOMMIT) == ISkinFeatureSupport.IS_NOT_SUPPORTED){
                	connection.setAutoCommit(false);
                }else{
                	connection.setAutoCommit(true);
                }

                statement.setFetchSize(10000);
                Date start = new Date();
//                ResultSet result = statement.executeQuery(sql);
                boolean isResultset = statement.execute(sql);
				while (!isResultset && statement.getUpdateCount() >= -1) {
					isResultset = statement.getMoreResults();
				}
				ResultSet result = statement.getResultSet();
                
                /*logger.info("SQLQuery#" + queryNum + " executed in "
                        + (System.currentTimeMillis() - now) + " ms.");*/
                //MorphiaLoggerFactory.registerLogger(SLF4JLoggerImplFactory.class);

    			logger.info("task="+this.getClass().getName()+" method=executeQuery"+" duration="+(System.currentTimeMillis() - now)+ " error=false status=running driver=" +connection.getMetaData().getDatabaseProductName()  + " queryid="+queryNum+" SQLQuery#" + queryNum + " executed in " + (System.currentTimeMillis() - now) + " ms.");

                IJDBCDataFormatter formatter = ds.getDataFormatter(connection);
                ExecutionItem ex = new ExecutionItem(ds.getDatabase(), ds.getDatasource(), connection, result, formatter, queryNum);
                ex.setExecutionDate(start);
                return ex;
            } catch (Exception e) {
                // ticket:2972
                // it is our responsibility to dispose connection and statement
                if (statement!=null) statement.close();
                statement = null;
                if (connection!=null) connection.close();
                connection = null;
                ds.getDatasource().releaseSemaphore();
                throw e;
            }
        } catch (Exception e) {
            if (connection!=null) {
                try {
                    connection.close();
                    connection = null;
                    ds.getDatasource().releaseSemaphore();
                } catch (SQLException ee) {
                    // ignore
                }
            }
            logger.error(e.toString());
            logger.error("SQLQuery#" + queryNum + " failed in "
                    + (System.currentTimeMillis() - now) + " ms.");
            throw new ExecutionException("SQLQuery#" + queryNum + " failed: ",e);
        } finally {

        	// unregister
        	ExecutionManager.INSTANCE.unregisterTask(this);
        }
    }

}
