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

import com.squid.core.sql.db.features.IAliasInGroupingSupport;
import com.squid.core.sql.db.templates.SkinFactory;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.ISelectPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SelectPiece;

public class GroupByElementPiece implements IGroupByElementPiece {

	private IPiece piece;
	private GroupType type;

	public GroupByElementPiece(IPiece select) {
		super();
		this.piece = select;
		this.type = GroupType.GROUP_BY;
	}

	public GroupByElementPiece(IPiece piece, GroupType type) {
		super();
		this.piece = piece;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.squid.core.sql2.render.IGroupByPeice#getSelect()
	 */
	public IPiece getPiece() {
		return piece;
	}

	@Override
	public GroupType getType() {
		return type;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		if (piece instanceof ISelectPiece) {
			if  (((SelectPiece)piece).getAlias() != null && skin.getFeatureSupport(IAliasInGroupingSupport.ID)==IAliasInGroupingSupport.IS_SUPPORTED) {
				if (((SelectPiece)piece).isQuoteAlias()) {
					return skin.quoteIdentifier(((SelectPiece)piece).getAlias());
				} else {
					return ((SelectPiece)piece).getAlias();
				}
			} else {
				return ((ISelectPiece)piece).getSelect().render(skin);
			}
		} else {
			return piece.render(skin);
		}
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