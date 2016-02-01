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
package com.squid.core.sql.db.statements.ddl;

import com.squid.core.database.model.Table;
import com.squid.core.sql.statements.Statement;

public class CreateTableStatement 
extends Statement 
{

    private Table m_tableDefinition;
    private boolean m_dropTableFirst;

    /**
     * 
     */
    public CreateTableStatement() {
        super();
    }

    public CreateTableStatement(Table table) {
    	setTable(table);
    }

    public Table getTable() {
        return m_tableDefinition;
    }

    public void setTable(Table tableDefinition) {
        m_tableDefinition = tableDefinition;
    }

    public boolean isDropTableFirst() {
        return m_dropTableFirst;
    }

    public void setDropTableFirst(boolean dropTableFirst) {
        m_dropTableFirst = dropTableFirst;
    }
	
	@Override
	public String getTemplateId() {
		return "com.squid.db.sql2.statements.ddl.CreateTableStatement";
	}
    
}
