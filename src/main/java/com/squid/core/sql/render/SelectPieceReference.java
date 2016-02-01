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

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.sql.model.IAlias;

/**
 * this is a reference to a SelectPiece contained inside a scope (sub-select for example)
 * @author sergefantino
 *
 */
public class SelectPieceReference 
implements IPiece, ITypedPiece
{
	
	private IAlias scope;
	private ISelectPiece select;

	public SelectPieceReference(IAlias scope, ISelectPiece select) {
		this.scope = scope;
		this.select = select;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		if (scope!=null) {
			return skin.quoteIdentifier(scope.getAlias())+"."+skin.quoteIdentifier(select.getAlias());
		} else {
			return skin.quoteIdentifier(select.getAlias());
		}
	}
	
	@Override
	public ExtendedType getType() {
		// make sure not to propagate a meta domain here
		return new ExtendedType(IDomain.INTRINSIC, ExtendedType.UNDEFINED.getDataType());
	}

}
