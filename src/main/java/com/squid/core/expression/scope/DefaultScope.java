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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.expression.Compose;
import com.squid.core.expression.ExpressionAST;
import com.squid.core.expression.PrettyPrintOptions;
import com.squid.core.expression.parser.ExpressionParser;
import com.squid.core.expression.parser.ExpressionParserImp;
import com.squid.core.expression.parser.ParseException;
import com.squid.core.expression.parser.TokenMgrError;

public class DefaultScope 
extends DefaultExpressionConstructor
implements ExpressionScope 
{
    
    public DefaultScope() {
    }

    public ExpressionScope applyExpression(ExpressionAST expression) throws ScopeException {
        return this;
    }
    
    /**
     * the default is to use the compose operator
     */
    public ExpressionAST compose(ExpressionAST first, ExpressionAST second) throws ScopeException {
        return new Compose(first, second);
    }
    
    public IdentifierType lookupIdentifierType(String token) throws ScopeException {
    	final String normalize = token.toLowerCase();
    	IdentifierType type = IdentifierTypeManager.INSTANCE.lookup(normalize);
    	if (type!=null) {
    		return type;
    	} else {
    		throw new ScopeException("unknow identifier type");
    	}
    }
    
    /* (non-Javadoc)
     * @see com.squid.ldm.model.scopes.DomainScope#getDefinitions()
     */
    public List<ExpressionAST> getDefinitions() {
    	ArrayList<Object> definitions = new ArrayList<Object>();
        buildDefinitionList(definitions);
        //
        ArrayList<ExpressionAST> exprs = new ArrayList<ExpressionAST>(definitions.size());
        //
        for (Object item : definitions) {
            try {
	            ExpressionAST expr = createReferringExpression(item);
	            if (expr!=null) exprs.add(expr);
            } catch (ScopeException e) {
            	// ignore
            }
        }
        //
        return exprs;
    }

    /**
     * compute a list of Object that can be inserted in the current context;
     * the object returned must be able to be converted to Expressions using the ExpressionFactory
     * @return
     */
    public void buildDefinitionList(List<Object> definitions) {
    	// override to add new definitions
    }
    
    public ExpressionAST createReferringExpression(Object object) throws ScopeException {
    	if (object instanceof OperatorDefinition) {
    		return createOperator((OperatorDefinition)object);
    	} else if (object instanceof ExpressionAST) {
    		return (ExpressionAST)object;
    	} else {
    		throw new ScopeException("cannot reference object :"+object.toString());
    	}
    }
    
    /*
    protected void filterAddAllOperator(List add, List<OperatorDefinition> operators, int position) {
    	for (Iterator<OperatorDefinition> iter = operators.iterator();iter.hasNext();) {
    		OperatorDefinition op = iter.next();
    		if ((op.getPosition()&position)!=0&&op.getName().equals(op.getSymbol())) {
    			add.add(op);
    		}
    	}
    }
    */

	@Override
	public Object lookupObject(IdentifierType identifierType, String identifier) throws ScopeException {
		throw new ScopeException("cannot find object '"+identifier+"'");
	}
	
	@Override
	public Object lookupComposableObject(IdentifierType identifierType,
			String identifier) throws ScopeException {
		return lookupObject(identifierType, identifier);
	}
    
    /**
     * default implementation for parsing an expression
     */
    public ExpressionAST parseExpression(String expression) throws ScopeException {
        if (expression==null) {
            throw new ScopeException("null expression");
        }
    	ExpressionParser parser = createParser(expression);
	    try {
	    	ExpressionAST parse = parser.parseExpression();
	        return parse;
	    } catch (TokenMgrError e) {
	        throw new ScopeException("failed to parse expression:\n" + genCode(expression) +"\n", e);
	    } catch (ScopeException e) {
	        throw new ScopeException("failed to parse expression:\n" + genCode(expression) + "\n"+e.getLocalizedMessage());
	    } catch (ParseException e) {
	    	if (e.currentToken!=null && !e.currentToken.equals("")) {
	    		throw new ScopeException("failed to parse expression:\n" + genCode(expression) + "\n at token '"+e.currentToken+"' \n", e);
	    	} else {
	    		throw new ScopeException("failed to parse expression:\n" + genCode(expression) + "\n", e);
	    	}
	    }
    }
    
    private String genCode(String expression) {
    	try {
    		BufferedReader bufReader = new BufferedReader(new StringReader(expression));
    		String line=null;
    		StringBuffer out = new StringBuffer();
    		out.append("---\n");
    		while( (line=bufReader.readLine()) != null )
    		{
    			out.append(line+"\n");
    		}
    		out.append("\n---");
    		return out.toString();
    	} catch (IOException e) {
    		return "---\n"+expression+"\n---";
    	}
    }
    
    /**
     * override to provide a custom parser;
     * the default implementation use the built-in ExpressionParser.
     * @return
     */
    protected ExpressionParser createParser(String expression) {
    	return new ExpressionParserImp(this,expression);
    }
    
	@Override
	public String prettyPrint(ExpressionAST expression) {
		return prettyPrint(expression, null);
	}
    
	@Override
	public String prettyPrint(ExpressionAST expression, PrettyPrintOptions options) {
		return expression.prettyPrint(options);
	}
    
    @Override
    public ExpressionDiagnostic validateExpression(ExpressionAST expression) {
    	IDomain image = expression.getImageDomain();
    	if (image!=IDomain.UNKNOWN) {
    		return ExpressionDiagnostic.IS_VALID;
    	} else {
    		return new ExpressionDiagnostic("the expression is not well defined (unknown type)");
    	}
    }
    

}
