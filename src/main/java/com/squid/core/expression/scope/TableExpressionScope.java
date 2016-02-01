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
package com.squid.core.expression.scope;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.domain.TableDomain;
import com.squid.core.database.model.Column;
import com.squid.core.database.model.ForeignKey;
import com.squid.core.database.model.Table;
import com.squid.core.domain.IDomain;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.reference.ColumnReference;
import com.squid.core.expression.reference.ForeignKeyRelationReference;

public class TableExpressionScope extends DefaultScope {
	
	private Table table = null;
	
	public TableExpressionScope(Table table) throws ScopeException {
		super();
		this.table  = table;
	}

	@Override
	public ExpressionScope applyExpression(ExpressionAST first) {
		IDomain image = first.getImageDomain();
		if (image.isInstanceOf(TableDomain.DOMAIN)) {
			Table target = ((TableDomain)image).getTable();
			// TODO we should detect possible cycle here
			return createScope(target);
		} else {
			return null;
		}
	}
	
	protected ExpressionScope createScope(Table target) {
		try {
			return new TableExpressionScope(target);
		} catch (ScopeException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IdentifierType lookupIdentifierType(String image) {
		// TODO Auto-generated method stub
		return IdentifierType.DEFAULT;
	}

	@Override
	public Object lookupObject(IdentifierType identifierType, String identifier) throws ScopeException {
		try {
			for (Column c : this.table.getColumns()) {
				if (identifier.equals(c.getName())) {
					return c;
				}
			}
			for (ForeignKey fk : this.table.getForeignKeys()) {
				if (identifier.equals(fk.getName())) {
					return fk;
				}
			}
		} catch (ExecutionException e) {
			throw new ScopeException("cannot lookup identifier '"+identifier+"' in table '"+table.getName()+"'",e);
		}
		// else
		return super.lookupObject(identifierType, identifier);
	}

	@Override
	public ExpressionAST createReferringExpression(Object reference) throws ScopeException {
		if (reference instanceof Column) {
			return new ColumnReference((Column)reference);
		} else if (reference instanceof ForeignKey) {
			return new ForeignKeyRelationReference((ForeignKey)reference,table);
		} else {
			return super.createReferringExpression(reference);
		}
	}
	
	@Override
	public void buildDefinitionList(List<Object> definitions) {
		super.buildDefinitionList(definitions);
		//
		if (table!=null) {
			try {
				definitions.addAll(table.getColumns());
				definitions.addAll(table.getForeignKeys());
			} catch (ExecutionException e) {
				// ignore
			}
		}
	}

}
