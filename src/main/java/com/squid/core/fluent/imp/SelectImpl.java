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
package com.squid.core.fluent.imp;

import com.squid.core.database.model.Database;
import com.squid.core.database.model.ForeignKey;
import com.squid.core.database.model.Table;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.analytics.AnalyticDomain;
import com.squid.core.expression.Compose;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.ExpressionRef;
import com.squid.core.expression.reference.Cardinality;
import com.squid.core.expression.reference.ForeignKeyReference;
import com.squid.core.expression.reference.ForeignKeyRelationReference;
import com.squid.core.expression.reference.RelationDirection;
import com.squid.core.expression.reference.TableReference;
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.AnalyzerMapping;
import com.squid.core.sql.Context;
import com.squid.core.sql.GroupingInterface;
import com.squid.core.sql.ISelect;
import com.squid.core.sql.db.render.FromTablePiece;
import com.squid.core.sql.db.statements.DatabaseSelectInterface;
import com.squid.core.sql.db.templates.SkinFactory;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.IFromPiece;
import com.squid.core.sql.render.IJoinDecorator.JoinType;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.ISelectPiece;
import com.squid.core.sql.render.IWherePiece;
import com.squid.core.sql.render.JoinDecorator;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SelectPiece;
import com.squid.core.sql.render.WherePiece;

/**
 * this is the actual implementation of the select
 * @author sergefantino
 *
 */
public class SelectImpl extends DatabaseSelectInterface implements ISelect {

	private SQLSkin skin;
	private PieceCreator pieceCreator;
	private GroupingInterface grouping;

	private AnalyzerMapping analyzer;

	public SelectImpl(Database db) {
		this.skin = SkinFactory.INSTANCE.createSkin(db);
		this.pieceCreator = createPieceCreator(this);
		//
		this.analyzer = createAnalyzer();
	}

	/**
	 * shorthand that performs a from(table)
	 * @param table
	 * @throws SQLScopeException
	 */
	public SelectImpl(Table table) throws SQLScopeException {
		this(table.getSchema().getDatabase());
		from(table);
	}

	protected AnalyzerMapping createAnalyzer() {
		return new AnalyzerMapping();
	}

	@Override
	public GroupingInterface getGrouping() {
		if (grouping==null) {
			grouping = new GroupingInterface(this);
		}
		return grouping;
	}

	@Override
	public IPiece createPiece(Context ctx, Scope scope, ExpressionAST expression)
			throws SQLScopeException {
		return this.pieceCreator.createPiece(ctx, scope, expression);
	}

	@Override
	public IPiece createPiece(Context ctx, Scope scope, ExpressionAST expression, Object mapping)
			throws SQLScopeException, ScopeException {
		return this.pieceCreator.createPiece(ctx, scope, expression);
	}

	/**
	 * select the given expression in the current scope
	 * @param expr
	 * @return
	 * @throws SQLScopeException
	 * @throws ScopeException
	 */
	public ISelectPiece select(ExpressionAST expression) throws SQLScopeException {
		return select(getScope(),expression);
	}

	public ISelectPiece select(ExpressionAST expression, String name) throws SQLScopeException {
		return select(getScope(), expression, name, true,true);
	}

	public ISelectPiece select(Scope parent, ExpressionAST expression) throws SQLScopeException {
		String baseName = guessExpressionName(expression);
		return select(parent, expression, baseName, true, true);
	}

	public ISelectPiece select(Scope parent, ExpressionAST expression, String baseName, boolean useAlias, boolean normalizeAlias) throws SQLScopeException {
		IDomain source = expression.getSourceDomain();
		Object mapping = null;
		if (source.isInstanceOf(IDomain.OBJECT)) {
			// check for availability in the parent scope
			Object object = source.getAdapter(Table.class);
			if (object!=null && object instanceof Table) {
				mapping = parent.get(object);
			}
		}
		if (mapping==null && !source.equals(IDomain.NULL)) { // null domain will be automatically bound to the main scope
			throw new SQLScopeException("the source domain is not bound");
		}
		IPiece piece = pieceCreator.createPiece(Context.SELECT, parent, expression);
		SelectPiece select =  getStatement().createSelectPiece(parent, piece, baseName, useAlias, normalizeAlias);
		// check if already bound
		if (parent.contains(expression)) {
			Object twin = parent.get(expression);
			if (twin instanceof SelectPiece) {
				SelectPiece check = (SelectPiece)twin;
				if (check.getSelect().equals(piece)) {
					return check;
				} else {
					throw new SQLScopeException("Binding is already mapped to a different value");
				}
			}
		}
		// else
		// bound the expression
		parent.override(expression, select);
		// bound the IPiece in the MAIN scope -- it must be possible to find it from the statement root
		getScope().put(select, expression);
		getStatement().getSelectPieces().add(select);
		return select;
	}

