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
package com.squid.core.sql.render;

import java.util.List;

/**
 * @deprecated SFA: you should use the CASE operator that do just that
 * @author Nha
 * 
 */
public class MultiCasePiece implements IPiece {

	private static final String CR_LF = System.getProperty("line.separator");
	
	private List<IPiece> whereParts;
	
	private List<IPiece> thenParts;
	
	private IPiece elsePart;

	public MultiCasePiece(List<IPiece> whereParts, List<IPiece> thenParts, IPiece elsePart) {
		this.whereParts = whereParts;
		this.thenParts = thenParts;
		this.elsePart = elsePart;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		StringBuilder builder = new StringBuilder();
		if(whereParts.size() == thenParts.size()){
			builder.append("(CASE");
			for (int i = 0; i < whereParts.size(); i++) {
				builder.append(" WHEN ");
				builder.append(whereParts.get(i).render(skin));
				builder.append(" THEN ");
				builder.append(thenParts.get(i).render(skin));
				builder.append(CR_LF);
			}
			builder.append(" ELSE " + elsePart.render(skin));
			builder.append(" END)");
		}
		return builder.toString();
	}

}
