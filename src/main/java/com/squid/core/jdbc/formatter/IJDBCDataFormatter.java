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
package com.squid.core.jdbc.formatter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The class is responsible for formatting JDBC data into readable string or plain java object
 * @author Serge Fantino
 *
 */
public interface IJDBCDataFormatter
{
	
	public static final int DEFAULT_FETCH_SIZE = 5000;
	
	public static final boolean DEFAULT_DISPLAYS_WARNING = true;
	
	public IDataFormatter getFormatter();

	// Load the classes used by the plugin using the contextClassloader.
	public void loadClasses();


    /**
     * 
     * @param column
     * @param format
     * @param colType
     * @return
     * @throws SQLException 
     */
    public String formatJDBCObject(final Object jdbcObject, final int colType) throws SQLException;

    
    public void setData(PreparedStatement pstmt, final int position, final Object data, final int colType, final int size, final int precision) throws SQLException;
    
    public int getFetchSize();
    
    public boolean displaysWarnings();
  
    
    // moved from IJDBCDataFormatter
    public String getColumnFormat(int colType, int precision, int scale);
    public Object unboxJDBCObject(final Object jdbcObject, final int colType) throws SQLException;
    
}
