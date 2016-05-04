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
import java.util.Calendar;

import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.analytics.WindowingDomain;
import com.squid.core.domain.extensions.cast.CastOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateOperatorDefinition;
import com.squid.core.domain.extensions.cast.CastToTimestampOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateIntervalOperatorDefinition;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.sort.DomainSort;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SimpleConstantValuePiece;

public class OrderedAnalyticOperatorRenderer 
extends BaseOperatorRenderer
{
	
	public OrderedAnalyticOperatorRenderer() {
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
		String result = piece.getOpDef().getSymbol();
		ExtendedType[] types = piece.getParamTypes();
		if (args.length==0) {
			result += "()";
		} else if (args.length==1) {
			result += "("+args[0]+")";
		} else {
			result += "("+args[0]+")";
		}
		if (args.length>1) {
			result += " OVER (";
			int mode = 0;
			for (int i=1;i<args.length;i++) {
				boolean needComa = true;
				if (types[i].getDomain().isInstanceOf(DomainSort.DOMAIN)) {
					mode=2;
					result += " ORDER BY ";
					needComa = false;
				}
				else if (types[i].getDomain().isInstanceOf(WindowingDomain.DOMAIN)) {
					mode=3;
					result += " ";// ROWS will be added by the ROWS() operator
					needComa = false;
				}
				else if (mode==0) {
					mode=1;
					result += "PARTITION BY ";
					needComa = false;
				}
				if (needComa) {
					result += ",";
				}
				result += args[i];
			}
			result += ")";
		}
		return result;
	}

	public String getLocalEpoch(SQLSkin skin, IPiece piece, ExtendedType type,
			String arg) throws RenderingException {
		DateOperatorDefinition dateInterval = new DateOperatorDefinition("DATE_INTERVAL", DateIntervalOperatorDefinition.ID,IDomain.NUMERIC, OperatorDefinition.DATE_TIME_TYPE);
		
		String intervalType = "SECOND";
		Calendar cal = Calendar.getInstance();
		cal.set(1970, 1, 1, 0, 0, 0);
		SimpleConstantValuePiece refDatePiece = new SimpleConstantValuePiece("1970-01-01" ,IDomain.STRING);
		String dateRef = getTimestamp(skin,refDatePiece,"'1970-01-01'");

		SimpleConstantValuePiece intervalTypePiece = new SimpleConstantValuePiece(intervalType,IDomain.STRING);
		DomainStringConstant dsc = new DomainStringConstant(intervalType);
		ExtendedType intevalTypeDomain = new ExtendedType(dsc,Types.VARCHAR,0,intervalType.length());
		OperatorPiece intervalPiece = new OperatorPiece(dateInterval, new IPiece[] {piece,refDatePiece,intervalTypePiece},  new ExtendedType[] {type, ExtendedType.TIMESTAMP, intevalTypeDomain});
		return skin.render(skin, intervalPiece, dateInterval, new String[] {arg, dateRef,intervalType});

	}
	
	public String getTimestamp(SQLSkin skin, IPiece piece, String date) throws RenderingException {
		CastOperatorDefinition toTimestamp= new CastOperatorDefinition("TO_TIMESTAMP", CastToTimestampOperatorDefinition.ID,IDomain.TIMESTAMP, OperatorDefinition.DATE_TIME_TYPE);
		OperatorPiece castPiece = new OperatorPiece(toTimestamp, new IPiece[]{piece}, new ExtendedType[] {ExtendedType.STRING});
		return skin.render(skin, castPiece, toTimestamp, new String[] {date});

	}
}
