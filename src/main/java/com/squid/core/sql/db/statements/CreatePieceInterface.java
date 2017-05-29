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
package com.squid.core.sql.db.statements;

import java.util.Iterator;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.Key;
import com.squid.core.sql.db.render.ColumnPiece;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.ExpressionListPiece;
import com.squid.core.sql.render.IPiece;

/**
 * This is an attempt to factor all the createPiece() method...
 * @author serge fantino
 *
 */
public class CreatePieceInterface {
	
	public ColumnPiece createPiece(Scope parent, Column column) throws SQLScopeException {
		return new ColumnPiece(parent,column);
	}
	
	public ExpressionListPiece createPiece(Scope parent, Key key) throws SQLScopeException {
		IPiece[] pieces = new IPiece[key.getColumns().size()];
		int i=0;
		for (Iterator<Column> iter = key.getColumns().iterator();iter.hasNext();) {
			Column column = iter.next();
			pieces[i++] = createPiece(parent, column);
		}
		return new ExpressionListPiece(pieces);
	}

}
