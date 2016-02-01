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
import com.squid.core.domain.sort.DomainSort;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

public class RankOperatorRenderer 
extends BaseOperatorRenderer
{
	
	public RankOperatorRenderer() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		throw new RenderingException("not supported");
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		String result = opDef.getSymbol()+"()";
		ExtendedType[] types = piece.getParamTypes();
		if (args.length>0) {
			result += " OVER (";
			int mode = 0;
			for (int i=0;i<args.length;i++) {
				boolean needComa = true;
				if (mode==0) {
					if (types[i].getDomain().isInstanceOf(DomainSort.SORT)) {
						mode=2;
						result += "ORDER BY ";
						needComa = false;
					} else {
						mode=1;
						result += "PARTITION BY ";
						needComa = false;
					}
				} else if (mode==1) {
					if (types[i].getDomain().isInstanceOf(DomainSort.SORT)) {
						mode=2;
						result += " ORDER BY ";
						needComa = false;
					}
				}
				if (needComa) result += ",";
				result += args[i];
			}
			result += ")";
		}
		return result;
	}

}
