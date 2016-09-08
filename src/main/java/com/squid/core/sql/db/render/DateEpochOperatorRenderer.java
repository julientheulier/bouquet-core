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

import java.util.Calendar;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.extensions.cast.CastOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateOperatorDefinition;
import com.squid.core.domain.extensions.date.IntervalOperatorDefinition;
import com.squid.core.domain.extensions.cast.CastToCharOperatorDefinition;
import com.squid.core.domain.extensions.cast.CastToTimestampOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateAddOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateIntervalOperatorDefinition;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SimpleConstantValuePiece;

public class DateEpochOperatorRenderer extends BaseOperatorRenderer {

	public static final int FROM = 0;
	public static final int TO = 1;
	
	protected int type;

	protected CastOperatorDefinition toTimestamp = null;
	protected CastOperatorDefinition toChar = null;
	protected DateOperatorDefinition diod = null;
	protected DateOperatorDefinition dasod = null;
	protected CastOperatorRenderer cor = null;
	protected DateIntervalOperatorRenderer tdior = null;
	protected DateAddSubOperatorRenderer dasor = null;
	protected IntervalOperatorDefinition iodh = null;
	protected IntervalOperatorDefinition iodm = null;
	protected IntervalOperatorDefinition iods = null;
	protected IntervalOperatorRenderer iorh = null;
	protected IntervalOperatorRenderer iorm = null;
	protected IntervalOperatorRenderer iors = null;

