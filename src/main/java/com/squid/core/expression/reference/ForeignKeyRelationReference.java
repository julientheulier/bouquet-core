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
import com.squid.core.database.model.Table;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.set.SetDomain;
import com.squid.core.expression.ExpressionRef;
import com.squid.core.expression.scope.IdentifierType;
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.render.SQLSkin;

/**
 * A ForeignKeyRelationReference allow to apply directly a foreignKey to the source domain and navigate to the image domain.
 * So if the fk is define on AXB and the FKRelationReference is applied to A then image is B.
 * If applied to B, the image is A (it's ok to navigate both way)
 * @author sergefantino
 *
 */
public class ForeignKeyRelationReference extends ExpressionRef {
	
	private ForeignKey reference;
	private Table source;
	private Table image;
	private RelationDirection direction;
	
	/**
	 * apply the fk to the source table
	 * @param fk
	 * @param source
	 * @throws ScopeException 
	 */
	public ForeignKeyRelationReference(ForeignKey fk, Table source) throws ScopeException {
		super();
		this.reference = fk;
		this.source = source;
		if (source.equals(fk.getForeignTable())) {
			this.image = fk.getPrimaryTable();
			this.direction = RelationDirection.LEFT_TO_RIGHT;
		} else if (source.equals(fk.getPrimaryTable())) {
			this.image = fk.getForeignTable();
			this.direction = RelationDirection.RIGHT_TO_LEFT;
		} else {
			throw new ScopeException("cannot apply foreignKey '"+fk.getName()+"' to table '"+source+"'");
		}
	}
	
	public RelationDirection getDirection() {
		return this.direction;
	}

	@Override
	public ExtendedType computeType(SQLSkin skin){
		if (this.direction==RelationDirection.LEFT_TO_RIGHT) {
			return this.image.getDomain().computeType(skin);
		} else {
			return SetDomain.MANAGER.getDomainSet(this.image.getDomain()).computeType(skin);
		}
	}

	@Override
	public IDomain getImageDomain() {
		if (this.direction==RelationDirection.LEFT_TO_RIGHT) {
			return this.image.getDomain();
		} else {
			return SetDomain.MANAGER.getDomainSet(this.image.getDomain());
		}
	}
	
	public Table getImageTable() {
		return this.image;
	}

	@Override
	public IDomain getSourceDomain() {
		return this.source.getDomain();
	}
	
	public Table getSourceTable() {
		return this.source;
	}
	
	@Override
	public String getReferenceName() {
		return this.reference.getName();
	}
	
	@Override
	public String getReferenceIdentifier() {
		return null;
	}
	@Override
	public IdentifierType getReferenceType() {
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
				+ ((direction == null) ? 0 : direction.hashCode());
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
		ForeignKeyRelationReference other = (ForeignKeyRelationReference) obj;
		if (direction != other.direction)
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}
	
}
