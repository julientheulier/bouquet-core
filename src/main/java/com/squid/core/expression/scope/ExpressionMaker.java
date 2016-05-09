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
package com.squid.core.expression.scope;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.extensions.date.AddMonthsOperatorDefinition;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.Operators;
import com.squid.core.domain.vector.VectorDomain;
import com.squid.core.expression.Compose;
import com.squid.core.expression.ConditionalConstant;
import com.squid.core.expression.ConstantValue;
import com.squid.core.expression.DateConstant;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.NullExpression;
import com.squid.core.expression.NumericConstant;
import com.squid.core.expression.Operator;
import com.squid.core.expression.StringConstant;
import com.squid.core.expression.TimestampConstant;
 
/**
 * Helper class to create expressions in a more friendly way.
 * 
 * First you have to instanciate an ExpresisonMaker for a ExpressionScope or an IExpressionScopeProvider.
 * 
 * The ExpressionMaker allows to:
 *  * create Expression references from model domain objects using the ref() method
 *  * compose ExpressionPiece
 *  * create operators
 *  
 *  Note that the ExpressionMaker takes care of copying the Expression if needed... you can also use the static clone() method to easily copy an expression.
 * 
 * @author sfantino
 *
 */
public class ExpressionMaker {
	
	public static final ConstantValue ONE = CONSTANT(1.0);
	public static final ConstantValue ZERO = CONSTANT(0.0);
	
	public static Compose COMPOSE(ExpressionAST first, ExpressionAST second) throws ScopeException {
		return new Compose(first, second);
	}
	
	public static Operator op(OperatorDefinition opdef) {
		Operator op = new Operator(opdef);
		return op;
	}
	
	public static ExpressionAST op(OperatorDefinition opdef, ExpressionAST a1) {
		Operator op = new Operator(opdef);
		op.add((a1));
		return op;
	}
	
	public static ExpressionAST op(OperatorDefinition opdef, ExpressionAST a1, ExpressionAST a2) {
		Operator op = new Operator(opdef);
		op.add((a1));
		op.add((a2));
		return op;
	}
	
	public static ExpressionAST op(OperatorDefinition opdef, ExpressionAST a1, ExpressionAST a2, ExpressionAST a3) {
		Operator op = new Operator(opdef);
		op.add((a1));
		op.add((a2));
		op.add((a3));
		return op;
	}
	
	public static ExpressionAST op(OperatorDefinition opdef, List<ExpressionAST> as) {
		Operator op = new Operator(opdef);
		for (ExpressionAST a : as) {
			op.add((a));
		}
		return op;
	}
	
