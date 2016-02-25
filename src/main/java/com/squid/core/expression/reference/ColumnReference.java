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

import com.squid.core.database.model.Column;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.expression.ExpressionRef;
import com.squid.core.expression.PrettyPrintConstant;
import com.squid.core.sql.render.SQLSkin;

/**
 * this is a reference to a column object, in the context of a given Domain.
 * We need to keep the reference to the domain in order to compute the correct source-domain.
 * @author sfantino
 *
 */
public class ColumnReference extends ExpressionRef {
	
//	private Domain domain;
	private IDomain source = IDomain.UNKNOWN;
	private Column reference;
	
	public ColumnReference(Column reference) {
		super();
		this.reference = reference;
		this.source = reference.getTable()!=null?reference.getTable().getDomain():IDomain.UNKNOWN;
	}

	/**
	 * protected constructor to allow overriding default domain
	 * @param reference
	 * @param domain
	 */
    protected ColumnReference(Column reference, IDomain source) {
        super();
        this.reference = reference;
        this.source = source;
    }

	@Override
	public ExtendedType computeType(SQLSkin skin){
		if (getColumn() != null) {
			return getColumn().getType();
		} else {
			return IDomain.UNKNOWN.computeType(skin);
		}
	}

	@Override
	public IDomain getSourceDomain() {
		return source;
	}

	@Override
	public IDomain getImageDomain() {
		if (getColumn() != null) {
			return getColumn().getTypeDomain();
		} else {
			return IDomain.UNKNOWN;
		}
	}
	
	@Override
	public String getReferenceName() {
		if (reference!=null) {
			return reference.getName();
		} else {
			return "";
		}
	}
	
	@Override
	public String getReferenceIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 */
	public Column getColumn() {
		return this.reference;
	}

	public Object getReference() {
		return getColumn();
	}
	
	@Override
	public String prettyPrint() {
		return PrettyPrintConstant.COLUMN_TAG+PrettyPrintConstant.OPEN_IDENT+getReferenceName()+PrettyPrintConstant.CLOSE_IDENT;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof ColumnReference) {
			ColumnReference ref = (ColumnReference) obj;
			return this.getColumn() != null
					&& this.getColumn().equals(ref.getColumn());
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.getColumn() != null ? this.getColumn().hashCode() : super
				.hashCode();
	}
	
	@Override
	public String toString() {
		if (this.reference!=null) {
			return "'"+this.reference.getName()+"'";
		} else {
			return "undefinedColumnReference";
		}
	}

}
