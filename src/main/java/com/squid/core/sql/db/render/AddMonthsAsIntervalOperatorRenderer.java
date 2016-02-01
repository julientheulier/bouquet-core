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

import java.sql.Types;

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.extensions.DateOperatorDefinition;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SimpleConstantValuePiece;
/**
 * This class is to implement ADD_MONTHS as an interval when not supported by a database vendor
 * This is for MySQL and Postgres
 * @author jtheulier
 *
 */
public class AddMonthsAsIntervalOperatorRenderer extends BaseOperatorRenderer {

	public AddMonthsAsIntervalOperatorRenderer() {
	}

	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		throw new RenderingException("invalid arguments for "+opDef.getName()+"() operator");
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		//
		// extract the constant value for args[1]
		if (piece.getParamTypes()==null || piece.getParamTypes().length<2) {
			throw new RenderingException("invalid arguments for "+opDef.getName()+"() operator");
		}
		IDomain args1_type = piece.getParamTypes()[1].getDomain();
		if (!args1_type.isInstanceOf(DomainNumericConstant.DOMAIN)) {
			throw new RenderingException("invalid argument '"+args[1]+"' for "+opDef.getName()+"() operator at position 1: must be a integer constant");
		}
		Double value = ((DomainNumericConstant)args1_type).getValue();
		if (value==null) {
			throw new RenderingException("invalid argument '"+args[1]+"' for "+opDef.getName()+"() operator at position 1: invalid integer constant");
		}
		long addMonths = Math.round(value);
		if (addMonths!=value) {
			throw new RenderingException("invalid argument '"+args[1]+"' for "+opDef.getName()+"() operator at position 1: invalid integer constant");
		}
		
		String[] newArgs = new String[3];
		newArgs[0] = args[0];
		DateOperatorDefinition dod = null;
		if (addMonths<0) {
			dod = new DateOperatorDefinition("DATE_SUB",DateOperatorDefinition.DATE_SUB,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
			newArgs[1] = new Long(-addMonths).toString();
		} else {
			dod = new DateOperatorDefinition("DATE_ADD",DateOperatorDefinition.DATE_ADD,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
			newArgs[1] = args[1];
		}
		DomainNumericConstant valueDomain = new DomainNumericConstant(new Double(newArgs[1]).doubleValue());
		IPiece piece1 =  new SimpleConstantValuePiece(newArgs[1], valueDomain);
		newArgs[2] = "MONTH";
		DomainStringConstant unitDomain = new DomainStringConstant(newArgs[2]);
		IPiece piece2 =  new SimpleConstantValuePiece(newArgs[2], unitDomain);
		OperatorPiece intervalPiece = new OperatorPiece(dod, new IPiece[] {piece.getParams()[0], piece1, piece2}, new ExtendedType[] {getExtendedPieces(piece)[0], new ExtendedType(valueDomain, Types.INTEGER, 0, 11), new ExtendedType(unitDomain, Types.VARCHAR, 0, newArgs[2].length())} );
		return skin.render(skin, intervalPiece, dod, newArgs);
	}

}
