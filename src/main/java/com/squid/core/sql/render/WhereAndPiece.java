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



public class WhereAndPiece 
extends AbstractWherePiece
{
	
	private List<IPiece> pieces;
	private boolean not;
	
	public WhereAndPiece(List<IPiece> pieces) {
		this.pieces = pieces;
	}
	
	public List<IPiece> getPieces() {
		return pieces;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		String render = "";
		if (hasComment()) render += renderComment(skin);
		if (not){
			render+=skin.getToken(SQLTokenConstant.NOT);
		}
		if (pieces.size()>1 || not) render += "(";
		//
		int i=0;
		for (IPiece piece : pieces) {
			if (i>0) render +=" " + skin.getToken(SQLTokenConstant.AND) +" ";
			render += "("+piece.render(skin)+")";
			i++;
		}
		//
		if (pieces.size()>1 || not) render += ")";
		//
		return render;
	}

}
