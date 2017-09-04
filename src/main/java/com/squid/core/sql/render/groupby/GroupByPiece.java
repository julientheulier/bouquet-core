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
package com.squid.core.sql.render.groupby;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.sql.db.features.IAliasInGroupingSupport;
import com.squid.core.sql.db.templates.SkinFactory;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SelectPiece;

public class GroupByPiece
implements IGroupByPiece
{

	private ArrayList<IGroupByElementPiece> pieces = new ArrayList<IGroupByElementPiece>();

	public GroupByPiece() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<IGroupByElementPiece> getAllPieces() {
		return pieces;
	}

	@Override
	public List<IGroupByElementPiece> getPieces(int filter) {
		ArrayList<IGroupByElementPiece> result = new ArrayList<IGroupByElementPiece>();
		for (IGroupByElementPiece piece : pieces) {
			if ((piece.getType().v() & filter)==piece.getType().v()) {
				result.add(piece);
			}
		}
		return result;
	}

	public List<IGroupByElementPiece> getPieces(GroupType type) {
		ArrayList<IGroupByElementPiece> copy = new ArrayList<IGroupByElementPiece>();
		for (IGroupByElementPiece piece : pieces) {
			if (piece.getType()==type) {
				copy.add(piece);
			}
		}
		return copy;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		if (!pieces.isEmpty()) {
			return "GROUP BY "+renderElements(skin,getAllPieces());
		} else {
			return "";
		}
	}

	protected String renderElements(SQLSkin skin, List<IGroupByElementPiece> pieces) throws RenderingException {
		String result = "";
		boolean first = true;
		for (IGroupByElementPiece piece: pieces) {
			if (!first) result += " , ";
			if (piece instanceof SelectPiece && ((SelectPiece)piece).getAlias() != null && skin.getFeatureSupport(IAliasInGroupingSupport.ID)==IAliasInGroupingSupport.IS_SUPPORTED) {
				if (((SelectPiece)piece).isQuoteAlias()) {
					result += skin.quoteIdentifier(((SelectPiece)piece).getAlias());
				} else {
					result += ((SelectPiece)piece).getAlias();
				}
			} else {
				result += piece.render(skin);
			}
			first = false;
		}
		return result;
	}

	@Override
	public String toString() {
		try {
			return render(SkinFactory.INSTANCE.getDefaultSkin());
		} catch (RenderingException e) {
			return super.toString();
		}
	}

}
