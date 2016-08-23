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
import java.sql.Statement;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.impl.DataSourceReliable.FeatureSupport;
import com.squid.core.database.model.impl.DatabaseManager;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;
import com.squid.core.sql.render.ISkinFeatureSupport;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;


/**
 * Static class supporting query execution
 * @author sergefantino
 *
 */
public class Queries {
    
    static final Logger logger = LoggerFactory
            .getLogger(Queries.class);
    
    static final AtomicInteger queryCnt = new AtomicInteger();
    
    /**
     * execute the query defined by this select statement on the given database
     * @param service
     * @param select
     * @return
     * @throws RenderingException
     * @throws DatabaseServiceException
     */
    public static QueryResult execute(DatabaseManager service, SelectImpl select) throws ExecutionException {
        int queryNum = queryCnt.incrementAndGet();
        long now = System.currentTimeMillis();
        try {
	        SQLSkin skin = service.getSkin();
	        String sql = select.render(skin);
            Connection connection = service.getDatasource().getConnectionBlocking();
            Statement statement = connection.createStatement();
            try {
                IJDBCDataFormatter formatter = service.getDataFormatter(connection);
                logger.info("running SQLQuery#" + queryNum + " on " + service.getDatabase().getUrl()
                        + ":\n" + sql +"\nHashcode="+sql.hashCode());
                // make sure auto commit is false (for cursor based ResultSets and postgresql)
				if(service.getSkin().getFeatureSupport(FeatureSupport.AUTOCOMMIT) == ISkinFeatureSupport.IS_NOT_SUPPORTED){
                	connection.setAutoCommit(false);
                }else{
                	connection.setAutoCommit(true);
                }
                statement.setFetchSize(10000);
                ResultSet result = statement.executeQuery(sql);
                logger.info("SQLQuery#" + queryNum + " executed in "
                        + (System.currentTimeMillis() - now) + " ms.");
                return new QueryResult(service, connection, select, result, formatter, queryNum);
            } catch (Exception e) {
                // ticket:2972
                // it is our responsibility to dispose connection and statement
                if (statement!=null) statement.close();
                if (connection!=null) {
                	if (!connection.getAutoCommit()) {
                		connection.rollback();
                	}
                    connection.close();
                    service.getDatasource().releaseSemaphore();
                }
                throw e;
            }
        } catch (Exception e) {
            logger.info(e.toString());
            logger.info("SQLQuery#" + queryNum + " failed in "
                    + (System.currentTimeMillis() - now) + " ms.");
            throw new ExecutionException("SQLQuery#" + queryNum + " failed", e);

        }
    }

}
