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
import com.squid.core.domain.IDomain;
import com.squid.core.domain.extensions.cast.CastToDateOperatorDefinition;
import com.squid.core.domain.extensions.date.DateTruncateShortcutsOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateAddOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateSubOperatorDefinition;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.expression.NumericConstant;
import com.squid.core.expression.StringConstant;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SimpleConstantValuePiece;
/**
 * This class is to implement ADD_MONTHS as an interval when not supported by a database vendor
 * This is for MySQL and Postgres
 * A code generating the same logic as Oracle or Redshift is (ex with 1 month to add:
 * <code>
 * 	case when
 *		transaction_date=date_trunc('month',transaction_date)+interval'1 month'-interval'1 day'
 *		THEN date_trunc('month',transaction_date)+interval'2 month'-interval'1 day'
 *	else date_trunc('month',transaction_date)+interval'1 month' end
 * </code>
 * @author jtheulier
 *
 */
public class AddMonthsAsIntervalOperatorRenderer extends BaseOperatorRenderer {

	public AddMonthsAsIntervalOperatorRenderer() {
	}

	@Override
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

		OperatorDefinition truncate = OperatorScope.getDefault().lookupByExtendedID(DateTruncateShortcutsOperatorDefinition.MONTHLY_ID);
		OperatorDefinition cast = OperatorScope.getDefault().lookupByExtendedID(CastToDateOperatorDefinition.TO_DATE);
		OperatorDefinition intervalAdd = OperatorScope.getDefault().lookupByExtendedID(DateAddOperatorDefinition.ID);
		OperatorDefinition intervalSub = OperatorScope.getDefault().lookupByExtendedID(DateSubOperatorDefinition.ID);

		String truncated = skin.render(skin, piece, truncate, new String[]{args[0]});
		boolean isPositive = true;
		if (addMonths<0) {
			isPositive = false;
			addMonths = addMonths *-1;
		}
		NumericConstant one = new NumericConstant(1);
		NumericConstant months = new NumericConstant(addMonths);
		NumericConstant months1 = new NumericConstant(addMonths+1);
		StringConstant day = new StringConstant("day");
		StringConstant month = new StringConstant("month");

		IPiece onePiece = new SimpleConstantValuePiece(one.getValue(), one.computeType(skin));
		IPiece monthsPiece = new SimpleConstantValuePiece(months.getValue(), months.computeType(skin));
		IPiece months1Piece = new SimpleConstantValuePiece(months1.getValue(), months1.computeType(skin));

		IPiece dayPiece = new SimpleConstantValuePiece(day.getValue(), day.computeType(skin));
		IPiece monthPiece = new SimpleConstantValuePiece(month.getValue(), month.computeType(skin));

		String endOfMonth = truncated;
		String endOfComputedMonth = truncated;

		OperatorPiece oneDay =  new OperatorPiece(intervalSub, new IPiece[]{piece.getParams()[0], onePiece, dayPiece});
		OperatorPiece oneMonth = new OperatorPiece(intervalAdd, new IPiece[]{piece.getParams()[0], onePiece, monthPiece});
		OperatorPiece addedMonth = new OperatorPiece(intervalAdd, new IPiece[]{piece.getParams()[0], monthsPiece, monthPiece});
		OperatorPiece addedMonth1 = new OperatorPiece(intervalAdd, new IPiece[]{piece.getParams()[0], months1Piece, monthPiece});

		endOfMonth = skin.render(skin, oneMonth, intervalAdd, new String[]{endOfMonth, one.toString(), month.toString()});
		endOfMonth = skin.render(skin, oneDay, intervalSub, new String[]{endOfMonth, one.toString(), day.toString()});

		endOfComputedMonth = skin.render(skin, addedMonth1, (isPositive?intervalAdd:intervalSub), new String[]{endOfComputedMonth, months1.toString(), months1.toString()});
		endOfComputedMonth = skin.render(skin, oneDay, intervalSub, new String[]{endOfComputedMonth, one.toString(), day.toString()});

		String addMonth = skin.render(skin, addedMonth, (isPositive?intervalAdd:intervalSub), new String[]{args[0], months.toString(), month.toString()});

		if (piece.getParamTypes()[0].equals(ExtendedType.DATE)) {
			endOfMonth = skin.render(skin, piece, cast, new String[]{endOfMonth});
		}

		String txt = "CASE WHEN " + args[0] + " = " + endOfMonth + " THEN " + endOfComputedMonth + " ELSE "+ addMonth + " END";
		if (piece.getParamTypes()[0].equals(ExtendedType.DATE)) {
			txt = skin.render(skin, piece, cast, new String[]{txt});
		}
		return txt;
	}

}
