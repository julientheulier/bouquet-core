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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.expression.Compose;
import com.squid.core.expression.ConstantValue;
import com.squid.core.expression.DateConstant;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.NullExpression;
import com.squid.core.expression.NumericConstant;
import com.squid.core.expression.Operator;
import com.squid.core.expression.StringConstant;
import com.squid.core.expression.UndefinedExpression;

public abstract class DefaultExpressionConstructor 
implements ExpressionScope
{
	
	public static final DateFormat ParseDateFormat = new SimpleDateFormat("d/M/y");
	public static final DateFormat FormatDateFormat = new SimpleDateFormat("d/M/yyyy");
	public static final DateFormat FormatTimestampFormat = new SimpleDateFormat("d/M/yyyy H:m:s");

    
    public OperatorScope getOperatorScope() {
    	return OperatorScope.getDefault();
    }
    
    /**
     * add brackets around the expression; make a copy if the expression is already contained
     * @param inner
     * @return
     */
    public ExpressionAST wrapWithBrackets(ExpressionAST inner) {
		Operator op = new Operator(OperatorScope.getDefault().lookupByID(IntrinsicOperators.IDENTITY));
		op.getArguments().add(inner);
		return op;
    }
    
    /**
     * the default is to use the compose operator
     */
    public ExpressionAST compose(ExpressionAST first, ExpressionAST second) throws ScopeException {
        Compose compose = new Compose(first,second);
        return compose;
    }
	
	/* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createCompose(com.squid.ldm.model.api.expression.Expression, com.squid.ldm.model.api.expression.Expression)
	 */
    public ExpressionAST createCompose(ExpressionAST first, ExpressionAST second) throws ScopeException {
        // check validity
        if (!first.getImageDomain().isInstanceOf(second.getSourceDomain())) {
        	throw new ScopeException("cannot compose: invalid types");
        }
        //
        return compose(first, second);
    }

    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#lookup(java.lang.String)
	 */
    public OperatorDefinition lookup(String fun) throws ScopeException {
        return getOperatorScope().lookupByName(fun.toUpperCase().trim());
    }

    public Set<OperatorDefinition> looseLookup(String fun) throws ScopeException {
        return getOperatorScope().looseLookupByName(fun.toUpperCase().trim());
    }

    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createOperator(com.squid.domain.model.operators.OperatorDefinition)
	 */
    public Operator createOperator(OperatorDefinition def) {
        Operator operator = new Operator(def);
        return operator;
    }
    
    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createOperator(java.lang.String)
	 */
    public Operator createOperator(String name) throws ScopeException {
        OperatorDefinition def = lookup(name);
        Operator operator = new Operator(def);
        return operator;
    }

    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createOperator(java.lang.String, com.squid.ldm.model.api.expression.Expression)
	 */
    public Operator createOperator(String name, ExpressionAST unary) throws ScopeException {
        OperatorDefinition def = lookup(name);
        OperatorDiagnostic diag = def.validateParameters(Collections.singletonList(unary.getImageDomain()));
        if (diag!=OperatorDiagnostic.IS_VALID) {
            String message = diag.getErrorMessage();
            int pos = diag.getPosition();
            if (pos==1) {
                message = message.replaceAll("#1", unary.prettyPrint());
            }
            throw new ScopeException(name+"():"+message+(diag.getHint()!=null?"\n"+diag.getHint():""));
        }
        Operator operator = new Operator(def);
        operator.getArguments().add(unary);
        return operator;
    }

    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createOperator(java.lang.String, com.squid.ldm.model.api.expression.Expression, com.squid.ldm.model.api.expression.Expression)
	 */
    public ExpressionAST createOperator(String name, ExpressionAST left, ExpressionAST right) throws ScopeException {
    	OperatorDefinition def = lookup(name);
    	List<IDomain> domains = new ArrayList<IDomain>(2);
    	domains.add(left.getImageDomain());
    	domains.add(right.getImageDomain());
        OperatorDiagnostic diag = def.validateParameters(domains);
        if (diag!=OperatorDiagnostic.IS_VALID) {
            String message = diag.getErrorMessage();
            int pos = diag.getPosition();
            if (pos==1) {
                message = message.replaceAll("#1", left.prettyPrint());
            }
            if (pos==2) {
                message = message.replaceAll("#2", right.prettyPrint());
            }
        	throw new ScopeException(name+"():"+message+(diag.getHint()!=null?"\n"+diag.getHint():""));
        }
        Operator operator = new Operator(def);
        operator.add(left);
        operator.add(right);
        return operator;
    }

    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createOperator(java.lang.String, java.util.List)
	 */
    public ExpressionAST createOperator(String name, List<ExpressionAST> args) throws ScopeException {
    	OperatorDefinition def = lookup(name);
    	List<IDomain> domains = null;
    	if (args!=null) {
	    	domains = new ArrayList<IDomain>(args.size());
	    	for (Iterator<ExpressionAST> iter = args.iterator();iter.hasNext();) {
	    		domains.add(iter.next().getImageDomain());
	    	}
    	} else {
    		domains = Collections.emptyList();
    	}
        OperatorDiagnostic diag = def.validateParameters(domains);
        if (diag!=OperatorDiagnostic.IS_VALID) {
            String message = diag.getErrorMessage();
            for (int pos=0;pos<args.size();pos++) {
            	message = message.replaceAll("#"+(pos+1), Matcher.quoteReplacement(args.get(pos).prettyPrint()));
            }
            throw new ScopeException(def.getName()+": "+message+(diag.getHint()!=null?"\nUsage: "+diag.getHint():""));
        }
        Operator operator = new Operator(def);
        if (args!=null)
        	operator.getArguments().addAll(args);
        return operator;
    }

    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createSigmaOperator(com.squid.ldm.model.api.expression.Expression, com.squid.ldm.model.api.expression.Expression)
	 */
    public ExpressionAST createSigmaOperator(ExpressionAST first, ExpressionAST second) {
        OperatorDefinition def = OperatorScope.getDefault().lookupByID(IntrinsicOperators.PLUS);
        Operator operator = new Operator(def);
        operator.getArguments().add(first);
        operator.getArguments().add(second);
        return operator;
    }
    
    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createNumericalConstantValue(java.lang.String)
	 */
    public ConstantValue createNumericalConstantValue(String image) {
    	Double value = new Double(image);
        NumericConstant expr = new NumericConstant(value);
        return expr;
    }

    /* (non-Javadoc)
	 * @see com.squid.ldm.model.api.expression.managers.ExpressionConstructor#createStringConstantValue(java.lang.String)
	 */
    public ConstantValue createStringConstantValue(String image) {
        StringConstant expr = new StringConstant(image.substring(1,image.length()-1));
        return expr;
    }
    
    public ExpressionAST createDateConstantValue(String value) {
    	try {
        	DateConstant expr = new DateConstant(parseDate(value));
	    	return expr;
		} catch (ParseException e) {
			UndefinedExpression undef = new UndefinedExpression(value);
			return undef;
		}
    }
    
    protected Date parseDate(String value) throws ParseException {
    	try {
			return ParseDateFormat.parse(value);
		} catch (ParseException e) {
			return DateFormat.getInstance().parse(value);
		}
    }
    
    @Override
    public ExpressionAST createNullValue() {
    	return new NullExpression();
    }

	@Override
	public ExpressionAST createUndefinedExpression() {
		return new UndefinedExpression("");
	}

}

