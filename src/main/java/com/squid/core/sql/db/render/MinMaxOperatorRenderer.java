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

import java.io.IOException;

import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.vector.VectorOperatorDefinition;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * Support MIN|MAX(VECTOR()) syntax
 * @author sfantino
 *
 */
public class MinMaxOperatorRenderer 
extends OrderedAnalyticOperatorRenderer
{

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		//
		if (args.length==1) {
			IPiece param = piece.getParams()[0];
			if (param instanceof OperatorPiece) {
				OperatorPiece op = (OperatorPiece)param;
				if (op.getOpDef().getExtendedID().equals(VectorOperatorDefinition.ID)) {
					// ok, we found a vector parameter
					IPiece[] params = op.getParams();// iter through the vector
					if (params.length>1) {
						try {
							return prettyPrint(skin,opDef.getId(),params);
						} catch (IOException e) {
							throw new RenderingException("invalid use of MIN/MAX");
						}
					} else {
						throw new RenderingException("invalid use of MIN/MAX");
					}
				}
			}
		} 
		//else
		return super.prettyPrint(skin, piece, opDef, args);
	}

	private String prettyPrint(SQLSkin skin, int operatorID, IPiece[] params) throws RenderingException, IOException {
		String op = "";
		switch (operatorID) {
		case IntrinsicOperators.MIN:
			op = "<=";break;
		case IntrinsicOperators.MAX:
			op = ">=";break;
		default:
			throw new RenderingException("invalid use of MIN/MAX");
		}
		//
		String[] elements = new String[params.length];
		for (int i=0;i<params.length;i++) {
			elements[i] = params[i].render(skin);
		}
		String result = "(CASE";
		for (int i=0;i<params.length-1;i++) {
			result += " WHEN "+prettyPrint(skin, op, elements, i)+" THEN "+elements[i];
		}
		// else
		result += " ELSE "+elements[params.length-1]+" END)";
		return result;
	}
	
	private String prettyPrint(SQLSkin skin, String op, String[] elements, int x) {
		String result = "";
		for (int i=0;i<elements.length;i++) {
			if (i!=x) {
				if (result!="") result += " AND ";
				result += "("+elements[x]+")"+op+"("+elements[i]+")";
			}
		}
		return result;
	}
	
}
