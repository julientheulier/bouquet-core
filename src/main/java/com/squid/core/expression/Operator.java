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
package com.squid.core.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.squid.core.domain.DomainProduct;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.SQLSkin;

public class Operator extends NamedExpression {
		
	/**
	 * the actual operator definition as defined by the domain
	 */
	protected OperatorDefinition operatorDefinition = null;

	/**
	 * the list of actual arguments for the operator
	 */
	protected List<ExpressionAST> arguments;
	
	public Operator(OperatorDefinition operatorDefinition) {
		this.operatorDefinition = operatorDefinition;
		this.arguments = new ArrayList<ExpressionAST>();
	}


	public Operator(OperatorDefinition operatorDefinition, List<ExpressionAST> arguments) {
		this.operatorDefinition = operatorDefinition;
		this.arguments = arguments;
	}
	
	public OperatorDefinition getOperatorDefinition() {
		return operatorDefinition;
	}

	public List<ExpressionAST> getArguments() {
		if (arguments == null) {
			arguments = new ArrayList<ExpressionAST>();
		}
		return arguments;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Operator) {
			final Operator op = (Operator) obj;
			if (this.getOperatorDefinition() == null
					|| !this.getOperatorDefinition().equals(
							op.getOperatorDefinition())) {
				return false;
			}
			if (this.getArguments().size() != op.getArguments().size()) {
				return false;
			}
			for (int i = 0; i < this.getArguments().size(); i++) {
				if (this.getArguments().get(i) == null
						|| !this.getArguments().get(i).equals(
								op.getArguments().get(i))) {
					return false;
				}
			}
			return true;// yeah
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += this.getOperatorDefinition() != null ? this
				.getOperatorDefinition().hashCode() : 0;
		for (int i = 0; i < this.getArguments().size(); i++) {
			hash += this.getArguments().get(i) != null ? this.getArguments()
					.get(i).hashCode() : 0;
		}
		return hash;
	}

	//////////////////////////////////////////////////////////////////////////////////

	@Override
	public IDomain getSourceDomain() {
		if (getArguments().size() == 0) {
			return IDomain.NULL;
		} if (getArguments().size() == 1) {
			return getArguments().get(0).getSourceDomain();
		} else if (getArguments().size() == 2) {
			IDomain d0 = getArguments().get(0).getSourceDomain();
			IDomain d1 = getArguments().get(1).getSourceDomain();
			//
			if (d0==IDomain.NULL || d0.isInstanceOf(d1)) {
				return d1;
			} else if (d1==IDomain.NULL || d1.isInstanceOf(d0)) {
				return d0;
			} else {
				return DomainProduct.createDomain(d0,d1);
			}
		} else {
			HashSet<IDomain> temp = new HashSet<IDomain>();
			for (ExpressionAST expr : getArguments()) {
				IDomain source = expr.getSourceDomain();
				if (source != IDomain.NULL && !temp.contains(source)) {
					temp.add(source);
				}
			}
			return reduceDomainProduct(new ArrayList<IDomain>(temp));
		}
	}
	
	/**
	 * take a list of NON NULL IDomain, and return the product if cannot reduce, or the least common domain if possible
	 * @param domains
	 * @return
	 */
	protected IDomain reduceDomainProduct(List<IDomain> domains) {
        if (domains.size() == 1) {
            return domains.iterator().next();
        } else if (domains.size() == 0) {
            return IDomain.NULL;
        } else if (domains.size() == 2) {
            IDomain d0 = domains.get(0);
            IDomain d1 = domains.get(1);
            //
            if (d0==IDomain.NULL || d0.isInstanceOf(d1)) {
                return d1;
            } else if (d1==IDomain.NULL || d1.isInstanceOf(d0)) {
                return d0;
            } else {
                return DomainProduct.createDomain(d0,d1);
            }
        } else {
            return DomainProduct.createDomain(domains);
        }
	}


	@Override
	public ExtendedType computeType(SQLSkin skin) {
		final OperatorDefinition opDef = this.getOperatorDefinition();
		ExtendedType[] types = new ExtendedType[this.getArguments().size()];
		int i=0;
		for (ExpressionAST argument : this.getArguments()) {
			types[i++] = argument.computeType(skin);
		}
		return opDef.computeExtendedType(types);
	}

	@Override
	public IDomain getImageDomain() {
		OperatorDefinition op = getOperatorDefinition();
		if (op == null) {
			return IDomain.UNKNOWN;
		} else {
			ArrayList<IDomain> domains = new ArrayList<IDomain>();
			for (Iterator<ExpressionAST> iter = getArguments().iterator(); iter
					.hasNext();) {
				ExpressionAST expr = iter.next();
				domains.add(expr.getImageDomain());
			}
			return op.computeImageDomain(domains);
		}
	}

	public void add(ExpressionAST argument) {
		this.arguments.add(argument);
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(operatorDefinition.getSymbol()+"(");
		boolean first = true;
		for (ExpressionAST expr : arguments) {
			if (!first) result.append(","); else first=false;
			result.append(expr.toString());
		}
		result.append(")");
		return result.toString();
	}
	
	@Override
	public String prettyPrint() {
		int size = arguments.size();
        String[] args = new String[size];
        boolean show_bracket = size>1;
        for (int i=0;i<size;i++) {
        	ExpressionAST arg = arguments.get(i);
        	if (!show_bracket&&arg instanceof Operator) {
        		show_bracket = true;
        	}
            args[i] = arg.prettyPrint();
        }
        if (operatorDefinition==null) {
        	return "?";
        } else
        	return operatorDefinition.prettyPrint(args,show_bracket);
	}

}
