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

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.expression.Compose;
import com.squid.core.expression.ConstantValue;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.NamedExpression;
import com.squid.core.expression.Operator;
import com.squid.core.expression.PrettyPrintOptions;
import com.squid.core.expression.reference.ColumnReference;
import com.squid.core.expression.scope.ExpressionMaker;
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.render.SQLSkin;

public class Expression extends NamedExpression implements ExpressionAST {
	
	private Select scope;
	private ExpressionAST proxy;
	
	public Expression(Select scope, ExpressionAST proxy) {
		super();
		this.scope = scope;
		this.proxy = proxy;
	}

	@Override
	public ExtendedType computeType(SQLSkin skin){
		if (proxy instanceof Operator) {
			return ((Operator)proxy).computeType(skin);
		} else
		if (proxy instanceof Compose) {
			return ((Compose)proxy).computeType(skin);
		} else {
			if (proxy instanceof ColumnReference) {
				return ((ColumnReference)proxy).getColumn().getType();
			} else if (proxy instanceof ConstantValue) {
				return ((ConstantValue)proxy).computeType(skin);
			} else {
				return ExtendedType.UNDEFINED;
			}
		}
	}
	@Override
	public IDomain getImageDomain() {
		return proxy.getImageDomain();
	}
	
	@Override
	public IDomain getSourceDomain() {return scope.getDomain();
	}
	
	@Override
	public String prettyPrint() {
		return proxy.prettyPrint();
	}
	
	@Override
	public String prettyPrint(PrettyPrintOptions options) {
		return proxy.prettyPrint();
	}

	public Expression equal(Object constant) throws SQLScopeException {
		try {
			if (constant instanceof ExpressionAST) {
				return new Expression(scope,ExpressionMaker.EQUAL(proxy, ((ExpressionAST)constant)));
			} else {
				return new Expression(scope,ExpressionMaker.EQUAL(proxy, ExpressionMaker.CONSTANT(constant)));
			}
		} catch (ScopeException e) {
			throw new SQLScopeException(e);
		}
	}
	
	public Expression min() {
		return new Expression(scope,ExpressionMaker.MIN(proxy));
	}
	
	public Expression max() {
		return new Expression(scope,ExpressionMaker.MAX(proxy));
	}

	public Expression lessOrEqual(ExpressionAST value) {
		return new Expression(scope,ExpressionMaker.LESSOREQUAL(proxy, value));
	}

	public Expression between(ExpressionAST start, ExpressionAST end) {
		return new Expression(scope, 
				ExpressionMaker.AND(
						ExpressionMaker.GREATEROREQUAL(start, proxy),
						ExpressionMaker.LESSOREQUAL(proxy, end)));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((proxy == null) ? 0 : proxy.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expression other = (Expression) obj;
		if (proxy == null) {
			if (other.proxy != null)
				return false;
		} else if (!proxy.equals(other.proxy))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}

}
