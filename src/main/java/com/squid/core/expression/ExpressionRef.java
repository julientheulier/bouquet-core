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

import com.squid.core.expression.PrettyPrintOptions.ReferenceStyle;
import com.squid.core.expression.scope.IdentifierType;

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
	 * return the reference canonical identifier expression, or null if there is no canonical identifier.
	 * If the identifier needs a prefix type, must override getReferenceType()
	 * 
	 * @return
	 */
	public abstract String getReferenceIdentifier();
	
	/**
	 * return the reference identifierType as supported by the parser when using explicit reference, e.g. [type:'reference']
	 * @return
	 */
	public abstract IdentifierType getReferenceType();
	
	/**
	 * return the expression equivalent to the reference using an identifier and a prefix if defined
	 * @return
	 */
	public String prettyPrintIdentifier() {
		if (getReferenceIdentifier()==null) {
			// use the name
			return PrettyPrintConstant.OPEN_IDENT+getReferenceName()+PrettyPrintConstant.CLOSE_IDENT;
		} else {
			if (getReferenceType()!=null) {
				return PrettyPrintConstant.OPEN_TYPED_IDENTIFIER+getReferenceType()+PrettyPrintConstant.TYPE_SEPARATOR+PrettyPrintConstant.OPEN_IDENT+getReferenceIdentifier()+PrettyPrintConstant.CLOSE_IDENT;
			} else {
				return PrettyPrintConstant.OPEN_TYPED_IDENTIFIER+getReferenceType()+PrettyPrintConstant.TYPE_SEPARATOR+PrettyPrintConstant.OPEN_IDENT+getReferenceIdentifier()+PrettyPrintConstant.CLOSE_IDENT;
			}
		}
	}
	
	@Override
	public String prettyPrint(PrettyPrintOptions options) {
		if (options==null || options.getStyle()==ReferenceStyle.LEGACY || (options.getStyle()==ReferenceStyle.NAME && !options.isExplicitType())) {
			return PrettyPrintConstant.OPEN_IDENT+getReferenceName()+PrettyPrintConstant.CLOSE_IDENT;
		}
		if (options.getStyle()==ReferenceStyle.IDENTIFIER && getReferenceIdentifier()!=null) {
			return getReferenceIdentifier();
		}
		if (options.isExplicitType() && getReferenceType()!=null) {
			if (options.getStyle()==ReferenceStyle.NAME || getReferenceIdentifier()==null) {
				return PrettyPrintConstant.OPEN_TYPED_IDENTIFIER+getReferenceType()+PrettyPrintConstant.TYPE_SEPARATOR+PrettyPrintConstant.OPEN_IDENT+getReferenceName()+PrettyPrintConstant.CLOSE_IDENT;
			}
			if (options.getStyle()==ReferenceStyle.IDENTIFIER) {
				return PrettyPrintConstant.OPEN_TYPED_IDENTIFIER+getReferenceType()+PrettyPrintConstant.TYPE_SEPARATOR+PrettyPrintConstant.OPEN_IDENT+getReferenceIdentifier()+PrettyPrintConstant.CLOSE_IDENT;
			}
		}
		// default == legacy
		return PrettyPrintConstant.OPEN_IDENT+getReferenceName()+PrettyPrintConstant.CLOSE_IDENT;
	}
	
	@Override
	public String toString() {
		return prettyPrint();
	}

}
