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

import com.squid.core.sql.db.templates.SkinFactory;
import com.squid.core.sql.model.IAlias;
import com.squid.core.sql.model.Scope;

public abstract class BoundPiece
extends CommentablePiece
implements IPiece
{
	
	private Scope scope;
	
	public BoundPiece(Scope scope) {
		super();
		this.scope = scope;
	}
	
	public Scope getScope() {
		return this.scope;
	}

	protected String getAlias(Object binding) throws RenderingException {
		Object value = scope.get(binding);
		if (value==null||!(value instanceof IAlias)) {
			throw new RenderingException("Cannot find alias for "+binding);
			//return "XXX";//use that for debugging !!!
		}
		return ((IAlias)value).getAlias();
	}
	
	protected Object getBinding(Object binding) {
		return scope.get(binding);
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
