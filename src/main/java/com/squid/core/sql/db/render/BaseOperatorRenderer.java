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
package com.squid.core.sql.db.render;

import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * The base implementation
 * @author Serge Fantino
 *
 */
public abstract class BaseOperatorRenderer
implements OperatorRenderer
{

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		// default is to use legacy
		return prettyPrint(skin, opDef, args);
	}

	/**
	 * Return the piece extended type
	 * @param piece
	 * @return
	 */
	protected ExtendedType[] getExtendedPieces(OperatorPiece piece) {
		if (piece!=null) {
			return piece.getParamTypes();
		} else {
			return null;
		}
		/*
		ExtendedType[] extendedTypes=null;
		if (piece!=null) {
			extendedTypes = new ExtendedType[piece.getParams().length];
			Object[] params = piece.getParams();
			for (int i=0; i<params.length; i++) {
				if (params[i] instanceof ITypedPiece) {
					extendedTypes[i] = ((ITypedPiece) params[i]).getType(); 
				}
			}
		}
		return extendedTypes;
		*/
	}

}
