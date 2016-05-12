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

import java.util.List;

import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.expression.ConstantValue;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.Operator;

public interface ExpressionScope {
	

	public ExpressionAST compose(ExpressionAST first, ExpressionAST second) throws ScopeException;

	/**
	 * create compose operator
	 * @param expression
	 * @param sub
	 * @return
	 * @throws ScopeException 
	 */
	public abstract ExpressionAST createCompose(ExpressionAST first, ExpressionAST second)
			throws ScopeException;

	/**
	 * @param fun
	 * @return
	 * @throws ScopeException 
	 */
	public abstract OperatorDefinition lookup(String fun) throws ScopeException;

	/**
	 * @param fun
	 * @return
	 * @throws ScopeException
	 */
	public abstract OperatorDefinition looseLookup(String fun) throws ScopeException;


	/**
	 * @param fun
	 * @param expr
	 * @return
	 */
	public abstract Operator createOperator(OperatorDefinition def);

	public abstract Operator createOperator(String name) throws ScopeException;

	/**
	 * @param fun
	 * @param expr
	 * @return
	 */
	public abstract Operator createOperator(String name, ExpressionAST unary) throws ScopeException;

	/**
	 * @param string
	 * @param expression
	 * @param sub
	 * @return
	 */
	public abstract ExpressionAST createOperator(String name, ExpressionAST left,
			ExpressionAST right) throws ScopeException;

	public abstract ExpressionAST createOperator(String name, List<ExpressionAST> args) throws ScopeException;

	/**
	 * @param first
	 * @param second
	 * @return
	 */
	public abstract ExpressionAST createSigmaOperator(ExpressionAST first,
			ExpressionAST second);

	/**
	 * @param image
	 * @return
	 */
	public abstract ConstantValue createNumericalConstantValue(String image);

	/**
	 * @param image
	 * @return
	 */
	public abstract ConstantValue createStringConstantValue(String image);
	
	/**
	 * create a date constant
	 * @param date
	 * @return
	 */
	public abstract ExpressionAST createDateConstantValue(String date);
	
	/**
	 * create a null value
	 * @return
	 */
	public abstract ExpressionAST createNullValue();

	/**
	 * create an undefined expression of type IUnknown
	 * @return
	 */
	public ExpressionAST createUndefinedExpression();

	/**
	 * apply the first expression to the current scope and return the composed scope if defined
	 * @param first
	 * @return
	 * @throws ScopeException
	 */
	public ExpressionScope applyExpression(ExpressionAST first) throws ScopeException;

	/**
	 * lookup an identifierType by its name
	 * @param image
	 * @return
	 * @throws ScopeException
	 */
	public IdentifierType lookupIdentifierType(String image) throws ScopeException;

	/**
	 * lookup an Object in this scope by its identifier and restricted to the identifierType.
	 * (use IdentifierType.DEFAULT if the type is not to be enforced)
	 * @param identifierType
	 * @param identifier
	 * @return
	 * @throws ScopeException
	 */
	public Object lookupObject(IdentifierType identifierType, String identifier) throws ScopeException;

	/**
	 * lookup an Composable Object in this scope by its identifier and restricted to the identifierType.
	 * A composable object is an object that can be composed with something (e.g 'composable'.'something')
	 * (use IdentifierType.DEFAULT if the type is not to be enforced)
	 * @param identifierType
	 * @param identifier
	 * @return
	 * @throws ScopeException
	 */
	public Object lookupComposableObject(IdentifierType identifierType, String identifier) throws ScopeException;

	/**
	 * create an valid expression which is a reference to the given object
	 * @param instance
	 * @return
	 * @throws ScopeException
	 */
	public ExpressionAST createReferringExpression(Object instance) throws ScopeException;

	/**
	 * parse the expression in this scope
	 * @param expression
	 * @return
	 * @throws ScopeException
	 */
	public ExpressionAST parseExpression(String expression) throws ScopeException;
	
	/**
	 * get the list of defined expression in this scope
	 * @return
	 */
	public List<ExpressionAST> getDefinitions();

	/**
	 * pretty-print an expression in that scope
	 * @param expression
	 * @return
	 */
	public String prettyPrint(ExpressionAST expression);
	
	/**
	 * validate the expression in this scope
	 * @param expression
	 * @return
	 */
	public ExpressionDiagnostic validateExpression(ExpressionAST expression);

}
