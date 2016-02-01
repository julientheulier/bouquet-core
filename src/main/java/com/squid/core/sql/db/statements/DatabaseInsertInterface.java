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
package com.squid.core.sql.db.statements;

import java.io.IOException;
import java.util.Iterator;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.Key;
import com.squid.core.database.model.Table;
import com.squid.core.database.model.impl.TableImpl;
import com.squid.core.sql.DDLStatementUtils;
import com.squid.core.sql.db.render.DummyIdentifierPiece;
import com.squid.core.sql.db.render.TablePiece;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.statements.*;

public class DatabaseInsertInterface
{
	
	private InsertSelectStatement insertStatement;
	private CreateTableAsStatement createTableAsStatement;
	private DDLCreateTableStatement createTableStatement;

	private insertionImpl insertImpl = insertionImpl.CREATEAS;

	public DatabaseInsertInterface(Table insertInto) {
		this.insertStatement = new InsertSelectStatement();
		this.createTableAsStatement = new CreateTableAsStatement();
		this.createTableStatement = new DDLCreateTableStatement(insertInto);
		this.insertStatement.setInsertIntoTable(new TablePiece(insertInto));
		this.createTableAsStatement.setInsertIntoTable(new TablePiece(insertInto));
	}




	public Statement getStatement() {
		switch(this.insertImpl){
			case CREATEAS:
				return createTableAsStatement;
			case INSERT:
				return insertStatement;
			default:
				return createTableAsStatement;
		}
	}
	
	public void addInserIntoColumn(Column column) {
		this.insertImpl = insertionImpl.INSERT;
		this.insertStatement.getInsertIntoColumns().add(new DummyIdentifierPiece(column));
	}
	
	public void addInserIntoColumn(Key key) {
		for (Iterator<Column> iter = key.getColumns().iterator();iter.hasNext();) {
			addInserIntoColumn(iter.next());
		}
	}
	
	public void setInsertFromSelect(DatabaseSelectInterface select) {
		this.insertStatement.setInsertSelect(select.getStatement());
		this.createTableAsStatement.setInsertSelect(select.getStatement());
	}
	
	public void setInsertFromSelect(ISelectStatement select) {
		this.insertStatement.setInsertSelect(select);
		this.createTableAsStatement.setInsertSelect(select);
	}
	public void setValues(IPiece[] values){
		this.insertStatement.setValue(values);
	}

	public String render(SQLSkin skin) throws RenderingException {
		Statement statement = getStatement();
		String result = "";
		if(!(this.insertImpl == insertionImpl.INSERT)){
			result = statement.render(skin);
		}else{
			result = createTableStatement.render(skin);
			result +=";"; //TODO correctly doing composition...
			result += statement.render(skin);
		}
		return result;
	}



}
