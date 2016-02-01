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
package com.squid.core.sql.statements;

import java.util.ArrayList;
import java.util.Iterator;

import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * UNION Statement
 * @author serge fantino
 *
 */
public class UnionStatement 
extends Statement 
implements ISelectStatement
{
	
	private ArrayList<ISelectStatement> elements = new ArrayList<ISelectStatement>();
	
	public ArrayList<ISelectStatement> getElements() {
		return elements;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		String result = "";
		for (Iterator<ISelectStatement> iter = elements.iterator();iter.hasNext();) {
			if (result!="") result += "\nUNION\n";
			result += iter.next().render(skin);
		}
		return result;
	}
	
	@Override
	public String getTemplateId() {
		return "com.squid.core.sql2.statements.UnionStatement";
	}

}
