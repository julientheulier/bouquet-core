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
package com.squid.core.expression.reference;

import com.squid.core.database.model.ForeignKey;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.expression.ExpressionRef;
import com.squid.core.expression.scope.IdentifierType;
import com.squid.core.sql.render.SQLSkin;

/**
 * ForeignKeyReference is just a plain reference of a fk in the context of defining a relation.
 * In that context the fk is a condition we apply to AxB where A,B are the foreign/primary tables
 * @author sergefantino
 *
 */
public class ForeignKeyReference extends ExpressionRef {
	
	private ForeignKey reference;
	
	public ForeignKeyReference(ForeignKey reference) {
		super();
		this.reference = reference;
	}

	@Override
	public ExtendedType computeType(SQLSkin skin){
		return IDomain.CONDITIONAL.computeType(skin);
	}

	@Override
	public IDomain getImageDomain() {
		// the fk is a condition
		return IDomain.CONDITIONAL;
	}

	@Override
	public IDomain getSourceDomain() {
		// source is a cross domain (pktable x fktable)
		if (reference!=null) return reference.getDomain();
		else return IDomain.UNKNOWN;
	}

	@Override
	public String getReferenceName() {
		return reference!=null?reference.getName():"";
	}
	
	@Override
	public String getReferenceIdentifier() {
		// natural name only
		return null;
	}
	
	@Override
	public IdentifierType getReferenceType() {
		// no type defined yet...
		return null;
	}
	
	@Override
	public Object getReference() {
		return reference;
	}
	
	public ForeignKey getForeignKey() {
		return reference;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((reference == null) ? 0 : reference.hashCode());
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
		ForeignKeyReference other = (ForeignKeyReference) obj;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

}
