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
package com.squid.core.sql.render;

import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;

/**
 * A piece that can make a reference to a selected piece from a sub-query
 * @author sfantino
 *
 */
public class SubSelectReferencePiece 
implements IPiece, ITypedPiece
{
	
	private IFromPiece from;
	private ISelectPiece select;

	/**
	 * create a reference to the 'select' alias from the 'from' piece
	 * @param from
	 * @param select
	 */
	public SubSelectReferencePiece(IFromPiece from, ISelectPiece select) {
		this.from = from;
		this.select = select;
	}

	public IFromPiece getFrom() {
		return from;
	}

	public ISelectPiece getSelect() {
		return select;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		return this.from.getAlias()+"."+skin.quoteIdentifier(this.select.getAlias());
	}

	@Override
	public ExtendedType getType() {
		IPiece inner = this.select.getSelect();
		if (inner instanceof ITypedPiece) {
			ExtendedType type = ((ITypedPiece)inner).getType();
			if (type.getDomain().isInstanceOf(AggregateDomain.DOMAIN)) {
				// need to hide that the domain is aggregate
				return new ExtendedType(((AggregateDomain)type.getDomain()).getSubdomain(), type);
			} else {
				return type;
			}
		} else {
			return ExtendedType.UNDEFINED;
		}
	}
	
	//@Override
	public String getAlias() {
		return this.select.getAlias();
	}

	//@Override
	public void setComment(String comment) {
		// TODO Auto-generated method stub
		
	}

}
