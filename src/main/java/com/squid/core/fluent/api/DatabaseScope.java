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
package com.squid.core.fluent.api;

import java.util.concurrent.ExecutionException;

import com.squid.core.database.model.Database;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;
import com.squid.core.sql.model.SQLScopeException;

public class DatabaseScope {
	
	private Database service;

	public DatabaseScope(Database service) {
		super();
		this.service = service;
	}
	
	public Select FROM(String qualifiedName) throws SQLScopeException {
		return new SelectFromTable(lookup(qualifiedName));
	}
	
	protected Table lookup(String qualifiedName) throws SQLScopeException {
		// parse the table reference
		int separator = qualifiedName.indexOf('.');
		if (separator < 0) {
			throw new SQLScopeException("must specify the table's schema");
		} else {
			String schemaName = qualifiedName.substring(0, separator);
			Schema schema = service.findSchema(schemaName);
			if (schema != null) {
				String tableName = qualifiedName.substring(separator + 1,
						qualifiedName.length());
				try {
					Table table = schema.findTable(tableName);
					if (table != null) {
						return table;
					} else {
						throw new SQLScopeException("undefined table '"+qualifiedName+"'");
					}
				} catch (ExecutionException e) {
					throw new SQLScopeException("cannot lookup table '"+tableName+"' in schema '"+schema.getName()+"'", e);
				}
			} else {
				throw new SQLScopeException("undefined schema '"+schemaName+"'");
			}
		}
	}

}
