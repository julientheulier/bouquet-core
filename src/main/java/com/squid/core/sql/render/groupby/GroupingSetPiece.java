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

import com.squid.core.sql.db.templates.SkinFactory;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

public class GroupingSetPiece 
implements IGroupByElementPiece
{

	private List<IGroupByElementPiece> pieces;
	private GroupType type;
	
	public GroupingSetPiece() {
		super();
		this.pieces = new ArrayList<IGroupByElementPiece>();
		this.type = GroupType.INNER;
	}
	
	public GroupingSetPiece(List<IGroupByElementPiece> pieces) {
		super();
		this.pieces = pieces;
		this.type = GroupType.GROUPING_SETS;
	}
	
	public GroupingSetPiece(List<IGroupByElementPiece> pieces, GroupType type) {
		super();
		this.pieces = pieces;
		this.type = type;
	}

	public List<IGroupByElementPiece> getPieces() {
		return pieces;
	}

	@Override
	public GroupType getType() {
		return type;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		return skin.render(skin, this);
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
