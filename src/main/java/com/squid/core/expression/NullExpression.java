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

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.sql.render.SQLSkin;

public class NullExpression extends NamedExpression 
implements ConstantValue {

	@Override
	public ExtendedType computeType(SQLSkin skin) {
		return ExtendedType.NULL;
	}
	
	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public IDomain getImageDomain() {
		return IDomain.NULL;// null by itself
	}

	@Override
	public IDomain getSourceDomain() {
		return IDomain.NULL;// this is a constant
	}
	
	@Override
	public String prettyPrint(PrettyPrintOptions options) {
		return "NULL";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		// else
		return true;
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return prettyPrint();
	}

}
