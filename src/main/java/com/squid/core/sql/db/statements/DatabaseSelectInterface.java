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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.Key;
import com.squid.core.database.model.Table;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.db.render.ColumnPiece;
import com.squid.core.sql.db.render.FromTablePiece;
import com.squid.core.sql.model.Aliaser;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.ExpressionListPiece;
import com.squid.core.sql.render.IFromPiece;
import com.squid.core.sql.render.IOrderByPiece.NULLS_ORDERING;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.ISelectPiece;
import com.squid.core.sql.render.IWherePiece;
import com.squid.core.sql.render.NoOpPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.OrderByPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SelectPiece;
import com.squid.core.sql.render.WhereAndPiece;
import com.squid.core.sql.render.WherePiece;
import com.squid.core.sql.render.groupby.GroupByElementPiece;
import com.squid.core.sql.statements.ISelectInterface;
import com.squid.core.sql.statements.SelectStatement;
import com.squid.core.sql.statements.Statement;

public class DatabaseSelectInterface 
implements ISelectInterface
{
	
	private SelectStatement statement;
	protected Scope scope = createMainScope();

	private boolean useLeftJoin = true;

	public DatabaseSelectInterface() {
		this.statement = createStatement(null);
	}
	
	public DatabaseSelectInterface(Scope scope, SelectStatement statement) {
		this.scope = scope;
		this.statement = statement;
	}

	public DatabaseSelectInterface(Aliaser aliaser) {
		this.statement = createStatement(aliaser);
	}
	
	protected SelectStatement createStatement(Aliaser aliaser) {
		return new SelectStatement(aliaser);
	}

	public SelectStatement getStatement() {
		return statement;
	}
	
	protected Scope createMainScope() {
		return new Scope();
	}

	public Scope getScope() {
		return scope;
	}
	
	public SelectPiece select(Column column) throws SQLScopeException {
		return select(getScope(),column);
	}
	
	public SelectPiece select(Column column, String alias) throws SQLScopeException {
		return select(getScope(),column, alias);
	}
	
	public SelectPiece select(Scope scope, Column column) throws SQLScopeException {
		if (!scope.contains(column.getTable())) {
			throw new SQLScopeException("Cannot select the column: the table is not bound");
		}
		SelectPiece select = createSelectPiece(scope, column);
		scope.put(column, select.getSelect());
		statement.getSelectPieces().add(select);
		return select;
	}
	
	public SelectPiece select(Scope scope, Column column, String alias) throws SQLScopeException {
		if (!scope.contains(column.getTable())) {
			throw new SQLScopeException("Cannot select the column: the table is not bound");
		}
		SelectPiece select = createSelectPiece(scope, column, alias);
		scope.put(column, select.getSelect());
		statement.getSelectPieces().add(select);
		return select;
	}
	
	public void select(Key key) throws SQLScopeException {
		select(getScope(),key);
	}
	
	public void orderBy(Column column) throws SQLScopeException{
		OrderByPiece orderBy = new OrderByPiece(new ColumnPiece(getScope(),column));
		statement.getOrderByPieces().add(orderBy);
	}
	
	public OrderByPiece orderBy(Column column, NULLS_ORDERING nullsOrdering) throws SQLScopeException{
		OrderByPiece orderBy = new OrderByPiece(new ColumnPiece(getScope(),column));
		orderBy.setNullsOrdering(nullsOrdering);
		statement.getOrderByPieces().add(orderBy);
		return orderBy;
	}
	
	public OrderByPiece orderBy(IPiece piece) {
		OrderByPiece orderBy = new OrderByPiece(piece);
		statement.getOrderByPieces().add(orderBy);
		return orderBy;
	}
	
	public ArrayList<SelectPiece> select(Scope scope, Key key) throws SQLScopeException {
		ArrayList<SelectPiece> result = new ArrayList<SelectPiece>();
		for (Iterator<Column> iter = key.getColumns().iterator();iter.hasNext();) {
			result.add(select(scope,iter.next()));
		}
		return result;
	}
	
	protected SelectPiece createSelectPiece(Scope scope,Column column) throws SQLScopeException {
		return createSelectPiece(scope,new ColumnPiece(scope,column),column.getName());
	}
	
	protected SelectPiece createSelectPiece(Scope scope,Column column, String alias) throws SQLScopeException {
		return createSelectPiece(scope,new ColumnPiece(scope,column),alias);
	}
	
	protected SelectPiece createSelectPiece(Scope scope,IPiece piece) {
		//return new SelectPiece(scope,piece,statement.getAliaser().getUniqueAlias());
		return statement.createSelectPiece(scope, piece);
	}
	
	protected SelectPiece createSelectPiece(Scope scope,IPiece piece, String baseName, boolean useAlias) {
		//return new SelectPiece(scope,piece,statement.getAliaser().getUniqueAlias());
		return statement.createSelectPiece(scope, piece, baseName, useAlias);
	}
	
	protected SelectPiece createSelectPiece(Scope scope,IPiece piece, String baseName, boolean useAlias, boolean normalizeAlias) {
        //return new SelectPiece(scope,piece,statement.getAliaser().getUniqueAlias());
        return statement.createSelectPiece(scope, piece, baseName, useAlias, normalizeAlias);
    }
	
	protected SelectPiece createSelectPiece(Scope scope,IPiece piece, String baseName) {
		//return new SelectPiece(scope,piece,statement.getAliaser().getUniqueAlias());
		return statement.createSelectPiece(scope, piece, baseName);
	}
	
	public IFromPiece from(Table table) throws SQLScopeException {
		return from(getScope(),table);
	}
	
	public IFromPiece from(Scope parent, Table table) throws SQLScopeException {
		return from(parent,table,createFromPiece(parent,table));
	}
	
	/**
	 * Add the table to the select
	 * @param table
	 * @return
	 * @throws SQLScopeException 
	 */
	public IFromPiece from(Scope parent, Table table, IFromPiece from) throws SQLScopeException {
		statement.getFromPieces().add(from);
		parent.put(table, from);
		return from;
	}
	
	public IFromPiece createFromPiece(Scope parent,Table table) {
		return new FromTablePiece(statement,parent,table,statement.getAliaser().getUniqueAlias());
	}
	
	public void groupBy(Scope scope,Column column) throws ScopeException, SQLScopeException {
		statement.getGroupByPiece().getAllPieces().add(
				new GroupByElementPiece(createPiece(scope,column,false)));
	}

	/*
	public void join(Scope source, Scope target, ForeignKey fk) throws SQLScopeException {
		join(source,fk.getForeignKey(),target,fk.getPrimaryKey());
	}
	*/
	
	public boolean isUseLeftJoin() {
		return useLeftJoin;
	}

	public void setUseLeftJoin(boolean useLeftJoin) {
		this.useLeftJoin = useLeftJoin;
	}

	public void join(Scope source, Key fromKey, Scope target, Key toKey) throws SQLScopeException {
		getStatement().getConditionalPieces().add(createJoin(source,fromKey,target,toKey));
	}
	
	public IWherePiece createJoin(Scope source, Key fromKey, Scope target, Key toKey) throws SQLScopeException {
		int size = fromKey.getColumns().size();
		if (size!=toKey.getColumns().size()) {
			throw new SQLScopeException("keys don't match");
		}
		if (size==0) {
			throw new SQLScopeException("undefined key");
		}
		List<IPiece> pieces = new ArrayList<IPiece>();
		for (int i=0;i<size;i++) {
			IPiece from = null;
			Column from_col = fromKey.getColumns().get(i);
			if (source.contains(from_col) && source.get(from_col) instanceof ColumnPiece) {
				from = (IPiece) source.get(from_col);
			} else {
				from = createPiece(source,fromKey.getColumns().get(i),false);
			}
			IPiece to = null;
			Column to_col = toKey.getColumns().get(i);
			if (target.contains(toKey) && target.get(toKey) instanceof ColumnPiece) {
				to = (IPiece) target.get(to_col);
			} else {
				to = createPiece(target,(Column)toKey.getColumns().get(i),false);
			}
			pieces.add(new OperatorPiece(OperatorScope.getDefault().lookupByID(IntrinsicOperators.EQUAL),new IPiece[]{from,to}));
		}
		IWherePiece where = new WhereAndPiece(pieces);
		return where;
	}
	
	/**
	 * add a where piece
	 * @param parent
	 * @param expression
	 * @return
	 * @throws SQLScopeException 
	 */
	public IWherePiece addWherePiece(Scope parent, IPiece piece) throws SQLScopeException {
		if (piece!=null&&piece!=NoOpPiece.NOOP) {
			IWherePiece where = null;
			if (!(piece instanceof IWherePiece)) {
				where = new WherePiece(piece);
			} else {
				where = (IWherePiece)piece;
			}
			getStatement().getConditionalPieces().add(where);
			return where;
		} else {
			return null;
		}
	}
	
	public IPiece createPiece(Scope parent, Column column) throws SQLScopeException {
		Object override = parent.get(column);
		if (override!=null && override instanceof IPiece) {
			if (override instanceof ISelectPiece) {
				return ((ISelectPiece) override).getSelect();
			} else {
				return (IPiece) override;// ticket:1176
			}
		} else {
			return new ColumnPiece(parent,column);
		}
	}
	
	public IPiece createPiece(Scope parent, Column column, boolean safe) throws SQLScopeException {
		Object override = parent.get(column);
		if (override!=null && override instanceof IPiece) {
			if (override instanceof ISelectPiece) {
				// ignore
			} else {
				return (IPiece) override;// ticket:1176
			}
		}
		//
		return new ColumnPiece(parent,column);
	}
	
	public ExpressionListPiece createPiece(Scope parent, Key key) throws SQLScopeException {
		IPiece[] pieces = new IPiece[key.getColumns().size()];
		for (int i=0;i<pieces.length;i++) {
			pieces[i] = createPiece(parent, (Column)key.getColumns().get(i),false);
		}
		return new ExpressionListPiece(pieces);
	}
	
	///////////////////////////////////////////////////////

	public String render(SQLSkin skin) throws RenderingException {
		Statement statement = skin.prepareStatement(getStatement());
		return statement.render(skin);
	}
	
}
