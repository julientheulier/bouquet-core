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

import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.vector.VectorOperatorDefinition;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * Support AVG(VECTOR()) syntax
 * @author sfantino
 *
 */
public class AverageOperatorRenderer 
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
				if (op.getOpDef().getExtendedID()==VectorOperatorDefinition.ID) {
					// ok, we found a vector parameter
					IPiece[] params = op.getParams();// iter through the vector
					String pp = "";
					String dd = "";
					int s = params.length;
					for (IPiece p : params) {
						String prender = p.render(skin);
						// pp: add the value if it is not NULL to avoid NULL result
						// dd: only count the value if it is NOT NULL == ignore NULL value in the mean
						pp += "(COALESCE("+prender+",0))";
						dd += "(CASE WHEN ("+prender+") IS NULL THEN 0 ELSE 1 END)";
						if (--s>0) {
							pp += "+";
							dd += "+";
						}
					}
					return "(CAST(("+pp+") AS FLOAT)/("+dd+"))";
				}
			}
		} 
		//else
		return super.prettyPrint(skin, piece, opDef, args);
	}
	
}
