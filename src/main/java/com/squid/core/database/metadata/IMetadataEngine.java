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

public interface IMetadataEngine {
	
	public static final String POSTGRESQL_NAME = "PostgreSQL";
	public static final String REDSHIFT_NAME = "Redshift";
	public static final String GREENPLUM_NAME = "Greenplum";
	public static final String HAWQ_NAME = "Hawq";
	public static final String MYSQL_NAME = "MySQL";
	public static final String APACHEDRILL_NAME = "Apache Drill";
	public static final String ORACLE_NAME = "Oracle";
	public static final String SQLSERVER_NAME = "Microsoft SQL Server";
	public static final String SPARK_NAME = "Spark SQL";
	public static final String HIVE_NAME = "Apache Hive";
	public static final String TERADATA_NAME = "Teradata";

}
