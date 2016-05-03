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

import com.squid.core.domain.extensions.cast.*;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * IMPORTANT NOTE: if you extend this class, check that the rendering of a addition or substraction of a date
 * with an interval in hour, minute, second is correctly casted in a timestamp
 * @author jtheulier
 *
 */
public class CastOperatorRenderer
extends BaseOperatorRenderer {

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		if (args.length==1) {
			return prettyPrintSingleArg(skin, opDef, piece, args);
		} else if (args.length==2) {
			return prettyPrintTwoArgs(skin, piece, opDef, args);
		} else {
			if (CastToNumberOperatorDefinition.ID.equals(opDef.getExtendedID())) {
				return prettyPrintSingleArg(skin, opDef, piece, args);
			} else {
				throw new RenderingException("Invalid operator " +  opDef.getSymbol());
			}
		}
	}

	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		return prettyPrint(skin, null, opDef, args);
	}

	protected String prettyPrintSingleArg(SQLSkin skin, OperatorDefinition opDef, OperatorPiece piece, String[] args) throws RenderingException {
		String txt = "CAST(";
		txt += args[0] + " AS ";
		if (CastToTimestampOperatorDefinition.ID.equals(opDef.getExtendedID())) {
			txt +="TIMESTAMP)";
		} else if (CastToDateOperatorDefinition.ID.equals(opDef.getExtendedID())){
			txt +="DATE)";
		} else if (CastToCharOperatorDefinition.ID.equals(opDef.getExtendedID())){
			txt +="VARCHAR(";
			txt+=((CastOperatorDefinition)opDef).getPieceLength(getExtendedPieces(piece))+"))";
		} else if (CastToNumberOperatorDefinition.ID.equals(opDef.getExtendedID())){
			if (args.length==1) {
				txt +="NUMERIC)";
			} else if (args.length==3) {
				txt +="DECIMAL("+args[1]+","+args[2]+"))";
			}
		} else if (CastToIntegerOperatorDefinition.ID.equals(opDef.getExtendedID())){
			txt +="INTEGER)";
		}
		return txt;
	}

	protected String prettyPrintTwoArgs(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef, String[] args) throws RenderingException {
		return opDef.getSymbol()+"("+args[0]+","+args[1].replaceAll("HH","HH24")+")";
	}

}
