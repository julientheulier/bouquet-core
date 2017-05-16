package com.squid.core.sql.db.render;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

public class SubstractionOperatorRenderer extends  BaseOperatorRenderer{

	
	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		if (args.length == 2) {
			//
			// rule: if A-B where A is timestamp and B is date (resp B and
			// A...), then cast the Timestamp as a Date
			//
			if (piece.getParamTypes()[0].getDomain() == IDomain.TIMESTAMP
					&& piece.getParamTypes()[1].getDomain() == IDomain.DATE) {
				args[0] = "CAST(" + args[0] + " AS DATE)";
			} else if (piece.getParamTypes()[0].getDomain() == IDomain.DATE
					&& piece.getParamTypes()[1].getDomain() == IDomain.TIMESTAMP) {
				args[1] = "CAST(" + args[1] + " AS DATE)";
			}
		}
		return opDef.prettyPrint(args, true);
	}

	
	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef, String[] args) throws RenderingException {
		throw new RenderingException();			
	}

}
