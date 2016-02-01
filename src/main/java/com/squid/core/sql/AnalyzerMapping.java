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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.KeyPair;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.Operators;
import com.squid.core.expression.Compose;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.Operator;
import com.squid.core.expression.reference.ColumnReference;
import com.squid.core.expression.reference.ForeignKeyReference;
import com.squid.core.sql.model.Scope;

/**
 * this is the part of the analyzer responsible for mapping equi-joins
 * @author sergefantino
 *
 */
public class AnalyzerMapping {
	
	public class Mapping {
		public Scope scope;
		public Column column;
		public Mapping(Scope scope, Column column) {
			super();
			this.scope = scope;
			this.column = column;
		}
	}
	
	private HashMap<Column, List<Mapping>> equiMap = new HashMap<Column,List<Mapping>>();
	
	public List<Mapping> getMapping(Column subject) {
		return equiMap.get(subject);
	}

	public void analyzeRelation(Scope joinScope, ExpressionAST relation) {
		List<KeyPair> keys = factorRelation(relation);
		for (KeyPair kp : keys) {
			addMapping(kp.getExported(), new Mapping(joinScope, kp.getPrimary()));
			addMapping(kp.getPrimary(), new Mapping(joinScope, kp.getExported()));
		}
	}
	
	protected void addMapping(Column source, Mapping mapping) {
		if (equiMap.containsKey(source)) {
			List<Mapping> contents = equiMap.get(source);
			contents.add(mapping);
		} else {
			List<Mapping> contents = new LinkedList<Mapping>();
			contents.add(mapping);
			equiMap.put(source, contents);
		}
	}
	
	public List<KeyPair> factorRelation(ExpressionAST relation) {
		// it must be an operator or a FK
		if (relation instanceof Operator) {
			// it can be either a single == or a list
			Operator op = (Operator)relation;
			return factorRelationRaw(op);
		} else if (relation instanceof ForeignKeyReference) {
			ForeignKeyReference ref = (ForeignKeyReference)relation;
			// easy going
			return ref.getForeignKey().getKeys();
		} else {
			// no good
			return Collections.emptyList();
		}
	}
	
	private List<KeyPair> factorRelationRaw(Operator op) {
		if (op.getOperatorDefinition().getId()==IntrinsicOperators.IDENTITY) {
			// ok, just ignore
			return factorRelation(op.getArguments().get(0));
		} else if (op.getOperatorDefinition()==Operators.EQUAL) {
			// ok, simple case
			KeyPair kp = factorEqui(op);
			if (kp!=null) {
				return Collections.singletonList(kp);
			} else {
				return Collections.emptyList();
			}
		} else if (op.getOperatorDefinition()==Operators.AND) {
			// ok, hope it's not embedded ?
			LinkedList<KeyPair> result = new LinkedList<KeyPair>();
			for (ExpressionAST arg : op.getArguments()) {
				List<KeyPair> factor = factorRelation(arg);
				if (factor.isEmpty()) {
					//return factor;// shortcut
					// ignore in case of some constant ?
				} else {
					result.addAll(factor);
				}
			}
			// looks good
			return result;
		} else {
			// not good
			return Collections.emptyList();
		}
	}

	private KeyPair factorEqui(Operator op) {
		// must have 2 arguments
		if (op.getArguments().size()!=2) {
			return null;
		}
		ExpressionAST first = op.getArguments().get(0);
		ExpressionAST second = op.getArguments().get(1);
		// factor columns
		Column c1 = factorColumn(first);
		Column c2 = factorColumn(second);
		if (c1!=null && c2!=null) {
			// sounds good
			return new KeyPair(c1, c2, -1);
		} else {
			return null;
		}
	}

	private Column factorColumn(ExpressionAST first) {
		// ok, it is something like table.column... easy, isn't it?
		if (first instanceof Compose) {
			Compose compose = (Compose)first;
			if (compose.getBody().size()==2) {
				ExpressionAST head = compose.getHead();// just interested in the head - if it is a column then [0] must be a table
				if (head instanceof ColumnReference) {
					return ((ColumnReference)head).getColumn();
				}
			}
		}
		// else
		return null;
	}

}