	protected String guessExpressionName(ExpressionAST expression) {
		if (expression instanceof ExpressionRef) {
			ExpressionRef ref = (ExpressionRef)expression;
			return ref.getReferenceName();
		}
		if (expression instanceof Compose) {
			Compose compose = (Compose)expression;
			return guessExpressionName(compose.getHead());
		}
		//
		return null;
	}

	protected PieceCreator createPieceCreator(SelectImpl select) {
		return new PieceCreator(select);
	}

	public SQLSkin getSkin() {
		return skin;
	}

	@Override
	public IFromPiece from(Context ctx, Scope scope, ExpressionAST expression)
			throws SQLScopeException {
		if (expression instanceof TableReference) {
			Table table = ((TableReference)expression).getTable();
			Object binding = scope.get(table);
			if (binding!=null && binding instanceof IFromPiece) {
				return ((IFromPiece)binding);
			} else {
				throw new SQLScopeException("the source domain '"+table.getName()+"'is not bound");
			}
			//throw new SQLScopeException("Unsupported from-expression: "+expression.toString());
		} else if (expression instanceof ForeignKeyRelationReference) {
			ForeignKeyRelationReference reference = (ForeignKeyRelationReference)expression;
			ForeignKey fk = reference.getForeignKey();
			if (fk!=null) {
				Table source = reference.getSourceTable();
				Table target = reference.getImageTable();
				if (source!=null && target!=null) {
					Object binding = scope.get(source);
					if (binding!=null && binding instanceof IFromPiece) {
						IFromPiece from = (IFromPiece)binding;
						return join(ctx, from, reference);
					}
				}
			}
			//
			throw new SQLScopeException("Unable to apply from-expression: "+expression.toString());
		} else {
			throw new SQLScopeException("Unsupported from-expression: "+expression.toString());
		}
	}
	private IFromPiece join(Context ctx, IFromPiece from, ForeignKeyRelationReference relation) throws SQLScopeException {
		Object binding = from.getScope().get(relation.getForeignKey());
		if (binding==null) {
			RelationDirection direction = relation.getDirection();
			Cardinality sourceCardinality = (direction==RelationDirection.LEFT_TO_RIGHT)?Cardinality.MANY:Cardinality.ONE;
			Cardinality targetCardinality = (direction==RelationDirection.LEFT_TO_RIGHT)?Cardinality.ONE:Cardinality.MANY;
			JoinType type = computeJoinType(sourceCardinality, targetCardinality);
			if (ctx==Context.WHERE && type==JoinType.LEFT) {
				// if we are creating a condition, inner is fine
				type = JoinType.INNER;
			}
			// check if we must enforce a natural join => when the from definition is in a different statement
			boolean forceNaturalJoin = from.getStatement()!=getStatement();
			IFromPiece to = forceNaturalJoin?createNaturalJoin(from, relation):createJoin(ctx, type, from, relation);
			from.getScope().put(relation, to);
			to.getScope().put(relation, from);// opposite link
			return to;
		} else {
			// follow existing relation
			if (binding instanceof IFromPiece) {
				return (IFromPiece)binding;
			} else {
				throw new SQLScopeException("invalid binding for relation '"+relation.getReferenceName()+"'");
			}
		}
	}

	private IFromPiece createJoin(Context ctx, JoinType type, IFromPiece from, ForeignKeyRelationReference relation) throws SQLScopeException {
		switch (type) {
			case LEFT:
				return createLeftJoin(ctx, from, relation);
			case INNER:
				return createInnerJoin(ctx, from, relation);
			case RIGHT:
				return createRightJoin(ctx, from, relation);
			default:
				throw new RuntimeException("Unsupported join type");
		}
	}

