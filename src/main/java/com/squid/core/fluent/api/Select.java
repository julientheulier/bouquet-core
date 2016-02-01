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

public interface Select {
	
	public Select WHERE(ExpressionAST condition) throws SQLScopeException;
	
	public Select JOIN(String fkName) throws SQLScopeException;
	
	public Select GROUPBY(ExpressionAST... expressions) throws SQLScopeException;
	
	public Expression SELECT(String colName) throws SQLScopeException;
	
	public SelectImpl apply(ExpressionAST... expressions) throws SQLScopeException;
	
	public IDomain getDomain();

	/**
	 * return the inferred PK
	 * @return
	 */
	public ExpressionAST[] PK();

	public Select EXISTS(Select exists);

}
