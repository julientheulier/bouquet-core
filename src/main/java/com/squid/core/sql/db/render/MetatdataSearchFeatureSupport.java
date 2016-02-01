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

import java.util.Iterator;
import java.util.List;

/**
 * Ticket# 1117: [Metadata searches] Search Engine
 * @author Nha
 *
 */
public abstract class MetatdataSearchFeatureSupport implements IDBEngineFeatureSupport {

	public static final String METADATA_SEARCH_FEATURE_ID = "MetadataSearchFeatureID";
	
	public String createMetaSearchQuery(List<String> schemas, String tableName, String columnName, boolean isCaseSensitive){
		StringBuilder sqlCode = new StringBuilder();
		String likeTableName = (tableName != null)? qouteString("%" + tableName + "%") : null;
		String likeColumnName = (columnName != null)? qouteString("%" + columnName + "%") : null;
		if(tableName != null && columnName == null){
			// search tables only
			sqlCode.append(createTableSearch(schemas, likeTableName, isCaseSensitive));
		}else if(columnName != null){
			// search columns only
			sqlCode.append(createColumnSearch(schemas, likeTableName, likeColumnName, isCaseSensitive));
		}
		return sqlCode.toString();
	}
	
	/**
	 * Create search SQL for table
	 * @param schemas
	 * @param tableName
	 * @return
	 */
	public abstract String createTableSearch(List<String> schemas, String tableName, boolean isCaseSensitive);
	
	/**
	 * Create search SQL for column
	 * @param schemas
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public abstract String createColumnSearch(List<String> schemas, String tableName, String columnName, boolean isCaseSensitive);
	
	/**
	 * Get group of schema names from <code>schemas</code>
	 * @param schemas
	 * @return
	 */
	protected String getGroupSchemaNames(List<String> schemas){
		StringBuilder schemaBuffer = new StringBuilder();
		for (Iterator<String> iterator = schemas.iterator(); iterator.hasNext();) {
			schemaBuffer.append(qouteString(iterator.next()));
			if(iterator.hasNext()){
				schemaBuffer.append(",");
			}
		}
		return schemaBuffer.toString();
	}
	
	/**
	 * Apply for case sensitive
	 * @param value
	 * @param isCaseSensitive
	 * @return
	 */
	protected String applyCaseSensitive(String value, boolean isCaseSensitive){
		String applyValue = value;
		if(!isCaseSensitive){
			applyValue = "LOWER(" + value + ")";
		}
		return applyValue;
	}
	
	protected String qouteString(String value) {
		return "'" + value + "'";
	}
	
}
