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


/**
 * The SQL IN operator, restricted to apply to expression list (no sub-select for instance, see WhereInPiece for a general purpose one)
 * Note that InConditionPiece rendering may be overrided by the SQLSkin (for instance Teradata does NOT support multi-column/constant IN operator)
 * 
 * @author serge fantino
 *
 */
public class InConditionPiece 
extends AbstractWherePiece
{
	
	private ExpressionListPiece left;
	private ExpressionListPiece right;
	private boolean not;

	/**
	 * 
	 * @param left
	 * @param right
	 * @param not: if true, create a "NOT IN" expression
	 */
	public InConditionPiece(ExpressionListPiece left, ExpressionListPiece right, boolean not) {
		super();
		this.left = left;
		this.right = right;
		this.not = not;
	}

	public ExpressionListPiece getLeft() {
		return left;
	}
	
	public boolean isNot() {
		return not;
	}

	public ExpressionListPiece getRight() {
		return right;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		String override = skin.render(skin,this);
		if (override!=null) {
			return override;
		}
		String render = "";
		if (hasComment()) render += renderComment(skin);
		render += left.render(skin);
		if (not) render += " "+skin.getToken(SQLTokenConstant.NOT)+" ";
		render += " "+skin.getToken(SQLTokenConstant.IN)+" ";
		render += right.render(skin);
		return render;
	}

}
