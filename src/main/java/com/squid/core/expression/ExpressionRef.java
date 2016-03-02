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
package com.squid.core.expression;

public abstract class ExpressionRef extends NamedExpression implements ExpressionLeaf {
	
	// this is the token position as provided by the parser
	private TokenPosition tokenPosition = null;
	
	public TokenPosition getTokenPosition() {
		return tokenPosition;
	}
	
	public void setTokenPosition(TokenPosition identifier) {
		this.tokenPosition = identifier;
	}
	
	/**
	 * return the reference object
	 * @return
	 */
	public abstract Object getReference();
	
	/**
	 * return the reference name
	 * @return
	 */
	public abstract String getReferenceName();
	
	/**
	 * return the reference identifier expression, or null if there is no standard identifier expression.
	 * A valid reference identifier expression is for example: @'someID' or @'someREf'.@'someId'
	 * 
	 * @return
	 */
	public abstract String getReferenceIdentifier();
	
	@Override
	public String prettyPrint() {
		return PrettyPrintConstant.OPEN_IDENT+getReferenceName()+PrettyPrintConstant.CLOSE_IDENT;
	}
	
	@Override
	public String toString() {
		return prettyPrint();
	}

}
