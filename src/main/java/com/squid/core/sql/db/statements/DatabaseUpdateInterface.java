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

import com.squid.core.database.model.Column;
import com.squid.core.database.model.Key;
import com.squid.core.database.model.Table;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.sql.db.render.ColumnPiece;
import com.squid.core.sql.db.render.FromTablePiece;
import com.squid.core.sql.db.render.SimpleSetterPiece;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.*;
import com.squid.core.sql.statements.IUpdateInterface;
import com.squid.core.sql.statements.Statement;
import com.squid.core.sql.statements.UpdateStatement;

public class DatabaseUpdateInterface 
extends CreatePieceInterface implements IUpdateInterface
{
	
	private Table tableToUpdate;
	private UpdateStatement statement;
	private Scope scope;
	
	public DatabaseUpdateInterface(Table tableToUpdate) throws SQLScopeException {
		this.statement = createUpdateStatement();
		this.scope = new Scope();
		this.tableToUpdate = tableToUpdate;
		//
		IFromPiece from = createFromPiece(scope,tableToUpdate);
		statement.setTableToUpdate(from);
		scope.put(tableToUpdate, from);
	}
	
	protected UpdateStatement createUpdateStatement() {
		return new UpdateStatement();
	}
	
	/**
	 * @return the statement
	 */
	public UpdateStatement getStatement() {
		return statement;
	}

	/**
	 * @return the scope
	 */
	public Scope getScope() {
		return scope;
	}
	
	public Table getTableToUpdate() {
		return tableToUpdate;
	}
	
	public ISetterPiece addSetter(Column column, DatabaseSelectInterface select) throws SQLScopeException {
		Object binding = scope.get(column.getTable());
		if (binding==null) {
			throw new SQLScopeException("the column is not bound in the current scope");
		}
		ISetterPiece piece = createSetterPiece(column,new WrapStatementPiece(select.getStatement()));
		statement.getSetters().add(piece);
		return piece;
	}
	
	public ISetterPiece addSetter(Column column, IPiece valuePiece) throws SQLScopeException {
		Object binding = scope.get(column.getTable());
		if (binding==null) {
			throw new SQLScopeException("the column is not bound in the current scope");
		}
		ISetterPiece piece = createSetterPiece(column,valuePiece);
		statement.getSetters().add(piece);
		return piece;
	}
	
	public void join(Scope scope1, Key primaryKey1, Scope scope2, Key primaryKey2) {
		IPiece[] p = new IPiece[2];
		for (int i=0;i<primaryKey1.getColumns().size();i++) {
			Column c1 = (Column)primaryKey1.getColumns().get(i);
			p[0] = new ColumnPiece(scope1,c1);
			Column c2 = (Column)primaryKey2.getColumns().get(i);
			p[1] = new ColumnPiece(scope1,c2);
			IPiece where = new OperatorPiece(OperatorScope.getDefault().lookupByID(IntrinsicOperators.EQUAL),p);
			statement.getWherePieces().add(new WherePiece(where));
		}
	}

	protected ISetterPiece createSetterPiece(Column column, IPiece value) {
		return new SimpleSetterPiece(column,value);
	}

	protected IFromPiece createFromPiece(Scope parent,Table table) {
		return new FromTablePiece(statement,parent,table,statement.getAliaser().getUniqueAlias());
	}

}
