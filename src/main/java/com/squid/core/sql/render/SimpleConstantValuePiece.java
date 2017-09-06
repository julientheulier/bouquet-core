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
import com.squid.core.sql.db.templates.SkinFactory;

public class SimpleConstantValuePiece
implements IConstantPiece, ITypedPiece
{

	private Object value;
	private ExtendedType type;

	/**
	 * @param value
	 * @param domain
	 */
	public SimpleConstantValuePiece(Object value, ExtendedType type) {
		super();
		this.value = value;
		this.type = type;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		return skin.quoteConstant(value, type.getDomain());
	}

	@Override
	public ExtendedType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		try {
			return render(SkinFactory.INSTANCE.getDefaultSkin());
		} catch (RenderingException e) {
			return value.toString();
		}
	}

}
