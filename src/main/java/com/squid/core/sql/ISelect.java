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

import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.IFromPiece;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.statements.SelectStatement;

public interface ISelect {

	Scope getScope();

	IPiece createPiece(Context ctx, Scope scope, ExpressionAST expression) throws SQLScopeException, ScopeException;
	
	/**
	 * 
	 * @param ctx
	 * @param scope
	 * @param expression
	 * @param mapping is the expression source domain mapping. In case this is a sub-select, that may allow to inject some definition on the fly (column for instance)
	 * @return
	 * @throws SQLScopeException
	 * @throws ScopeException
	 */
	IPiece createPiece(Context ctx, Scope scope, ExpressionAST expression, Object mapping) throws SQLScopeException, ScopeException;

	SelectStatement getStatement();

	GroupingInterface getGrouping();
	
	IFromPiece from(Context ctx, Scope subscope, ExpressionAST segment) throws SQLScopeException, ScopeException;

}
