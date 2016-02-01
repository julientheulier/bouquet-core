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
import java.util.Iterator;

public class OrderedAnalyticalPiece
implements IPiece
{
	
	private String operator;
	private ArrayList<IPiece> partitionByClause = new ArrayList<IPiece>();
	private ArrayList<OrderByPiece> orderByClause = new ArrayList<OrderByPiece>();
	
	public OrderedAnalyticalPiece(String operator) {
		super();
		this.operator = operator;
	}
	
	public void addPartitionByClause(IPiece piece) {
		this.partitionByClause.add(piece);
	}
	
	public void addOrderByClause(OrderByPiece piece) {
		this.orderByClause.add(piece);
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		String res = "(" + operator+" OVER (";
		String pb = "";
		for (Iterator<IPiece> iter = partitionByClause.iterator();iter.hasNext();) {
			if (pb!="") pb += ",";
			pb += iter.next().render(skin);
		}
		res += "PARTITION BY " + pb;
		pb = "";
		for (Iterator<OrderByPiece> iter = orderByClause.iterator();iter.hasNext();) {
			if (pb!="") pb += ",";
			pb += iter.next().render(skin);
		}
		res += " ORDER BY "+pb+"))=1";
		return res;
	}

}
