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
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
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
		String truncated = skin.render(skin, piece, truncate, new String[]{args[0]});
		OperatorDefinition cast = OperatorScope.getDefault().lookupByExtendedID(CastToDateOperatorDefinition.TO_DATE);

		String operator = " + ";
		if (addMonths<0) {
			operator = " - ";
			addMonths = addMonths *-1;
		}
		String endOfMonth = truncated + " + interval '1 month' - interval '1 day'";
		if (piece.getParamTypes()[0].equals(ExtendedType.DATE)) {
			endOfMonth = skin.render(skin, piece, cast, new String[]{endOfMonth});
		}

		String txt = "CASE WHEN " + args[0] + " = " + endOfMonth + " THEN " + truncated + operator + "interval'"+ (addMonths+1) +" month"+((addMonths+1)>1?"s":"")+"' - interval'1 day' ELSE "+args[0]+ operator + "interval'"+ addMonths +" months' END";
		if (piece.getParamTypes()[0].equals(ExtendedType.DATE)) {
			txt = skin.render(skin, piece, cast, new String[]{txt});
		}
		return txt;
	}

}
