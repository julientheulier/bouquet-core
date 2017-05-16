package com.squid.core.sql.db.render;

import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

public class CountDistinctOperatorRenderer extends  BaseOperatorRenderer {

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		return "COUNT(DISTINCT (" + args[0] + "))";

	}

}