	private JoinType computeJoinType(Cardinality sourceCardinality, Cardinality targetCardinality) {
		switch (targetCardinality) {
			case ZERO_OR_ONE:
				return JoinType.LEFT;
			case ONE:
				return JoinType.INNER;
			case MANY:
				return JoinType.LEFT;
			default:
				throw new RuntimeException("Unexpected cardinality definition");
		}
	}

	// create a simple natural join
	private IFromPiece createNaturalJoin(IFromPiece from, ForeignKeyRelationReference relation) throws SQLScopeException {
		// just add the target and link
		Scope subscope = new Scope(from.getScope());
		IFromPiece targetFromPiece = createFromPiece(subscope,relation.getImageTable());
		getStatement().getFromPieces().add(targetFromPiece);
		//
		Scope joinScope = new Scope();
		joinScope.put(relation.getSourceDomain(), from);
		joinScope.put(relation.getImageTable(), targetFromPiece);
		//
		ExpressionAST conditionExpression = new ForeignKeyReference(relation.getForeignKey());
		where(joinScope, conditionExpression);
		//
		if (analyzer!=null) {
			analyzer.analyzeRelation(joinScope, conditionExpression);
		}
		//
		return targetFromPiece;
	}

	private IFromPiece createRightJoin(Context ctx, IFromPiece from, ForeignKeyRelationReference relation) throws SQLScopeException {
		return createDecoratedJoin(ctx, from, JoinType.RIGHT, relation);
	}

	private IFromPiece createLeftJoin(Context ctx, IFromPiece from, ForeignKeyRelationReference relation) throws SQLScopeException {
		return createDecoratedJoin(ctx, from, JoinType.LEFT, relation);
	}

	private IFromPiece createInnerJoin(Context ctx, IFromPiece from, ForeignKeyRelationReference relation) throws SQLScopeException {
		return createDecoratedJoin(ctx, from, JoinType.INNER, relation);
	}

	private IFromPiece createDecoratedJoin(Context ctx, IFromPiece from, JoinType joinType, ForeignKeyRelationReference relation) throws SQLScopeException {
		// just add the target and link
		Scope subscope = new Scope(from.getScope());
		IFromPiece targetFromPiece = createFromPiece(subscope,relation.getImageTable());
		//
		if (targetFromPiece instanceof FromTablePiece && joinType==JoinType.LEFT) {
			((FromTablePiece)targetFromPiece).setDense(false);// LEFT JOIN is NOT DENSE, i.e. it may return NULL records
		}
		//
		Scope joinScope = new Scope();
		joinScope.put(relation.getSourceTable(), from);
		joinScope.put(relation.getImageTable(), targetFromPiece);
		//
		ExpressionAST conditionExpression = new ForeignKeyReference(relation.getForeignKey());
		IPiece conditionPiece = createPiece(ctx, joinScope, conditionExpression);
		//
		JoinDecorator decorator = new JoinDecorator(joinType,targetFromPiece,new WherePiece(conditionPiece));
		from.addJoinDecorator(decorator);
		//
		if (analyzer!=null && (joinType==JoinType.INNER)) {
			analyzer.analyzeRelation(joinScope, conditionExpression);
		}
		//
		return targetFromPiece;
	}

	public IWherePiece where(ExpressionAST filter) throws SQLScopeException {
		return where(getScope(),filter);
	}

	public IWherePiece where(Scope parent, ExpressionAST filter) throws SQLScopeException {
		IDomain image = filter.getImageDomain();
		if (!image.isInstanceOf(IDomain.CONDITIONAL)) {
			filter.getImageDomain();
			throw new SQLScopeException("Invalid WHERE expression: it must be a condition ");
		}
		IPiece piece = createPiece(Context.WHERE, parent, filter);// must use the local scope
		IWherePiece where = addWherePiece(parent,piece);
		// add support for HAVING and QUALIFY
		//
		if (image.isInstanceOf(AggregateDomain.DOMAIN)) {
			where.setType(IWherePiece.HAVING);
		} else if (image.isInstanceOf(AnalyticDomain.DOMAIN)) {
			where.setType(IWherePiece.QUALIFY);
		}
		return where;
	}


}
