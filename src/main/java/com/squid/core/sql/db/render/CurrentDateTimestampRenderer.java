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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.squid.core.domain.extensions.DateOperatorDefinition;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

public class CurrentDateTimestampRenderer
extends BaseOperatorRenderer {
	
	protected static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	protected static SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef, String[] args) throws RenderingException {
//		if ((opDef.getId()==OperatorScope.CURRENT_DATE || opDef.getId()==OperatorScope.CURRENT_TIMESTAMP) && args.length==0) {
		if (args.length==0) {
			return opDef.getSymbol();
		} else {
			Calendar currentDate = Calendar.getInstance();
			if (DateOperatorDefinition.CURRENT_DATE.equals(opDef.getExtendedID())) {
				return "DATE '"+df.format(currentDate.getTime())+"'";
			} else if (DateOperatorDefinition.CURRENT_TIMESTAMP.equals(opDef.getExtendedID())) {
				return "TIMESTAMP '"+tf.format(currentDate.getTime())+"'";
			} else {
				throw new RenderingException("Invalide operator " +  opDef.getSymbol());
			}
		}
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		// TODO Auto-generated method stub
		return prettyPrint(skin, null, opDef, args);
	}
}
