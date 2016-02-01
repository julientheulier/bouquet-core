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

public class OrderByPiece implements IOrderByPiece {

	private ORDERING ordering = ORDERING.ASCENT;
	private IPiece piece;
	private NULLS_ORDERING nullsOrdering;
	
	public OrderByPiece (IPiece piece) {
		super ();
		this.piece = piece;
	}
	
	public OrderByPiece (IPiece piece, ORDERING ordering) {
		super ();
		this.piece = piece;
		this.ordering = ordering;
	}
	
	@Override
	public IPiece getPiece() {
		return piece;
	}
	
	@Override
	public ORDERING getOrdering() {
		return ordering;
	}

	@Override
	public void setOrdering(ORDERING o) {
		ordering = o;
	}
	
	public NULLS_ORDERING getNullsOrdering() {
		return nullsOrdering;
	}
	
	public void setNullsOrdering(NULLS_ORDERING nullsOrdering) {
		this.nullsOrdering = nullsOrdering;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		StringBuilder render = new StringBuilder();
		if (piece instanceof SelectPiece && ((SelectPiece)piece).getAlias() != null) {
			if (((SelectPiece)piece).isQuoteAlias()) {
				render.append(skin.quoteIdentifier(((SelectPiece)piece).getAlias()));
			} else {
				render.append(((SelectPiece)piece).getAlias());
			}
		} else {
			render.append("(" + piece.render(skin) + ")");
		}
		//
		if (ordering == ORDERING.ASCENT) render.append(" ASC");
		else render.append(" DESC");
		//
		return renderNullsOrdering(skin, render.toString());
	}
	
	private String renderNullsOrdering(SQLSkin skin, String render) throws RenderingException {
		if (this.nullsOrdering == NULLS_ORDERING.NULLS_FIRST) {
			try{	
				String token = skin.getToken(SQLTokenConstant.NULLS_FIRST);
				return render +" "+ token;
			}
			catch(RenderingException e) {
				return " CASE WHEN " + piece.render(skin) + " IS NULL THEN 0 ELSE 1 END , "+render;
			}
		} else if (this.nullsOrdering == NULLS_ORDERING.NULLS_LAST) {
			try{
				String token = skin.getToken(SQLTokenConstant.NULLS_LAST);
				return (render + " "+ token);
			}
			catch(RenderingException e) {
				return " CASE WHEN " + piece.render(skin) + " IS NULL THEN 1 ELSE 0 END , "+render;
			}
		}	 else {
			return render;
		}
	}

}
