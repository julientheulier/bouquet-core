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


public class CountPiece
implements IPiece
{
	
	private IPiece expression;
	private boolean distinct = false;
	
	public CountPiece() {
		this.expression = new SelectAllPiece();
	}
	
	public CountPiece(IPiece expression) {
		this.expression = expression;
	}
	
	public CountPiece(IPiece expression, boolean distinct) {
		super();
		this.expression = expression;
		this.distinct = distinct;
	}
	
	public CountPiece(ExpressionListPiece expression,boolean distinct) {
		super();
		this.expression = expression;
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		String render = "";
		if (distinct){
			render = "COUNT(DISTINCT ";
		}else{
			render = "COUNT(";
		}
		if (expression!=null) {
			render+=expression.render(skin)+")";
		} else {
			render+="*)";
		}
		return render;
	}

}