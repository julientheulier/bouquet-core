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

import java.util.ArrayList;


/**
 *
 * @author serge fantino
 *
 */
public class JoinDecorator
implements IJoinDecorator
{

	protected JoinType type;
	private IWherePiece condition;
	protected IFromPiece joinTable;

	public JoinDecorator(JoinType type, IFromPiece joinTable, IWherePiece condition) {
		this.type = type;
		this.joinTable = joinTable;
		this.condition = condition;
		this.joinTable.setDefiningJoinDecorator(this);
	}

	/**
	 * add the new condition to the join (using AND)
	 * @param condition
	 */
	public void addCondition(IWherePiece condition) {
		if (this.condition instanceof WhereAndPiece) {
			((WhereAndPiece)this.condition).getPieces().add(condition);
		} else {
			ArrayList<IPiece> pieces = new ArrayList<IPiece>();
			pieces.add(this.condition);
			pieces.add(condition);
			this.condition = new WhereAndPiece(pieces);
		}
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		String render = "";
		switch (type) {
			case LEFT:
				render = skin.getToken(SQLTokenConstant.LEFT_OUTER_JOIN);
				break;
			case RIGHT:
				render = skin.getToken(SQLTokenConstant.RIGHT_OUTER_JOIN);
				break;
			case FULL:
				render = skin.getToken(SQLTokenConstant.FULL_OUTER_JOIN);
				break;
			case INNER:
				render = skin.getToken(SQLTokenConstant.INNER_JOIN);
				break;
			default:
				throw new RenderingException("unsupported join type");
		}
		render += " "+joinTable.render(skin);
		render += " ON ";
		render += condition.render(skin);
		return render;
	}

}
