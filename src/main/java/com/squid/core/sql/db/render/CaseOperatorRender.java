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

import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * The SQL render for CASE operator
 * @author serge fantino
 *
 */
public class CaseOperatorRender
extends BaseOperatorRenderer {
	
	public static final String CASE = " CASE ";
	public static final String WHEN = " WHEN (";
	public static final String THEN = ") THEN (";
	public static final String ELSE = " ELSE (";
	public static final String END = " END ";

	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		String txt = CASE;
		for (int i=0;i<args.length;) {
			if (i==args.length-1) {
				// else
				txt += ELSE+args[i++]+") ";
			} else {
				txt += WHEN+args[i++]+THEN+args[i++]+") ";
			}
		}
		txt+=END;
		return txt;
	}

}
