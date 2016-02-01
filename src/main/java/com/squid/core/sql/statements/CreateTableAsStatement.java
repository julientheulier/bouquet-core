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
package com.squid.core.sql.statements;

import com.squid.core.sql.model.Aliaser;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.WrapStatementPiece;

import java.util.ArrayList;
import java.util.List;

public class CreateTableAsStatement
extends Statement {

	private IPiece insertIntoTable;
	private ArrayList<IPiece> insertIntoColumns = new ArrayList<IPiece>();
	private IPiece value;

	private Aliaser aliaser = new Aliaser();

	public CreateTableAsStatement() {
		this.aliaser = new Aliaser();
	}

	public CreateTableAsStatement(Aliaser aliaser) {
		this.aliaser = aliaser;
	}


	public Aliaser getAliaser() {
		return aliaser;
	}

	public void setAliaser(Aliaser aliaser) {
		this.aliaser = aliaser;
	}

	public IPiece getInsertIntoTable() {
		return insertIntoTable;
	}

	public void setInsertIntoTable(IPiece insertIntoTable) {
		this.insertIntoTable = insertIntoTable;
	}
	
	public boolean hasInsertIntoColumns() {
		return !insertIntoColumns.isEmpty();
	}
    
    public List<IPiece> getInsertIntoColumns() {
        return insertIntoColumns;
    }
    
    public void setInsertSelect(ISelectStatement select) {
    	this.value = new WrapStatementPiece(select);
    }
    
    public IPiece getInsertValue() {
    	return this.value;
    }
	
	@Override
	public String getTemplateId() {
		return "createTableAsStatement";//"com.squid.core.sql2.statements.InsertSelectStatement";
	}
	
}
