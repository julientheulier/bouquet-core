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
package com.squid.core.sql.db.render;

import com.squid.core.database.model.Column;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.BoundPiece;
import com.squid.core.sql.render.ITypedPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

public class ColumnPiece 
extends BoundPiece 
implements ITypedPiece
{

	protected Column column;
	private String alias;
	
	public ColumnPiece(Scope scope, Column column) throws SQLScopeException {
		super(scope);
		this.column = column;
		try {
			this.alias = getAlias(column.getTable());
		} catch (RenderingException e) {
			throw new SQLScopeException("cannot find column '"+column.getName()+"' in the scope");
		}
	}
	
	@Override
	public ExtendedType getType() {
		return column.getType();
	}

	public Column getColumn () {
		return column;
	}
	
	public String getAlias() {
		return this.alias;
	}
	
	public String render(SQLSkin skin) throws RenderingException {
		String render = getAlias();
        render += ".";
        render += skin.quoteColumnIdentifier(column);
        return render;
	}

}
