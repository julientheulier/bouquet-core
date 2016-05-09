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
import com.squid.core.domain.extensions.date.DateTruncateOperatorDefinition;
import com.squid.core.domain.extensions.date.DateTruncateShortcutsOperatorDefinition;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * @author luatnn
 * @rev 2011-06-29 by jth: for Teradata, truncate to day if input is a timestamp
 */
public class DateTruncateOperatorRenderer extends BaseOperatorRenderer {

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef, String[] args) throws RenderingException {
		if (args.length == 2 && opDef.getExtendedID().equals(DateTruncateOperatorDefinition.DATE_TRUNCATE)) {
			return prettyPrintTwoArgs(skin, piece, opDef, args);
		} else if (args.length == 1 && opDef.getExtendedID().startsWith(DateTruncateShortcutsOperatorDefinition.SHORTCUT_BASE)) {
			if (opDef.getExtendedID().equals(DateTruncateShortcutsOperatorDefinition.DAILY_ID) && piece!=null) {
				// if DAILY, only applies if the data is a timestamp
				ExtendedType type0 = piece.getParamTypes()[0];
				if (!type0.getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
					return "("+args[0]+")";
				}
			}
			String arg1 = getArgument(opDef.getExtendedID());
			return prettyPrintTwoArgs(skin, piece, opDef, new String[]{args[0],arg1});
		} else {
			throw new RenderingException("Invalid operator " + opDef.getSymbol());
		}
	}

	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		return prettyPrint(skin, null, opDef, args);
	}

	protected String prettyPrintTwoArgs(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef, String[] args) throws RenderingException {
	    ExtendedType[] extendedTypes = null;
        extendedTypes = getExtendedPieces(piece);
        if(DateTruncateOperatorDefinition.WEEK.equals(args[1].replaceAll("'", ""))) {
            return args[0] + " - ((" + args[0] + " - CAST('1900' AS DATE FORMAT 'YYYY')) MOD 7 + 1) + 1";
        } else if(DateTruncateOperatorDefinition.MONTH.equals(args[1].replaceAll("'", ""))) {
            return args[0] + " - extract(day from "+ args[0] +") + 1";
        } else if(DateTruncateOperatorDefinition.YEAR.equals(args[1].replaceAll("'", ""))) {
            return args[0] + "-(" + args[0] + " - CAST(CAST(EXTRACT(YEAR FROM ("+ args[0] +")) AS CHAR(4)) AS DATE FORMAT 'YYYY') + 1) +1";
        } else if (extendedTypes[0].getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
            //a timestamp has to be DATE_TRUNCated so it becomes a date
            return "cast(" + args[0] + " as date)"; 
        } else if (extendedTypes[0].getDomain().isInstanceOf(IDomain.DATE)) {
            // If it is already a date, no transformation is required
            return args[0];
        }
        return opDef.getSymbol() + "(" + args[0] + "," + args[1] + ")";
	}
	
	private String getArgument(String extendedID) throws RenderingException {
		if (extendedID.equalsIgnoreCase(DateTruncateShortcutsOperatorDefinition.HOURLY_ID)) {
			return DateTruncateOperatorDefinition.HOUR;
		}
		if (extendedID.equalsIgnoreCase(DateTruncateShortcutsOperatorDefinition.DAILY_ID)) {
			return DateTruncateOperatorDefinition.DAY;
		}
		if (extendedID.equalsIgnoreCase(DateTruncateShortcutsOperatorDefinition.WEEKLY_ID)) {
			return DateTruncateOperatorDefinition.WEEK;
		}
		if (extendedID.equalsIgnoreCase(DateTruncateShortcutsOperatorDefinition.MONTHLY_ID)) {
			return DateTruncateOperatorDefinition.MONTH;
		}
		if (extendedID.equalsIgnoreCase(DateTruncateShortcutsOperatorDefinition.YEARLY_ID)) {
			return DateTruncateOperatorDefinition.YEAR;
		}
		throw new RenderingException("invalid argument for DATE_TRUNCATE");
	}

}
