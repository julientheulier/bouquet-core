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

import com.squid.core.database.model.Table;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.expression.ExpressionRef;
import com.squid.core.expression.PrettyPrintConstant;
import com.squid.core.expression.scope.IdentifierType;
import com.squid.core.sql.render.SQLSkin;

/**
 * this is a reference to a Table Object
 * @author sfantino
 *
 */
public class TableReference extends ExpressionRef {
	
	private Table reference;
	
	public TableReference(Table reference) {
		super();
		this.reference = reference;
	}


	@Override
	public ExtendedType computeType(SQLSkin skin){
		 if(this.reference!=null){
			 return this.reference.getDomain().computeType(skin);
		 }else{
			 return IDomain.UNKNOWN.computeType(skin);
		 }
	}

	@Override
	public IDomain getImageDomain() {
		return this.reference!=null?this.reference.getDomain():IDomain.UNKNOWN;
	}

	@Override
	public IDomain getSourceDomain() {
		return IDomain.NULL;
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
	
	@Override
	public String prettyPrint() {
		return PrettyPrintConstant.OPEN_TYPED_IDENTIFIER+
				IdentifierType.TABLE+PrettyPrintConstant.TYPE_SEPARATOR+PrettyPrintConstant.OPEN_IDENT+
				getReferenceName()+
				PrettyPrintConstant.CLOSE_IDENT+
				PrettyPrintConstant.CLOSE_TYPED_IDENTIFIER;
	}

	/**
	 */
	public Table getTable() {
		return this.reference;
	}

	public Object getReference() {
		return getTable();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof TableReference) {
			TableReference ref = (TableReference) obj;
			return this.getTable() != null
					&& this.getTable().equals(ref.getTable());
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.getTable() != null ? this.getTable().hashCode() : super
				.hashCode();
	}
	
	@Override
	public String toString() {
		if (this.reference!=null) {
			return "'"+this.reference.getName()+"'";
		} else {
			return "undefinedTableReference";
		}
	}


}
