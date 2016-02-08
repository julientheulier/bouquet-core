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
package com.squid.core.expression.parser;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.ExpressionRef;
import com.squid.core.expression.NumericConstant;
import com.squid.core.expression.Operator;
import com.squid.core.expression.TokenPosition;
import com.squid.core.expression.scope.ExpressionMaker;
import com.squid.core.expression.scope.ExpressionScope;
import com.squid.core.expression.scope.IdentifierType;
import com.squid.core.expression.scope.ScopeException;

/**
 * 
 * the base implementation of expression parser
 * 
 * @author serge fantino
 */
public abstract class ExpressionParser 
//extends ExpressionManager
{
	
	protected ExpressionScope scope;
    
    //abstract public boolean checkExpression(ExpressionScope scope) throws ParseException;
    
    //abstract public ConstantExpression parseConstant(ExpressionScope scope) throws ParseException;

    /**
     * Actually parse the expression against the scope;
     * Note that the correct way to parse an expression is to use the ExpressionScope.parseExpression() method, which takes care of creating the right parser.
     * @return
     */
    abstract public ExpressionAST parseExpression() throws ParseException;
    
    protected Token removeSurrounding(Token token) {
    	String identifier = removeSurrounding(token.image);
    	token.image = identifier;
    	return token;
    }
    
    protected ExpressionAST bindIdentifier(ExpressionAST source, Token token, IdentifierType type) {
    	//System.out.println("token pos = L"+token.beginLine+"C"+token.beginColumn+" - L"+token.endLine+"C"+token.endColumn+" : "+token.image);
    	if (source instanceof ExpressionRef) {
    		ExpressionRef ref = (ExpressionRef)source;
    		ref.setTokenPosition(new TokenPosition(token.beginLine, token.beginColumn, token.endColumn, type));
    	}
    	return source;
    }
    
    protected ExpressionAST bindPrefixedIdentifier(ExpressionAST source, Token token, IdentifierType type) {
    	//System.out.println("token pos = L"+token.beginLine+"C"+token.beginColumn+" - L"+token.endLine+"C"+token.endColumn+" : "+token.image);
    	if (source instanceof ExpressionRef) {
    		ExpressionRef ref = (ExpressionRef)source;
    		// offset the position to span the prefix, e.g: @'someID'
    		ref.setTokenPosition(new TokenPosition(token.beginLine, token.beginColumn-1, token.endColumn, type));
    	}
    	return source;
    }
    
    /**
     * in case of a typed identifier we must correct the position
     * @param source
     * @param token
     * @param type
     * @return
     */
    protected ExpressionAST bindTypedIdentifier(ExpressionAST source, Token token, IdentifierType type) {
    	//System.out.println("token pos = L"+token.beginLine+"C"+token.beginColumn+" - L"+token.endLine+"C"+token.endColumn+" : "+token.image);
    	if (source instanceof ExpressionRef) {
    		ExpressionRef ref = (ExpressionRef)source;
    		// offset the position to span the entire definition, e.g: [type:'identifier']
    		int begin = token.beginColumn-type.getToken().length()-2;// e.g [type:
    		int end = token.endColumn+1;// e.g ]
    		ref.setTokenPosition(new TokenPosition(token.beginLine, begin, end, type));
    	}
    	return source;
    }
    
    protected String removeSurrounding(String identifier) {
    	if (identifier!=null&&identifier.length()>1) {
    		return identifier.substring(1, identifier.length()-1);
    	} else
    		return identifier;
    }
    
    protected void setOperator(Operator op, ExpressionAST first, ExpressionAST second) throws ScopeException {
    	op.getArguments().add(first);
    	op.getArguments().add(second);
    	// validate...
    	ArrayList<IDomain> domains = new ArrayList<IDomain>();
    	domains.add(first.getImageDomain());
    	domains.add(second.getImageDomain());
		OperatorDiagnostic diag = op.getOperatorDefinition().validateParameters(domains);
        if (diag!=OperatorDiagnostic.IS_VALID) {
            String message = diag.getErrorMessage();
            int pos = diag.getPosition();
            if (pos==1) {
                message = message.replaceAll("#1", first.prettyPrint());
            }
            if (pos==2) {
                message = message.replaceAll("#2", second.prettyPrint());
            }
            throw new ScopeException("failed to validate "+op.prettyPrint()+" <b>caused by</b>:<br>"+message+(diag.getHint()!=null?": "+diag.getHint():""));
        }
    }
    
    public ExpressionAST buildExpression(ArrayList<ExpressionOperatorPair> pairs) throws ScopeException {
    	if (pairs.isEmpty()) {
    		return scope.createUndefinedExpression();
    	} else if (pairs.size()==1) {
    		return pairs.get(0).getLeft();
    	} else {
    		ExpressionOperatorPair first = pairs.remove(0);
    		ExpressionOperatorPair second = pairs.remove(0);
    		if (pairs.isEmpty()) {
    			setOperator(first.getOp(), first.getLeft(), second.getLeft());
    			return first.getOp();
    		} else {
    			Operator op1 = first.getOp();
    			Operator op2 = second.getOp();
    			//
    			if (op1.getOperatorDefinition().getPrecedenceOrder()<=op2.getOperatorDefinition().getPrecedenceOrder()) {
    				//op1.getArguments().add(first.getLeft());
    				//op1.getArguments().add(second.getLeft());
    				setOperator(op1,first.getLeft(),second.getLeft());
    				pairs.add(0,new ExpressionOperatorPair(op1,second.getOp()));
    				return buildExpression(pairs);
    			} else {
    				pairs.add(0,second);
    				ExpressionAST right = buildExpression(pairs);
    				//op1.getArguments().add(first.getLeft());
    				//op1.getArguments().add(right);
    				setOperator(op1,first.getLeft(),right);
    				return op1;
    			}
    		}
    	}
    }

	protected abstract ExpressionAST createRawOperator(int operatorKind, ExpressionAST expression, ExpressionAST sub) throws ScopeException;

    /**
     * that one support user-defined infix operators
     * @param name
     * @return
     * @throws ScopeException
     */
    protected Operator createInfixOperator(String name) throws ScopeException {
    	OperatorDefinition opDef = scope.lookup(name.trim());
    	if (opDef.getPosition()!=OperatorDefinition.INFIX_POSITION) {
    		throw new ScopeException("Syntax error: cannot use operator '"+name+"' as infix operator");
    	} else {
    		Operator operator = scope.createOperator(opDef);
    		return operator;
    	}
    }
    
    /**
     * suppot built-in infix operators
     * @param operatorKind: the Token identifier
     * @return
     * @throws ScopeException
     */
	protected abstract Operator createInfixOperator(int operatorKind) throws ScopeException;
	
	/**
	 * create regular operator
	 * @param fun
	 * @param args
	 * @param on_filter
	 * @return
	 * @throws ScopeException 
	 */
	protected ExpressionAST createOperator(ExpressionScope scope, String fun, List<ExpressionAST> args) throws ScopeException {
		return scope.createOperator(fun,args);
	}
	
	/**
	 * create regular unary operator
	 * @param fun
	 * @param args
	 * @param on_filter
	 * @return
	 * @throws ScopeException 
	 */
	protected ExpressionAST createOperator(ExpressionScope scope, String fun, ExpressionAST arg) throws ScopeException {
		if (fun.equals("MINUS")) {
			if (arg instanceof NumericConstant) {
				NumericConstant n = (NumericConstant)arg;
				return ExpressionMaker.CONSTANT(-n.getIntrinsicValue());
			}
		}
		return scope.createOperator(fun,arg);
	}
	
	protected ExpressionAST createConstant(ExpressionScope scope, boolean value) {
		return ExpressionMaker.CONSTANT(value);
	}
	
	protected ExpressionAST createQuery(ExpressionScope cope, ExpressionAST from, List<QueryTerm> terms) throws ScopeException {
		ExpressionAST query = from;
		for (QueryTerm term : terms) {
			query = scope.createCompose(query, term.getLeft(), term.getTerm());
		}
		return query;
	}
    	
}
