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

import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.vector.VectorOperatorDefinition;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SQLTokenConstant;

/**
 * Ticket #1410: Implement IN feature by using vector domain
 * @author Nha
 *
 */
public class InOperatorRenderer extends BaseOperatorRenderer {

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		throw new RenderingException("not supported");
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		if (args.length==2) {
			IPiece param = piece.getParams()[1];
			if (param instanceof OperatorPiece) {
				OperatorPiece op = (OperatorPiece)param;
				if (op.getOpDef().getExtendedID()==VectorOperatorDefinition.ID) {
					// ok, we found a vector parameter
					IPiece[] params = op.getParams();
					if (params.length >= 1) {
						try {
							String result = piece.getParams()[0].render(skin);
							result += prettyPrint(skin,opDef.getId(),params);
							return result;
						} catch (IOException e) {
							throw new RenderingException("invalid use of IN operator");
						}
					}
				}
			}
		}
		throw new RenderingException("invalid use of IN operator");
	}

	private String prettyPrint(SQLSkin skin, int operatorID, IPiece[] params) throws RenderingException, IOException {
		String result = " " + skin.getToken(SQLTokenConstant.IN) + " (";
		for (int i = 0; i < params.length; i++) {
			result += params[i].render(skin);
			if (i < params.length - 1) {
				result += ",";
			}
		}
		result += ")";
		//
		return result;
	}
	
}
