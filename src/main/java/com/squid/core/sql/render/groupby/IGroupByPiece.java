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

import java.util.List;

import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.render.IPiece;

public interface IGroupByPiece 
extends IPiece
{
	
	public static final int GROUP_BY = 1;
	public static final int GROUPING_SETS = 2;
	public static final int ROLLUP = 4;
	public static final int CUBE = 8;
	public static final int ALL = GROUP_BY | GROUPING_SETS | ROLLUP | CUBE;
	
	List<IGroupByElementPiece> getPieces(int filter) throws ScopeException, SQLScopeException;
	
	List<IGroupByElementPiece> getAllPieces() throws ScopeException, SQLScopeException;

}
