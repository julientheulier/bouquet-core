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

/**
 * This is a special case of a null expression (an expression with no value) to handle softly case where an expression is not valid (cannot be parsed).
 * The UndefinedExpression can be used to wrap the invalid definition and the error message associated.
 * 
 * @author sergefantino
 *
 */
public class UndefinedExpression extends NullExpression
{
	
	private String malformedValue = "";
	private String errorMessage = null;
	
	public UndefinedExpression(String malformedValue) {
		this.malformedValue = malformedValue;
	}

	public UndefinedExpression(String malformedValue, String errorMessage) {
		super();
		this.malformedValue = malformedValue;
		this.errorMessage = errorMessage;
	}

	public String getMalformedValue() {
		return malformedValue;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public int hashCode() {
		return ((malformedValue == null) ? 0 : malformedValue.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UndefinedExpression other = (UndefinedExpression) obj;
		if (malformedValue == null) {
			if (other.malformedValue != null)
				return false;
		} else if (!malformedValue.equals(other.malformedValue))
			return false;
		return true;
	}
	
	@Override
	public String prettyPrint() {
		// just return the original expression
		return malformedValue;
	}
	
	@Override
	public String toString() {
		return "UNDEFINED(" + malformedValue + ")";
	}

}
