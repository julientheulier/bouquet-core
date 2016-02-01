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

import com.squid.core.domain.IDomain;
import com.squid.core.domain.analytics.WindowingDomain;
import com.squid.core.domain.analytics.WindowingStaticExpression;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

public class RowsOperatorRenderer 
extends BaseOperatorRenderer
{

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		if (args.length==0) {
			return "";
		} else if (args.length==1) {
			IDomain d = piece.getDomains()[0];
			if (d.isInstanceOf(WindowingDomain.DOMAIN)) {
				WindowingStaticExpression e = ((WindowingDomain)d).getExpression();
				switch (e.getType()) {
				case UNBOUNDED:
					return "ROWS UNBOUNDED PRECEDING";
				case CURRENT:
					return "ROWS CURRENT ROW";
				case PRECEDING:
					return "ROWS "+e.getRowCount()+" PRECEDING";
				default:
					// error
				}
			}
		} else if (args.length==2) {
			String result =  "ROWS BETWEEN ";
			IDomain d1 = piece.getDomains()[0];
			IDomain d2 = piece.getDomains()[1];
			if (d1.isInstanceOf(WindowingDomain.DOMAIN) && d2.isInstanceOf(WindowingDomain.DOMAIN)) {
				WindowingStaticExpression e1 = ((WindowingDomain)d1).getExpression();
				WindowingStaticExpression e2 = ((WindowingDomain)d2).getExpression();
				switch (e1.getType()) {
				case UNBOUNDED:
					result += " UNBOUNDED PRECEDING";
					break;
				case CURRENT:
					result += " CURRENT ROW";
					break;
				case PRECEDING:
					result += e1.getRowCount()+" PRECEDING";
					break;
				case FOLLOWING:
					result += e1.getRowCount()+" FOLLOWING";
					break;
				default:
					// error
					return "**invalid window specification**";
				}
				result += " AND ";
				switch (e2.getType()) {
				case UNBOUNDED:
					result += " UNBOUNDED FOLLOWING";
					break;
				case CURRENT:
					result += " CURRENT ROW";
					break;
				case PRECEDING:
					result += e2.getRowCount()+" PRECEDING";
					break;
				case FOLLOWING:
					result += e2.getRowCount()+" FOLLOWING";
					break;
				default:
					// error
					return "**invalid window specification**";
				}
				return result;
			}
		}
		//else
		return "**invalid window specification**";
	}

}
