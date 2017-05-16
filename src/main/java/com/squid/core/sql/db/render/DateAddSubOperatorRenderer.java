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

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.extensions.cast.CastOperatorDefinition;
import com.squid.core.domain.extensions.cast.CastToTimestampOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateOperatorDefinition;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * The SQL render for substracting/adding to a date another date or a constant
 * @author julien theulier
 *
 */
public class DateAddSubOperatorRenderer
extends BaseOperatorRenderer
{
	public static final String INTERVAL = "INTERVAL ";

	public enum OperatorType {
		ADD,SUB;
	}

	private OperatorType builtinType = null;

	public DateAddSubOperatorRenderer(OperatorType builtinType) {
		this.builtinType = builtinType;
	}

	protected OperatorType getBuiltinType() {
		return builtinType;
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		validateArgs(skin, opDef, args);
		String txt = "(";
		txt += getSqlCode(skin, piece, opDef, args, builtinType);
		txt += ")";
		return txt;
	}

	protected String getSqlCode(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args, OperatorType type) throws RenderingException {
		String txt ="";
		ExtendedType[] extendedTypes = null;
		extendedTypes = getExtendedPieces(piece);
		if (args.length==2) {
			if (extendedTypes[0].getDomain().isInstanceOf(IDomain.TIMESTAMP) && extendedTypes[1].getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
				txt += args[0] + getOperator(type);
				txt += args[1];
			} else if (extendedTypes[0].getDomain().isInstanceOf(IDomain.DATE)&& extendedTypes[1].getDomain().isInstanceOf(IDomain.DATE) && !extendedTypes[0].getDomain().isInstanceOf(IDomain.TIMESTAMP) && !extendedTypes[1].getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
				txt += getDate(args[0]) + getOperator(type);
				txt += getDate(args[1]);
			} else if (extendedTypes[0].getDomain().isInstanceOf(IDomain.DATE) && extendedTypes[0].getDomain().isInstanceOf(IDomain.TIMESTAMP)==false) {
				if (extendedTypes[1].getDomain().isInstanceOf(IDomain.INTERVAL)) {
					if (extendedTypes[1].getScale()==1 || extendedTypes[1].getScale()==2 || extendedTypes[1].getScale()==3) {
						txt += args[0] + getOperator(type);
					} else {
						txt +=  castDateAsTimestamp(skin, piece.getParams()[0], args[0]) + getOperator(type);
					}
					txt += args[1];
				} else if (extendedTypes[1].getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
					txt +=  castDateAsTimestamp(skin, piece.getParams()[0], args[0]) + getOperator(type);
					txt += args[1];
				} else if (extendedTypes[1].getDomain().isInstanceOf(IDomain.NUMERIC)) {
					txt += args[0] + getOperator(type);
					txt += args[1];
				} else {
					txt += args[0] + getOperator(type);
					txt += args[1];
				}
			} else if (extendedTypes[0].getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
				txt += args[0] + getOperator(type);
				if (extendedTypes[1].getDomain().isInstanceOf(IDomain.DATE) && extendedTypes[1].getDomain().isInstanceOf(IDomain.TIMESTAMP)==false) {
					txt += castDateAsTimestamp(skin, piece.getParams()[1], args[1]);
				} else {
					txt += args[1];
				}
			} else if (extendedTypes[0].getDomain().isInstanceOf(IDomain.INTERVAL) ){
				txt += args[0] + getOperator(type);
				txt += args[1];
			}
		} else {
			String mode = ((DomainStringConstant)extendedTypes[2].getDomain()).getValue().toUpperCase();
			int unit = new Double(((DomainNumericConstant)extendedTypes[1].getDomain()).getValue()).intValue();
			if (DateOperatorDefinition.periods.get(mode)==IDomain.TIMESTAMP && DateOperatorDefinition.periods.get(mode)!=extendedTypes[0].getDomain()) {
				txt += castDateAsTimestamp(skin, piece.getParams()[0], args[0]);
			} else {
				txt += args[0];
			}
			txt += getOperator(type);
			txt += getInterval(unit,mode);
		}
		return txt;
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		return prettyPrint(skin, null, opDef, args);
	}

	protected void validateArgs(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		if (args == null || args.length==1 || args.length>3) {
			throw new RenderingException("invalid number of arguments for this operator");
		}
	}

	protected String castDateAsTimestamp(SQLSkin skin, IPiece piece, String arg) throws RenderingException {
		String[] subArgs = new String[1];
		subArgs[0] = arg;
		CastOperatorDefinition toTimestamp = new CastOperatorDefinition("TO_TIMESTAMP", CastToTimestampOperatorDefinition.ID,IDomain.TIMESTAMP);
		OperatorPiece operatorPiece = new OperatorPiece(toTimestamp,new IPiece[]{piece});
		return skin.render(skin, operatorPiece, toTimestamp, subArgs);
	}

	protected String getInterval(int unit, String period) {
		return INTERVAL + "'" + unit + "' " + period;
	}

	protected String getDate(String date) {
		return date;
	}

	protected String getOperator(OperatorType type) throws RenderingException {
		switch (type) {
			case ADD:return " + ";
			case SUB:return " - ";
			default:
				throw new RenderingException("invalid operator definition");
		}
	}
}