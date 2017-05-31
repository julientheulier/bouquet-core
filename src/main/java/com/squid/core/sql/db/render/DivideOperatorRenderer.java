package com.squid.core.sql.db.render;

import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

public class DivideOperatorRenderer extends  BaseOperatorRenderer{

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		if (args.length == 2) {
			// handle ticket #627
			// return "(CASE WHEN "+args[1]+"=0 THEN NULL ELSE
			// "+args[0]+"/"+args[1]+" END)";
			String arg0 = args[0];
			String arg1 = "(NULLIF(" + args[1] + ",0))";
			if (piece.getParamTypes() != null) {
				ExtendedType t0 = piece.getParamTypes()[0];
				ExtendedType t1 = piece.getParamTypes()[1];
				if (t0 != null && t1 != null && isExactNumber(t0)) {
					// cast result as float ?
					ExtendedType ext = getExtendedType();
					// ColumnType type =
					// createColumnType(ext.getDomain(),ext.getDataType(),null,ext.getSize(),ext.getScale());
					arg0 = "(CAST ((" + arg0 + ") AS " + skin.getTypeDefinition(ext) + "))";
				}
			}
			String safeOp = arg0 + opDef.getSymbol() + arg1;
			return safeOp;
		} else{
			throw new RenderingException("DIVIDE operator requires 2 args");
		}

	}

	protected boolean isExactNumber(ExtendedType ext) {
		return ext.isExactNumber();
	}

	protected ExtendedType getExtendedType() {
		return ExtendedType.FLOAT;
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		throw new RenderingException();
	}

}
