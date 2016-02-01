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

import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.sql.model.IAlias;
import com.squid.core.sql.model.Scope;

public class SelectPiece 
extends BoundPiece 
implements ISelectPiece, IAlias, ITypedPiece
{
	
	private IPiece select;
	private String alias;
	private boolean quoteAlias = true;

	public SelectPiece(Scope scope, IPiece select, String alias) {
		super(scope);
		this.select = select;
		this.alias = alias;
	}

	@Override
	public String getAlias() {
		return alias;
	}
	
	/**
	 * setAlias is protected ON PURPOSE. The alias is supposed to be set by the constructor, usually the SelectStatement, and guaranty to be unique in the statement scope.
	 * Also the alias is an internal string (something like a2, a3,...) to avoid possible collision with existing objects.
	 * User code should not try to modify the alias; if you really really need to override the alias definition, consider providing your own DatabaseSelectInterface...
	 * 
	 * @param alias
	 */
	protected void setAlias(String alias) {
		this.alias = alias;
	}

	public IPiece getSelect () {
		return select;	
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		try {
			String render = "";
			if (hasComment()) {
				render += renderComment(skin);
			}
			render += select.render(skin);
			if (alias!=null) {
		        render += " AS ";
		        if (!quoteAlias) {
		        	render += alias;
		        } else {
		        	render += skin.quoteIdentifier(alias);
		        }
			}
	        return render;
		} catch (RenderingException e) {
			String render = "-- ERROR while generating SQL: "+e.getMessage();
			if (alias!=null) {
		        render += "\n-- for element: ";
		        if (!quoteAlias) {
		        	render += alias;
		        } else {
		        	render += skin.quoteIdentifier(alias);
		        }
			}
	        return render;
		}
	}

	public void quoteAlias(boolean flag) {
		this.quoteAlias= flag;
	}

	public boolean isQuoteAlias() {
		return quoteAlias;
	}
	
	@Override
	public ExtendedType getType() {
		if (select instanceof ITypedPiece) {
			return ((ITypedPiece)select).getType();
		} else {
			return ExtendedType.UNDEFINED;
		}
	}

}
