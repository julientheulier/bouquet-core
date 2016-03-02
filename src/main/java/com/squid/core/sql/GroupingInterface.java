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
package com.squid.core.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.render.IOrderByPiece;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.ISelectPiece;
import com.squid.core.sql.render.ITypedPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SimpleConstantValuePiece;
import com.squid.core.sql.render.groupby.GroupByElementPiece;
import com.squid.core.sql.render.groupby.GroupType;
import com.squid.core.sql.render.groupby.GroupingSetPiece;
import com.squid.core.sql.render.groupby.IGroupByElementPiece;
import com.squid.core.sql.render.groupby.IGroupByPiece;

public class GroupingInterface {
	
	private ISelect select;
	private boolean forceGroupBy;
	
	public class AdaptiveGroupByPiece
	implements IGroupByPiece
	{

		@Override
		public List<IGroupByElementPiece> getAllPieces() throws ScopeException, SQLScopeException {
			return Collections.unmodifiableList(computeGroupByPieces());
		}
		
		@Override
		public List<IGroupByElementPiece> getPieces(int filter) throws ScopeException, SQLScopeException {
			return Collections.unmodifiableList(computeGroupByPieces(filter));
		}

		@Override
		public String render(SQLSkin skin) throws RenderingException {
			return skin.render(skin, this);
		}
		
	}
	
	protected List<GroupingElement> createList(List<ExpressionAST> expressions) {
		ArrayList<GroupingElement> result = new ArrayList<GroupingElement>();
		for (ExpressionAST e : expressions) {
			result.add(new GroupingElement(e));
		}
		return result;
	}
	
	public class GroupingSet {
		
		private LinkedHashSet<GroupingElement> set;
		
		public GroupingSet() {
			super();
			this.set = new LinkedHashSet<GroupingElement>();
		}

		public GroupingSet(List<GroupingElement> set) {
			super();
			this.set = new LinkedHashSet<GroupingElement>(set);
		}

		public GroupingSet(GroupingElement singleton) {
			super();
			this.set = new LinkedHashSet<GroupingElement>();
			this.set.add(singleton);
		}
		
		public void add(IPiece piece) {
			GroupingElement ep = new GroupingElement(piece);
			this.set.add(ep);
			GroupingInterface.this.sets_scope.add(ep);
		}
		
		public void add(ExpressionAST expression) {
			GroupingElement ep = new GroupingElement(expression);
			this.set.add(ep);
			GroupingInterface.this.sets_scope.add(ep);
		}

		public HashSet<GroupingElement> getSet() {
			return set;
		}
		