	public static ExpressionAST AND(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.AND,a1,a2);
	}

	public static ExpressionAST AND(List<ExpressionAST> joins) {
		if (joins.size()==1) {
			return joins.get(0);
		} else {
			return op(Operators.AND,joins);
		}
	}
	
	public static ExpressionAST OR(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.OR,a1,a2);
	}
	
	public static ExpressionAST ADD(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.ADD,a1,a2);
	}
	
	public static ExpressionAST MULT(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.MULTIPLY,a1,a2);
	}
	
	public static ExpressionAST MINUS(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.SUBTRACTION,a1,a2);
	}
	
	public static ExpressionAST DIV(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.DIVIDE,a1,a2);
	}

	/**
	 * group expression using parenthesis: identical to IDENTITY()
	 * @param a1
	 * @return
	 */
	public static ExpressionAST GROUP(ExpressionAST a1) {
		return op(Operators.IDENTITY,a1);
	}

	/**
	 * group expression using parenthesis
	 * @param a1
	 * @return
	 */
	public static ExpressionAST IDENTITY(ExpressionAST a1) {
		return op(Operators.IDENTITY,a1);
	}

	public static ExpressionAST EQUAL(ExpressionAST a1, ExpressionAST a2) {
		if (a1.getImageDomain().isInstanceOf(VectorDomain.DOMAIN) && a1 instanceof Operator
			&& a2.getImageDomain().isInstanceOf(VectorDomain.DOMAIN) && a2 instanceof Operator) 
		{
			// ok, transform VECTOR(ai)=VECTOR(bi) into AND(ai=bi)
			Operator v1 = (Operator)a1;
			Operator v2 = (Operator)a2;
			if (v1.getOperatorDefinition().equals(Operators.VECTOR)
				&& v2.getOperatorDefinition().equals(Operators.VECTOR)
				&& v1.getArguments().size()==v2.getArguments().size()) 
			{
				int size = v1.getArguments().size();
				ArrayList<ExpressionAST> equals = new ArrayList<ExpressionAST>();
				for (int i=0;i<size;i++) {
					equals.add(EQUAL(v1.getArguments().get(i),v2.getArguments().get(i)));
				}
				return AND(equals);
			}
		}
		return op(Operators.EQUAL,a1,a2);
	}

	public static ExpressionAST LESSOREQUAL(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.LESS_OR_EQUAL,a1,a2);
	}

	public static ExpressionAST LESS(ExpressionAST a1, ExpressionAST a2, boolean strict) {
		if (strict) {
			return op(Operators.LESS,a1,a2);
		} else {
			return op(Operators.LESS_OR_EQUAL,a1,a2);
		}
	}

	public static ExpressionAST GREATER(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.GREATER,a1,a2);
	}

	public static ExpressionAST GREATEROREQUAL(ExpressionAST a1, ExpressionAST a2) {
		return op(Operators.GREATER_OR_EQUAL,a1,a2);
	}

	public static ExpressionAST GREATER(ExpressionAST a1, ExpressionAST a2, boolean strict) {
		if (strict) {
			return op(Operators.GREATER,a1,a2);
		} else {
			return op(Operators.GREATER_OR_EQUAL,a1,a2);
		}
	}
	
	public static ExpressionAST ISNULL(ExpressionAST a1) {
		return op(Operators.ISNULL,a1);
	}

	public static ExpressionAST ISNOTNULL(ExpressionAST a1) {
		return op(Operators.IS_NOTNULL,a1);
	}
	
	public static ExpressionAST SUM(ExpressionAST a1) {
		return op(Operators.SUM,a1);
	}

	public static ExpressionAST MAX(ExpressionAST a1) {
		return op(Operators.MAX,a1);
	}

	public static ExpressionAST MIN(ExpressionAST a1) {
		return op(Operators.MIN,a1);
	}

	public static ExpressionAST CASE(List<ExpressionAST> args) {
		return op(Operators.CASE,args);
	}

	public static ExpressionAST CASE(ExpressionAST... args) {
		return op(Operators.CASE,Arrays.asList(args));
	}

	public static ExpressionAST CASE(ExpressionAST _if, ExpressionAST _then, ExpressionAST _else) {
		return op(Operators.CASE,_if,_then,_else);
	}

	public static ExpressionAST IFTHENELSE(ExpressionAST _if, ExpressionAST _then, ExpressionAST _else) {
		Operator _case = op(Operators.CASE);
		_case.add((_if));
		_case.add((_then));
		if (_else!=null) {
			// inline ?
			_case.add((_else));
		} else {
			_case.add(NULL());
		}
		return _case;
	}
	
	public static ExpressionAST NULL() {
		return new NullExpression();
	}

	public static ExpressionAST NOT(ExpressionAST test) {
		return op(Operators.NOT,test);
	}
	
	public static ExpressionAST PERCENTILE(ExpressionAST expr, double nTile) throws ScopeException {
		return op(Operators.PERCENTILE,CONSTANT(nTile),expr);
	}

	public static ExpressionAST COUNT() {
		return op(Operators.COUNT);
	}

	public static ExpressionAST COUNT(ExpressionAST a1) {
		return op(Operators.COUNT,a1);
	}
	
	public static ExpressionAST VECTOR(ExpressionAST... parameters) {
		Operator op = new Operator(Operators.VECTOR);
		for (ExpressionAST param : parameters) {
			op.add((param));
		}
		return op;
	}
	
	public static ExpressionAST VECTOR(List<ExpressionAST> parameters) {
		Operator op = new Operator(Operators.VECTOR);
		for (ExpressionAST param : parameters) {
			op.add((param));
		}
		return op;
	}

	public static ExpressionAST ZEROIFNULL(ExpressionAST expr) {
		return op(Operators.COALESCE,expr,CONSTANT(0));
	}

	public static ExpressionAST COUNT_DISTINCT(ExpressionAST a1) {
		return op(Operators.COUNT_DISTINCT,a1);
	}

	public static ExpressionAST IN(ExpressionAST expr, List<ExpressionAST> values) {
		return op(Operators.IN, expr, op(Operators.VECTOR, values));
	}
	
	// date
	
	public static ExpressionAST ADD_MONTHS(ExpressionAST date, ExpressionAST offset) {
		return op(OperatorScope.getDefault().lookupByExtendedID(AddMonthsOperatorDefinition.ADD_MONTHS),date,offset);
	}
	
	public static ConstantValue CONSTANT(double value) {
		return CONSTANT((Double)value,IDomain.NUMERIC);
	}
	
	public static List<ExpressionAST> CONSTANTS(List<Object> values) throws ScopeException {
		ArrayList<ExpressionAST> result = new ArrayList<ExpressionAST>(values.size());
		for (Object value : values) {
			result.add(ExpressionMaker.CONSTANT(value));
		}
		return result;
	}
	
	public static ConstantValue CONSTANT(String value) {
		return CONSTANT((String)value,IDomain.STRING);
	}
	
	public static ConstantValue CONSTANT(boolean value) {
		return CONSTANT((Boolean)value,IDomain.CONDITIONAL);
	}
	
	public static ConstantValue TRUE() {
		return CONSTANT(true);
	}
	
	public static ConstantValue FALSE() {
		return CONSTANT(false);
	}
	
	public static ConstantValue CONSTANT(Object value, IDomain domain) {
		if (value instanceof Date) {
			if (domain.isInstanceOf(IDomain.TIMESTAMP)) {
				TimestampConstant constant = new TimestampConstant((Date)value);
				return constant;
			}
			else if (domain.isInstanceOf(IDomain.DATE)) {
				DateConstant constant = new DateConstant((Date)value);
				return constant;
			}
		} else if (domain.isInstanceOf(IDomain.NUMERIC)) {
			Number number = (Number)value;
			NumericConstant constant = new NumericConstant(number.doubleValue());
			return constant;
		} else if (domain.isInstanceOf(IDomain.STRING)) {
			StringConstant constant = new StringConstant((String)value);
			return constant;
		} else if (value instanceof Boolean && domain.isInstanceOf(IDomain.CONDITIONAL)) {
			ConditionalConstant constant = new ConditionalConstant((Boolean)value);
			return constant;
		} 
		//else
			throw new RuntimeException("invalid constant");
	}
	
	public static ConstantValue CONSTANT(Object value) throws ScopeException {
		if (value instanceof Time) {
				TimestampConstant constant = new TimestampConstant((Date)value);
				return constant;
		} else if (value instanceof Date) {
			DateConstant constant = new DateConstant((Date)value);
			return constant;
		} else if (value instanceof Integer) {
			NumericConstant constant = new NumericConstant(((Integer)value).doubleValue());
			return constant;
		} else if (value instanceof Number) {
			Number number = (Number)value;
			NumericConstant constant = new NumericConstant(number.doubleValue());
			return constant;
		} else if (value instanceof String) {
			StringConstant constant = new StringConstant((String)value);
			return constant;
		} else if (value instanceof Boolean) {
			ConditionalConstant constant = new ConditionalConstant((Boolean)value);
			return constant;
		} else if (value==null) {
			// handling null value constant
			return new NullExpression();
		}
		//else
			throw new ScopeException("invalid constant");
	}

    /**
     * add brackets around the expression; make a copy if the expression is already contained
     * @param inner
     * @return
     */
    public static ExpressionAST wrapWithBrackets(ExpressionAST inner) {
    	Operator op = new Operator(Operators.IDENTITY);
		op.add((inner));
		return op;
    }
    
    /**
     * utility method to return a friendly name for an expression
     * @param expression
     * @return
     */
    /*
	public static String guessExpressionName(Expression expression) {
		if (expression instanceof ExpressionRef) {
			ExpressionRef ref = (ExpressionRef) expression;
			return ref.getModelReference().getName();
		}
		if (expression instanceof Compose) {
			Compose compose = (Compose) expression;
			return guessExpressionName(compose.getSecond());
		}else {
			IDomain source = expression.getSourceDomain();
			if (source.isInstanceOf(EntityDomain.ENTITY)) {
				MetaEntity entity = (MetaEntity) source.getAdapter(MetaEntity.class);
				try {
					if (entity!=null) {
						return entity.createExpressionScope().prettyPrint(expression, null);
					} else {
						return PrettyPrintExpression.Singleton.prettyPrint(expression);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	*/

}
