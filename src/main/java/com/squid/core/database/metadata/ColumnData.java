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

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnData {
    
    public String table_name;
    public String column_name;
    public String type_name;
    public int column_size;
    public String column_def;
    public String is_nullable;
    public int data_type;
	public int decimal_digits;
	public String remarks;
	
	public ColumnData(){
	}
	
	public ColumnData(ColumnData cd){  
		this.table_name = cd.table_name;
		this.column_name = cd.column_name;
		this.type_name = cd.type_name;
		this.column_size = cd.column_size;
		this.column_def = cd.column_def;
		this.is_nullable = cd.is_nullable;
	    this.data_type = cd.data_type;
	    this.decimal_digits = cd.decimal_digits;
	    this.remarks = cd.remarks;	
	}

	public void loadColumnData(int[] COLUMNS_CPOS, ResultSet res) throws SQLException {
		//
		//int[] COLUMNS_CPOS = ((MetadataEngine)engine).getColumnsCPOS(res);
		//
		this.table_name = res.getString(COLUMNS_CPOS[6]);           // TABLE_NAME
		this.column_name = res.getString(COLUMNS_CPOS[0]).trim();   // COLUMN_NAME
		this.type_name = res.getString(COLUMNS_CPOS[1]);            // TYPE_NAME
		this.column_size = res.getInt(COLUMNS_CPOS[2]);             // COLUMN_SIZE
		this.decimal_digits = res.getInt(COLUMNS_CPOS[7]);			// DECIMAL_DIGITS
		//
		// Oracle: issue: jdbc driver throwing exception when getting the DEfault_value column value after
		this.column_def = res.getString(COLUMNS_CPOS[4]);           // COLUMN_DEF
		this.is_nullable = res.getString(COLUMNS_CPOS[3]);          // IS_NULLABLE
		//
		this.data_type = res.getInt(COLUMNS_CPOS[5]);               // DATA_TYPE
		//
		this.remarks = res.getString(COLUMNS_CPOS[8]); // remarks
	}

}
