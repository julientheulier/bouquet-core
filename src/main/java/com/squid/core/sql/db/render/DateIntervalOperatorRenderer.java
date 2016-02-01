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

import java.util.Vector;

import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * The SQL render for converting a time difference into a specific period
 * @author julien theulier
 *
 */
public class DateIntervalOperatorRenderer
extends BaseOperatorRenderer
{
	protected DateSubOperatorRenderer dateSubRenderer = new DateSubOperatorRenderer();
	protected Vector<String> periods = new Vector<String>();
	protected Vector<String> multipliers = new Vector<String>();

	public DateIntervalOperatorRenderer() {
		periods.add("SECOND");
		periods.add("MINUTE");
		periods.add("HOUR");
		periods.add("DAY");
		multipliers.add("1");
		multipliers.add("60");
		multipliers.add("60");
		multipliers.add("24");
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		return prettyPrint(skin, null, opDef, args);
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef, String[] args) throws RenderingException {
		validateArgs(skin, opDef, args);
		//Time difference computation
		String[] subArgsSub = new String[2];
		subArgsSub[0] = args[0];
		subArgsSub[1] = args[1];
		String intervalAsTimestamp = dateSubRenderer.prettyPrint(skin, piece,opDef, subArgsSub);

		//Extract periods & convert them into the desired period
		String txt = "(";
		int position = periods.indexOf(args[2].trim().replaceAll("'", "")	);
		if (position==-1) {
			throw new RenderingException("The last argument must be a valid period");
		}
		String[] subArgsExtract = new String[1];
		subArgsExtract[0] = intervalAsTimestamp;
		String  complement="";
		for (int i=position; i<periods.size();i++) {
			ExtractOperatorRenderer extractOperatorRenderer = extractOperatorRendererForPeriod(periods.elementAt(i));

			String operation = extractOperatorRenderer.prettyPrint(skin, piece,opDef,subArgsExtract);

			txt += complement + operation;
			for (int j=i; j>position;j--) {
				txt += "*"+multipliers.elementAt(j);
			}
			complement = " + ";
		}
		txt += ")";
		return txt;
	}

	protected void validateArgs(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		if (args == null || args.length!=3) {
			throw new RenderingException("invalid number of arguments for this operator");
		}
		if ("SECOND".equalsIgnoreCase(args[2].toUpperCase().trim().replaceAll("'", ""))==false &&
				"MINUTE".equalsIgnoreCase(args[2].toUpperCase().trim().replaceAll("'", ""))==false &&
				"HOUR".equalsIgnoreCase(args[2].toUpperCase().trim().replaceAll("'", ""))==false &&
				"DAY".equalsIgnoreCase(args[2].toUpperCase().trim().replaceAll("'", ""))==false) {
			throw new RenderingException("The last argument must be a valid period");
		}
	}

	protected ExtractOperatorRenderer extractOperatorRendererForPeriod(String period) {
		return new ExtractOperatorRenderer(period);
	}
}
