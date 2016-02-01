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
import com.squid.core.expression.ExpressionAST;
import com.squid.core.fluent.imp.SelectImpl;
import com.squid.core.sql.model.SQLScopeException;

public class SelectWhere extends SelectBase {
	
	private Select base;
	private ExpressionAST condition;
	
	public SelectWhere(Select base, ExpressionAST condition) throws SQLScopeException {
		super();
		this.base = base;
		this.condition = condition;
		if (!this.base.getDomain().isInstanceOf(this.condition.getSourceDomain())) {
			throw new SQLScopeException("cannot apply the condition to the select, domains mismatch");
		}
	}
	
	@Override
	public ExpressionAST[] PK() {
		return base.PK();
	}

	@Override
	public Select JOIN(String fkName) throws SQLScopeException {
		throw new RuntimeException("NYI");
	}

	@Override
	public Expression SELECT(String colName) throws SQLScopeException {
		return base.SELECT(colName);
	}

	@Override
	public SelectImpl apply(ExpressionAST... expressions)
			throws SQLScopeException {
		SelectImpl select = this.base.apply(expressions);
		select.where(condition);
		return select;
	}

	@Override
	public IDomain getDomain() {
		throw new RuntimeException("NYI");
	}

}