	public DateEpochOperatorRenderer(int type) {
		this.type = type;
		toTimestamp = (CastOperatorDefinition) OperatorScope.getDefault().lookupByExtendedID(CastToTimestampOperatorDefinition.ID);
		toChar = (CastOperatorDefinition) OperatorScope.getDefault().lookupByExtendedID(CastToCharOperatorDefinition.ID);
		diod = (DateOperatorDefinition) OperatorScope.getDefault().lookupByExtendedID(DateIntervalOperatorDefinition.ID);
		dasod = (DateOperatorDefinition) OperatorScope.getDefault().lookupByExtendedID(DateAddOperatorDefinition.ID);
		iodh = (IntervalOperatorDefinition) OperatorScope.getDefault().lookupByExtendedID(IntervalOperatorDefinition.INTERVAL_HOUR);
		iodm = (IntervalOperatorDefinition) OperatorScope.getDefault().lookupByExtendedID(IntervalOperatorDefinition.INTERVAL_MINUTE);
		iods = (IntervalOperatorDefinition) OperatorScope.getDefault().lookupByExtendedID(IntervalOperatorDefinition.INTERVAL_SECOND);
		iorh = new IntervalOperatorRenderer("HOUR");
		iorm = new IntervalOperatorRenderer("MINUTE");
		iors = new IntervalOperatorRenderer("SECOND");
		cor = new CastOperatorRenderer();
		tdior = new DateIntervalOperatorRenderer();
		dasor = new DateAddSubOperatorRenderer(DateAddSubOperatorRenderer.OperatorType.ADD);
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		String txt = "";
		Calendar c = Calendar.getInstance();
		c.set(1970, 0, 1, 0, 0, 0);
		SimpleConstantValuePiece epochDate = new SimpleConstantValuePiece(c.getTime(), ExtendedType.DATE);
		
		switch (type) {
		case FROM:
			SimpleConstantValuePiece daysPiece = new SimpleConstantValuePiece("	", ExtendedType.NUMERIC);
			OperatorPiece daysAdd = new OperatorPiece(dasod, new IPiece[] {epochDate, daysPiece} );
			SimpleConstantValuePiece formatTimePiece = new SimpleConstantValuePiece("'99'", ExtendedType.STRING);
			SimpleConstantValuePiece formatDatePiece = new SimpleConstantValuePiece("'YYYY-MM-DD'", ExtendedType.STRING);
			SimpleConstantValuePiece formatTimestampPiece = new SimpleConstantValuePiece("'YYYY-MM-DD HH:MI:SS'", ExtendedType.STRING);
			String[] daysAddArgs = new String[2];
			//TODO: finish implementation so it can generate SQL code below
			daysAddArgs[0] = epochDate.render(skin);
			daysAddArgs[1] = daysPiece.render(skin);
			
			String addDaysTxt = skin.render(skin, daysAdd, dasod, daysAddArgs);
			
			SimpleConstantValuePiece minutesPiece = new SimpleConstantValuePiece("(CAST(" + args[0] + " AS INTEGER) MOD 86400) / 3600", ExtendedType.NUMERIC);
			String[] minutesAddArgs = new String[2];
			minutesAddArgs[0] =  minutesPiece.render(skin);
			minutesAddArgs[1] =  formatTimePiece.render(skin);
			
			txt += skin.render(skin,new OperatorPiece(dasod, new IPiece[] {new SimpleConstantValuePiece(dasor.prettyPrint(skin, daysAdd, dasod, daysAddArgs), IDomain.DATE), formatTimestampPiece} ), toChar, new String[] {dasor.prettyPrint(skin, daysAdd, dasod, daysAddArgs), formatTimestampPiece.render(skin)});
			txt += " || ' ' || " + skin.render(skin, new OperatorPiece(toChar,new IPiece[] {minutesPiece, formatTimePiece}), toChar, minutesAddArgs);

/*			
			txt = "CASE WHEN " + args[0] + ">=0 THEN";
			txt += "\n\t\t\t CAST(((DATE '1970-01-01' + (CAST(CAST(" + args[0] + " / 86400 as INTEGER) AS INTEGER)) ) ( FORMAT 'YYYY-MM-DD'))";
			txt += "\n\t\t\t || ' ' || (((CAST(" + args[0] + " AS INTEGER) MOD 86400) / 3600 ) (FORMAT '99'))";
			txt += "\n\t\t\t || ':' || (((CAST(" + args[0] + " AS INTEGER) MOD 3600 ) / 60) (FORMAT '99'))";
			txt += "\n\t\t\t || ':' || ((CAST(" + args[0] + " AS INTEGER) MOD 60) (FORMAT '99')) AS TIMESTAMP(0) )";
			txt += "\n\t\t ELSE";
			txt += "\n\t\t\t CAST(((DATE '1970-01-01' + (CAST(CAST(" + args[0] + " / 86400 as INTEGER) + (CASE WHEN (" + args[0] + " / 86400 - CAST(" + args[0] + " / 86400 as INTEGER))<0.5 THEN 0 ELSE 1 END) + (CASE WHEN ((CAST(" + args[0] + " AS INTEGER) MOD 60) + 60)>0 OR ((CAST(" + args[0] + " AS INTEGER) MOD 3600) / 60)>0  OR ((CAST(" + args[0] + " AS INTEGER) MOD 86400) / 3600)>0 THEN -1 ELSE 0 END) AS INTEGER)) ) ( FORMAT 'YYYY-MM-DD'))";
			txt += "\n\t\t\t || ' ' || (((CAST(" + args[0] + " AS INTEGER) MOD 86400 ) / 3600 + (CASE WHEN ((CAST(" + args[0] + " AS INTEGER) MOD 60) + 60)>0 OR ((CAST(" + args[0] + " AS INTEGER) MOD 3600) / 60)>0  THEN 23 ELSE 24 END)) (FORMAT '99'))";
			txt += "\n\t\t\t || ':' || (((CAST(" + args[0] + " AS INTEGER) MOD 3600 ) / 60 + (CASE WHEN ((CAST(" + args[0] + " AS INTEGER) MOD 60) + 60)>0 THEN 59 ELSE 60 END)) (FORMAT '99'))";
			txt += "\n\t\t\t || ':' || (((CAST(" + args[0] + " AS INTEGER) MOD 60) + 60) (FORMAT '99')) AS TIMESTAMP(0) )";
			txt += "\n\t\tEND";
*/			
			break;
		case TO:
			String[] epochArgs = new String[2];
			epochArgs[0] = "'1970-01-01 00:00:00'";
			epochArgs[1] = "'YYYY-MM-DD HH:MI:SS'";
			SimpleConstantValuePiece epochReference = new SimpleConstantValuePiece(epochArgs[0],ExtendedType.STRING);
			SimpleConstantValuePiece epochReferenceFormat = new SimpleConstantValuePiece(epochArgs[1],ExtendedType.STRING);
			OperatorPiece castPiece = new OperatorPiece(toTimestamp, new IPiece[]{epochReference, epochReferenceFormat});
			String[] extractArgs = new String[3];
			extractArgs[0] = args[0];
			extractArgs[1] = skin.render(skin, castPiece, toTimestamp, epochArgs);
			extractArgs[2] = "SECOND";
			
			IPiece timestamp = piece.getParams()[0];
			SimpleConstantValuePiece epochTimestamp = new SimpleConstantValuePiece(extractArgs[1],ExtendedType.TIMESTAMP);
			SimpleConstantValuePiece extractUnit = new SimpleConstantValuePiece(extractArgs[2],ExtendedType.STRING);
			OperatorPiece extractPiece = new OperatorPiece(toTimestamp, new IPiece[]{timestamp, epochTimestamp, extractUnit}, new ExtendedType[] {ExtendedType.TIMESTAMP, ExtendedType.TIMESTAMP, ExtendedType.STRING});
			txt = skin.render(skin, extractPiece, diod, extractArgs);
			
			
			break;
		}
		return txt;
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		return prettyPrint(skin, null, opDef, args);
		
	}

}
