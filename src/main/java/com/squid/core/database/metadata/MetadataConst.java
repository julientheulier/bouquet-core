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

/**
 * 
 * JDBC metadata constants
 * 
 * @author serge fantino
 */
public interface MetadataConst {

	public final static String REMARKS = "REMARKS";
    // tables
	public static final String TABLE_SCHEMA = "TABLE_SCHEM";
	public static final String TABLE_CATALOG = "TABLE_CAT";
	public final static String TABLE_NAME = "TABLE_NAME";
	public final static String TABLE_TYPE = "TABLE_TYPE";
	public final static String TABLE_TYPE_TABLE = "TABLE";
	public final static String TABLE_TYPE_VIEW = "VIEW";
	public final static String TABLE_TYPE_PROCEDURE = "PROCEDURE";
	// columns
	public final static String COLUMN_NAME = "COLUMN_NAME";
	public final static String DATA_TYPE = "DATA_TYPE";
	public final static String TYPE_NAME = "TYPE_NAME";
	public final static String COLUMN_SIZE = "COLUMN_SIZE";
	public final static String DECIMAL_DIGITS = "DECIMAL_DIGITS";
	public final static String NUM_PREC_RADIX = "NUM_PREC_RADIX";
	public final static String NULLABLE= "NULLABLE";
	public final static String COLUMN_DEF = "COLUMN_DEF";
	public final static String SQL_DATA_TYPE = "SQL_DATA_TYPE";
	public final static String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";
	public final static String ORDINAL_POSITION = "ORDINAL_POSITION";
	public final static String IS_NULLABLE = "IS_NULLABLE";
	public final static String SCOPE_CATALOG = "SCOPE_CATLOG";
	public final static String SCOPE_SCHEMA = "SCOPE_SCHEMA";
	public final static String SCOPE_TABLE = "SCOPE_TABLE";
	public final static String SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";
	// 
	public final static String KEY_SEQ = "KEY_SEQ";
	public final static String PK_NAME = "PK_NAME";
	// imported/exported keys
	public final static String PKTABLE_CAT = "PKTABLE_CAT";
	public final static String PKTABLE_SCHEM = "PKTABLE_SCHEM";
	public final static String PKTABLE_NAME = "PKTABLE_NAME";
	public final static String PKCOLUMN_NAME = "PKCOLUMN_NAME";
	public final static String FKTABLE_CAT = "FKTABLE_CAT";
	public final static String FKTABLE_SCHEM = "FKTABLE_SCHEM";
	public final static String FKTABLE_NAME = "FKTABLE_NAME";
	public final static String FKCOLUMN_NAME = "FKCOLUMN_NAME";
	//public final static String KEY_SEQ = "KEY_SEQ";
	public final static String UPDATE_RULE = "UPDATE_RULE";
	public final static String DELETE_RULE = "DELETE_RULE";
	public final static String FK_NAME = "FK_NAME";
	//public final static String PK_NAME = "PK_NAME";
	public final static String DEFERRABILITY = "DEFERRABILITY";
    //
    // typeInfo
    //public final static String TYPE_NAME = "TYPE_NAME";
    //public final static String DATA_TYPE = "DATA_TYPE";
    public final static String PRECISION = "PRECISION";
    public final static String MINIMUM_SCALE = "MINIMUM_SCALE";
    public final static String MAXIMUM_SCALE = "MAXIMUM_SCALE";
    // REMEMBER TO ADD THE KEYWORDS IN THE MetadataEngine.init() method !!!
    
    // indexInfo
    public final static String NON_UNIQUE = "NON_UNIQUE";
    public final static String TYPE = "TYPE";
    public final static String INDEX_NAME = "INDEX_NAME";
    
    public final static String[] definitions = new String[] {
			 TABLE_SCHEMA,TABLE_CATALOG,TABLE_NAME,TABLE_TYPE,
			 TABLE_TYPE_TABLE,TABLE_TYPE_VIEW,TABLE_TYPE_PROCEDURE,
			 COLUMN_NAME,DATA_TYPE,TYPE_NAME,COLUMN_SIZE,DECIMAL_DIGITS,NUM_PREC_RADIX,
			 NULLABLE,REMARKS,COLUMN_DEF,SQL_DATA_TYPE,CHAR_OCTET_LENGTH,
			 CHAR_OCTET_LENGTH,IS_NULLABLE,
			 SCOPE_CATALOG,SCOPE_SCHEMA,SCOPE_TABLE,SOURCE_DATA_TYPE,
			 KEY_SEQ,PK_NAME,
			 PKTABLE_CAT,PKTABLE_SCHEM,PKTABLE_NAME,PKCOLUMN_NAME,
			 FKTABLE_CAT,FKTABLE_SCHEM,FKTABLE_NAME,FKCOLUMN_NAME,
			 UPDATE_RULE,DELETE_RULE,FK_NAME,DEFERRABILITY,
			 PRECISION,MAXIMUM_SCALE,MINIMUM_SCALE,
			 NON_UNIQUE,TYPE,INDEX_NAME,ORDINAL_POSITION
	 };

}
