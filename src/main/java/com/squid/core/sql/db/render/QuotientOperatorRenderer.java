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

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.Operators;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.NullPiece;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SimpleConstantValuePiece;

/**
 * implements Quotient operator as a 
 * @author sfantino
 *
 */
public class QuotientOperatorRenderer 
implements OperatorRenderer
{
	
	@Override
	public String prettyPrint(SQLSkin skin, OperatorDefinition opDef,
			String[] args) throws RenderingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prettyPrint(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		try {
			return skin.render(skin,applyFilter(skin, piece));
		} catch (RenderingException e) {
			throw new RenderingException("cannot apply quotient operator, argument '" + opDef.getName().trim() + "' is not a valid aggregate operator or nor supported");
		}
	}
	
	protected OperatorPiece applyFilter(SQLSkin skin, OperatorPiece piece) throws RenderingException {
		IPiece[] params = piece.getParams();
		IPiece aggregate = params[0];
		IPiece filter = params[1];
		return applyFilter(skin, aggregate, filter, true);
	}
	
	protected OperatorPiece applyFilter(SQLSkin skin, IPiece aggregate,
			IPiece filter, boolean handleDefault) throws RenderingException {
		if (aggregate instanceof OperatorPiece) {
			OperatorPiece op = (OperatorPiece)aggregate;
			if (op.getParams().length==2) {
				switch (op.getOpDef().getId()) {
					case IntrinsicOperators.COALESCE:
					{
						IPiece param1 = op.getParams()[0];
						IPiece param2 = op.getParams()[1];
						if (param1 instanceof OperatorPiece && param2 instanceof SimpleConstantValuePiece) {
							OperatorPiece subop = (OperatorPiece)param1;
							OperatorPiece subcase = applyFilter(skin, subop, filter, false);
							OperatorPiece copy = new OperatorPiece(op.getOpDef(),new IPiece[]{subcase,param2});
							//
							return copy;
						}
					}
				}
			}
			else if (op.getParams().length==1) {
				switch (op.getOpDef().getId()) {
					case IntrinsicOperators.SUM:
					case IntrinsicOperators.COUNT:
					case IntrinsicOperators.COUNT_DISTINCT:
					{
						IPiece subexp = op.getParams()[0];
						IPiece case_pieces[] = new IPiece[3];
						case_pieces[0] = filter;
						case_pieces[1] = subexp;
						case_pieces[2] = handleDefault?new SimpleConstantValuePiece(0,IDomain.NUMERIC):new NullPiece();
						OperatorPiece subcase = new OperatorPiece(Operators.CASE,case_pieces);
						OperatorPiece copy = new OperatorPiece(op.getOpDef(),new IPiece[]{subcase});
						//
						return copy;
					}
					case IntrinsicOperators.MAX:
					case IntrinsicOperators.MIN:
					case IntrinsicOperators.AVG:
					case IntrinsicOperators.MEDIAN:
					case IntrinsicOperators.VARIANCE:
					case IntrinsicOperators.VAR_SAMP:
					case IntrinsicOperators.COVAR_POP:
					{
						IPiece subexp = op.getParams()[0];
						IPiece case_pieces[] = new IPiece[3];
						case_pieces[0] = filter;
						case_pieces[1] = subexp;
						case_pieces[2] = new NullPiece();
						OperatorPiece subcase = new OperatorPiece(Operators.CASE,case_pieces);
						OperatorPiece copy = new OperatorPiece(op.getOpDef(),new IPiece[]{subcase});
						//
						return copy;
					}
					default:
					{
						IPiece param = op.getParams()[0];
						if (param instanceof OperatorPiece) {
							OperatorPiece subop = (OperatorPiece)param;
							OperatorPiece subcase = applyFilter(skin, subop, filter, handleDefault);
							OperatorPiece copy = new OperatorPiece(op.getOpDef(),new IPiece[]{subcase});
							//
							return copy;
						}
					}
				}
			}
			else if (op.getParams().length==0) {
				switch (op.getOpDef().getId()) {
					case IntrinsicOperators.COUNT:
					{
						IPiece case_pieces[] = new IPiece[3];
						case_pieces[0] = filter;
						case_pieces[1] = new SimpleConstantValuePiece(1,IDomain.NUMERIC);
						case_pieces[2] = new NullPiece();
						OperatorPiece subcase = new OperatorPiece(Operators.CASE,case_pieces);
						OperatorPiece copy = new OperatorPiece(op.getOpDef(),new IPiece[]{subcase});
						//
						return copy;
					}
				}
			}
		}
		// else
		throw new RenderingException("unable to render the ON operator");
	}
	
}
