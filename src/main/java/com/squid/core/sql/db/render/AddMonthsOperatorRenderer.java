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

import com.squid.core.domain.extensions.cast.CastToDateOperatorDefinition;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
/**
 * This class is to implement ADD_MONTHS as an interval when not supported by a database vendor
 * This is for MySQL and Postgres
 * @author jtheulier
 *
 */
public class AddMonthsOperatorRenderer extends BaseOperatorRenderer {

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		String txt = "ADD_MONTHS("+args[0]+", "+args[1]+")";
		OperatorDefinition cast = OperatorScope.getDefault().lookupByExtendedID(CastToDateOperatorDefinition.TO_DATE);
		if (piece.getParamTypes()[0].equals(ExtendedType.DATE)) {
			txt = skin.render(skin, piece, cast, new String[]{txt});
		}
		return txt;
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		throw new RenderingException("invalid arguments for "+opDef.getName()+"() operator");
	}

}
