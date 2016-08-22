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

import com.squid.core.expression.PrettyPrintConstant;

public class IdentifierType {

	public static final IdentifierType DEFAULT = new IdentifierType();
	
	// a column-reference is prefixed with #
	public static final IdentifierType COLUMN = new IdentifierType("col");

	// a identifier-reference is prefixed with @
	public static final IdentifierType IDENTIFIER = new IdentifierType("id");

	// a parameter-reference is prefixed with $
	public static final IdentifierType PARAMETER = new IdentifierType(PrettyPrintConstant.PARAMETER_TAG);
	
	private String token = null;
	
	private IdentifierType() {
	}
	
	public IdentifierType(String token) {
		this.token = token;
		IdentifierTypeManager.INSTANCE.register(this,token);
	}

	public String getToken() {
		return token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
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
		IdentifierType other = (IdentifierType) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return token!=null?token:"";
	}

}
