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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.squid.core.domain.DomainUnknown;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.render.SQLSkin;

public class Compose extends NamedExpression {
	
	private List<ExpressionAST> body = null;
	
	private ExpressionAST head = null;
	
	/**
	 * apply the second expression to the first expression actually resulting in second(first)
	 * @param first
	 * @param second
	 * @throws ScopeException 
	 */
	public Compose(ExpressionAST first, ExpressionAST second) throws ScopeException {
		// first check that we can apply second to first
		IDomain image = first.getImageDomain();
		IDomain source = second.getSourceDomain();
		if (!image.isInstanceOf(source) && !source.equals(IDomain.NULL)) {
			throw new ScopeException("unable to compose expressions "+first.prettyPrint()+"."+second.prettyPrint()+" because of incompatible types: "+image.toString()+" o "+source.toString());
		}
		// ok, let's flatten
		this.body = Collections.unmodifiableList(addFlat(addFlat(new LinkedList<ExpressionAST>(),first),second));
		if (second instanceof Compose) {
			this.head = ((Compose)second).head;
		} else {
			this.head = second;
		}
	}

	private List<ExpressionAST> addFlat(List<ExpressionAST> list, ExpressionAST expression) {
		if (expression instanceof Compose) {
			list.addAll(((Compose)expression).body);
			return list;
		} else {
			list.add(expression);
			return list;
		}
	}

	protected Compose(List<ExpressionAST> body, ExpressionAST argument) {
		LinkedList<ExpressionAST> stuff = new LinkedList<ExpressionAST>(body);
		if (argument instanceof Compose) {
			stuff.addAll(((Compose)argument).body);
			this.head = ((Compose)argument).head;
		} else {
			stuff.add(argument);
			this.head = argument;
		}
		this.body = Collections.unmodifiableList(stuff);
	}

	@Override
	public ExtendedType computeType(SQLSkin skin) {
		return this.getHead().computeType(skin);
	}

	@Override
	public IDomain getImageDomain() {
		if (head!=null) {
			IDomain image = null;
			for (ExpressionAST segment : body) {
				image = (image==null)?segment.getImageDomain():image.compose(segment.getImageDomain());
			}
			return image;
		} else {
			return DomainUnknown.UNKNOWN;
		}
	}
	
	@Override
	public IDomain getSourceDomain() {
		if (body!=null && body.size()>0) {
			return body.get(0).getSourceDomain();
		} else {
			return DomainUnknown.UNKNOWN;
		}
	}
	
	/**
	 * return the head expression, e.g (AoBoC).head() = C
	 * @return
	 */
	public ExpressionAST getHead() {
		return head;
	}
	
	/**
	 * return the composition path without the head, e.g (AoBoC).tail() = (AoB)
	 * @return
	 */
	public ExpressionAST getTail() {
		if (body.size()==2) {
			return body.get(0);
		} else {
			LinkedList<ExpressionAST> linked = new LinkedList<ExpressionAST>(body);
			ExpressionAST head = linked.pollLast();
			return new Compose(linked, head);
		}
	}
	
	/**
	 * push the composition path inside the head if applicable (or just return this)
	 * @return
	 */
	public ExpressionAST push() {
		if (head instanceof Operator) {
			Operator op = ((Operator)head);
			LinkedList<ExpressionAST> arguments = new LinkedList<ExpressionAST>();
			for (ExpressionAST argument : op.getArguments()) {
				arguments.add(new Compose(body,argument));
			}
			return new Operator(op.getOperatorDefinition(),arguments);
		} else {
			return this;
		}
	}
	
	/**
	 * return the expression inner body, including the head
	 * @return
	 */
	public List<ExpressionAST> getBody() {
		return body;//return Collections.unmodifiableList(body);// it is already unmodifiable
	}

	@Override
	public String prettyPrint(PrettyPrintOptions options) {
		StringBuilder res = new StringBuilder();
		boolean first = true;
		boolean removedSpace=false;
		for (ExpressionAST item : body) {
			if (first 
				&& options!=null 
				&& options.getScope()!=null 
				&& item.getImageDomain().isInstanceOf(options.getScope())){
				removedSpace=true;
			}else{
				res.append(item.prettyPrint(options));
			}
			if(!(first && removedSpace) && item!=this.head) {
				res.append(PrettyPrintConstant.COMPOSE_TAG);
			}
			first= false;
		}
		return res.toString();
	}
	
	@Override
	public String toString() {
		return prettyPrint();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compose other = (Compose) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		return true;
	}
	
}
