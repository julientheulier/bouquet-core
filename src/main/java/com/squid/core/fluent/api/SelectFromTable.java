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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.ForeignKey;
import com.squid.core.database.model.Table;
import com.squid.core.domain.DomainBase;
import com.squid.core.domain.IDomain;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.reference.ColumnReference;
import com.squid.core.fluent.imp.SelectImpl;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.render.ISelectPiece;

/**
 * A simple Select implementation working with just a single table
 * @author sergefantino
 *
 */
public class SelectFromTable extends SelectBase {
	
	private Table table;
	private IDomain domain;
	
	class MyDomain extends DomainBase {
		
	}

	public SelectFromTable(Table table) {
		super();
		this.table = table;
		this.domain = new MyDomain();
	}
	
	@Override
	public IDomain getDomain() {
		return domain;
	}

	public Table getTable() {
		return table;
	}
	
	@Override
	public ExpressionAST[] PK() {
		List<ExpressionAST> pk = new ArrayList<ExpressionAST>();
		for (Column column : table.getPrimaryKey().getColumns()) {
			pk.add(SELECT(column));
		}
		ExpressionAST[] result = new ExpressionAST[pk.size()];
		return pk.toArray(result);
	}
	
	/**
	 * apply the select expression against this scope and return a reference to it
	 * @param expr
	 * @return
	 */
	@Override
	public Expression SELECT(String columnName) throws SQLScopeException {
		try {
			Column column = this.table.findColumnByName(columnName);
			if (column==null) {
				throw new SQLScopeException("undefined column '"+columnName+"' in table '"+this.table.getName()+"'");
			}
			return SELECT(column);
		} catch (ExecutionException e) {
			throw new SQLScopeException("undefined column '"+columnName+"' in table '"+this.table.getName()+"'",e);
		}
	}
	
	protected Expression SELECT(Column column) {
		// no check to enforce that column is part of the table
		ColumnReference ref = new ColumnReference(column);
		return new Expression(this,ref);
	}
	
	@Override
	public Select JOIN(String fkName) throws SQLScopeException {
		// lookup the fk in the table
		try {
			ForeignKey fk = table.findForeignKeyByName(fkName);
			if (fk==null) {
				throw new SQLScopeException("undefined foreign-key '"+fkName+"' for table '"+table.getName()+"'");
			} else {
				return new SelectJoin(this,fk);
			}
		} catch (ExecutionException e) {
			throw new SQLScopeException("undefined foreign-key '"+fkName+"' for table '"+table.getName()+"'",e);
		}
	}

	@Override
	public SelectImpl apply(ExpressionAST... expressions) throws SQLScopeException {
		SelectImpl imp = new SelectImpl(table);
		imp.from(getTable());
		for (ExpressionAST expr : expressions) {
			imp.select(expr);
		}
		return imp;
	}

}
