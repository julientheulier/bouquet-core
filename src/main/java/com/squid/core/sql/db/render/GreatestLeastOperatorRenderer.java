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

import com.squid.core.domain.maths.GreatestLeastOperatorDefinition;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * Implements LEAST & GREATEST using CASE expression on platform that does not support it (i.e. not ORACLE)
 * This is the same implementation then MIN({vecotr})
 * @author sfantino
 *
 */
public class GreatestLeastOperatorRenderer extends BaseOperatorRenderer {

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		String op = "";
		if (opDef.getExtendedID().equals(GreatestLeastOperatorDefinition.LEAST)) {
			op = "<=";
		} else if (opDef.getExtendedID().equals(GreatestLeastOperatorDefinition.GREATEST)) {
			op = ">=";
		} else {
			throw new RenderingException("invalid use of LEAST/GREATEST");
		}
		//
		String result = "(CASE";
		for (int i=0;i<args.length-1;i++) {
			result += " WHEN "+prettyPrint(skin, op, args, i)+" THEN "+args[i];
		}
		// else
		result += " ELSE "+args[args.length-1]+" END)";
		return result;
	}
	
	private String prettyPrint(SQLSkin skin, String op, String[] elements, int x) {
		String result = "";
		for (int i=0;i<elements.length;i++) {
			if (i!=x) {
				if (result!="") {
					result += " AND ";
				}
				result += "("+elements[x]+op+elements[i]+" OR "+ elements[i]+" IS NULL)";
			}
		}
		return result;
	}

}