		@Override
		public int hashCode() {
			return this.set.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof GroupingSet) {
				return this.set.equals(((GroupingSet)obj).set);
			} else {
				return super.equals(obj);
			}
		}
		
	}
	
	private ArrayList<GroupingSet> sets = new ArrayList<GroupingSet>();
	private LinkedHashSet<GroupingElement> sets_scope = new LinkedHashSet<GroupingElement>();
	
	private ArrayList<GroupingElement> groups = new ArrayList<GroupingElement>();
	private LinkedHashSet<GroupingElement> groups_scope = new LinkedHashSet<GroupingElement>();
	
	private ArrayList<GroupingElement> rollup = new ArrayList<GroupingElement>();
	
	public GroupingInterface(ISelect select) {
		super();
		this.select = select;
	}
	
	/**
	 * clear all groups and grouping sets
	 */
	public void clear() {
		sets = new ArrayList<GroupingSet>();
		sets_scope = new LinkedHashSet<GroupingElement>();
		groups = new ArrayList<GroupingElement>();
		groups_scope = new LinkedHashSet<GroupingElement>();
	}

	public boolean isForceGroupBy() {
		return forceGroupBy;
	}

	public void setForceGroupBy(boolean forceGroupBy) {
		this.forceGroupBy = forceGroupBy;
	}

	public void addGroupBy(ExpressionAST e) {
		GroupingElement ep = new GroupingElement(e);
		if (groups_scope.add(ep)) {
			groups.add(ep);
		}
	}

	public void addGroupBy(IPiece piece) {
		GroupingElement ep = new GroupingElement(piece);
		if (groups_scope.add(ep)) {
			groups.add(ep);
		}
	}
	
	public List<GroupingSet> getGroupingSet() {
		return Collections.unmodifiableList(sets);
	}

	public GroupingSet addGroupingSet() {
		GroupingSet x = new GroupingSet();
		sets.add(x);
		return x;
	}

	public void addGroupingSet(ExpressionAST e) {
		GroupingElement ep = new GroupingElement(e);
		if (sets.add(new GroupingSet(Collections.singletonList(ep)))) {
			sets_scope.add(ep);
		}
	}

	public void addGroupingSet(List<GroupingElement> epl) {
		if (sets.add(new GroupingSet(epl))) {
			sets_scope.addAll(epl);
		}
	}

	public void addGroupingSet(IPiece e) {
		GroupingElement ep = new GroupingElement(e);
		if (sets.add(new GroupingSet(ep))) {
			sets_scope.add(ep);
		}
	}

	/** 
	 * native rollup support
	 * @param axis
	 */
	public void addRollup(ExpressionAST expr) {
		if (rollup==null) {
			rollup = new ArrayList<GroupingElement>();
		}
		rollup.add(new GroupingElement(expr));
	}
	
	public IGroupByPiece createGroupByPiece() {
		return new AdaptiveGroupByPiece();
	}

	public List<IGroupByElementPiece> computeGroupByPieces() throws ScopeException, SQLScopeException {
		return computeGroupByPieces(IGroupByPiece.ALL);
	}
	
	public List<IGroupByElementPiece> computeGroupByPieces(int filter) throws ScopeException, SQLScopeException {
		ArrayList<GroupingElement> my_groups = new ArrayList<GroupingElement>(groups);
		HashSet<GroupingElement> my_scope = new HashSet<GroupingElement>();
		if ((filter & IGroupByPiece.GROUP_BY)>0) {
			my_scope.addAll(groups_scope);
		}
		if ((filter & IGroupByPiece.GROUPING_SETS)>0) {
			my_scope.addAll(sets_scope);
		} else {
			if (!sets.isEmpty()) {
				// we have to force using the grouping set as a group by
				for (GroupingSet set : sets) {
					for (GroupingElement piece : set.getSet()) {
						if (!my_scope.contains(piece)) {
							my_groups.add(piece);
							my_scope.add(piece);
						}
					}
				}
			}
		}
		// ---
		if (forceGroupBy) {
			List<GroupingElement> force_groups = computeGroupByExpressions();
			for (GroupingElement e : force_groups) {
				if (!my_scope.contains(e)) {
					my_groups.add(e);
					my_scope.add(e);
				}
			}
		}
		// --- T130: take care of orderby expression
		{
			List<GroupingElement> orderBy_groups = insertOrderByExpressions();
			for (GroupingElement e : orderBy_groups) {
				if (!my_scope.contains(e)) {
					my_groups.add(e);
					my_scope.add(e);
				}
			}
		}
		// ---
		ArrayList<IGroupByElementPiece> pieces = new ArrayList<IGroupByElementPiece>();
		for (GroupingElement expr : my_groups) {
			if (expr.isExpression()) {
				IPiece piece = select.createPiece(Context.GROUPBY, select.getScope(), expr.getExpression());
				pieces.add(new GroupByElementPiece(piece));
			} else {
				pieces.add(new GroupByElementPiece(expr.getPiece()));
			}
		}
		//
		if (!sets.isEmpty() && (filter & IGroupByPiece.GROUPING_SETS)>0) {
			GroupingSetPiece grouping = new GroupingSetPiece(new ArrayList<IGroupByElementPiece>());
			for (GroupingSet group : sets) {
				GroupingSetPiece inner = new GroupingSetPiece();
				for (GroupingElement ep : group.getSet()) {
					IPiece piece;
					if (ep.isExpression()) {
						piece = select.createPiece(Context.GROUPBY, select.getScope(), ep.getExpression());
					} else {
						piece = ep.getPiece();
					}
					GroupByElementPiece element = new GroupByElementPiece(piece);
					inner.getPieces().add(element);
				}
				grouping.getPieces().add(inner);
			}
			pieces.add(grouping);
		}
		// rollup support
		if (rollup!=null && !rollup.isEmpty()) {
			GroupingSetPiece piece = new GroupingSetPiece(new ArrayList<IGroupByElementPiece>(), GroupType.ROLLUP);
			for (GroupingElement element : rollup) {
				IPiece rpiece = select.createPiece(Context.GROUPBY, select.getScope(), element.getExpression());
				piece.getPieces().add(new GroupByElementPiece(rpiece));
			}
			pieces.add(piece);
		}
		//
		return pieces;
	}

	/**
	 * create a list of expression that must be added to the group by set
	 * @return
	 * @throws SQLScopeException 
	 */
	private List<GroupingElement> computeGroupByExpressions() throws SQLScopeException {
		ArrayList<GroupingElement> groupBy = new ArrayList<GroupingElement>();
		for (ISelectPiece piece : select.getStatement().getSelectPieces()) {
			Object binding = select.getScope().get(piece);
			if (piece instanceof ITypedPiece) {
				// if the piece is typed, this is a more precise information
				ITypedPiece typed = (ITypedPiece)piece;
				ExtendedType type = typed.getType();
				if (!type.getDomain().isInstanceOf(AggregateDomain.DOMAIN)
				 && !type.getDomain().equals(IDomain.UNKNOWN)) {// this is the case if the piece is a constant or the null value - if so don't need to group-by
					groupBy.add(new GroupingElement(piece.getSelect()));
				}
			} else if (binding!=null && binding instanceof ExpressionAST) {
				ExpressionAST expr = (ExpressionAST)binding;
				if (!expr.getImageDomain().isInstanceOf(AggregateDomain.DOMAIN)) {
					groupBy.add(new GroupingElement(expr));
				} else {
					// don't add
				}
			} else 
			{
				// no binding, so we cannot add it...
				// throw new SQLScopeException("invalid select piece");
				// loose hypothesis: if we don't know, add it
				IPiece select = piece.getSelect();
				if (select instanceof SimpleConstantValuePiece == false) {
					groupBy.add(new GroupingElement(select));
				}
			}
		}
		return groupBy;
	}
	
	/**
	 * check the orderBy clause and add expression if not yet in the group by scope and its not an aggregate
	 * T130
	 * @return
	 * @throws SQLScopeException
	 */
	private List<GroupingElement> insertOrderByExpressions() throws SQLScopeException {
		ArrayList<GroupingElement> groupBy = new ArrayList<GroupingElement>();
		for (IOrderByPiece orderBy : select.getStatement().getOrderByPieces()) {
			IPiece piece = orderBy.getPiece();
			Object binding = select.getScope().get(piece);
			if (binding==null) {// not in the select, must handle it
				if (piece instanceof ITypedPiece) {
					// if the piece is typed, this is a more precise information
					ITypedPiece typed = (ITypedPiece)piece;
					ExtendedType type = typed.getType();
					if (!type.getDomain().isInstanceOf(AggregateDomain.DOMAIN)
					 && !type.getDomain().equals(IDomain.UNKNOWN)) {// this is the case if the piece is a constant or the null value - if so don't need to group-by
						groupBy.add(new GroupingElement(piece));
					}
				}
			}
		}
		return groupBy;
	}

	public boolean isEmpty() {
		return groups_scope.isEmpty() && sets_scope.isEmpty();
	}

}
