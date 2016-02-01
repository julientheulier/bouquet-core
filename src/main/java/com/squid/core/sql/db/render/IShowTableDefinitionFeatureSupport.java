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
package com.squid.core.sql.db.render;

import java.sql.ResultSet;

import com.squid.core.database.model.Table;


/**
 * Ticket #1773: Database: Show table definition does not work
 * @author phuongtd
 *
 */
public interface IShowTableDefinitionFeatureSupport extends IDBEngineFeatureSupport {

	String SHOW_TABLE_DEFINITION_FEATURE_ID = "ShowTableDefinitionFeatureSupport";
	
	String createShowTableDefinitionStatement(Table table);

	/**
	 * The resultset of create show table statement can return multiple fields, so it's
	 * necessary to ask the skin to get the right statement from the result.
	 * 
	 * @param result
	 * @return
	 */
	String getTableDefinitionStatement(ResultSet result);
	
	//void beforeExecuteQuery(IExecutionItem ioc);
	
	//void afterExecuteQuery(IExecutionItem ioc);
	
}
