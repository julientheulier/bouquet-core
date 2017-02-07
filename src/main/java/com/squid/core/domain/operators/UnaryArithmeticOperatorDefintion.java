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
package com.squid.core.domain.operators;

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.Domains;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.associative.AssociativeDomainInformation;

import java.util.ArrayList;
import java.util.List;

public class UnaryArithmeticOperatorDefintion extends ArithmeticOperatorDefinition {

	public UnaryArithmeticOperatorDefintion(String name, String extendedID, int position, String symbol, IDomain domain, int categoryType) {
		super(name, extendedID, position, symbol, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	public UnaryArithmeticOperatorDefintion(String name, String extendedID, int position, String symbol, IDomain domain, int categoryType, ExtendedType extendedType) {
		super(name, extendedID, position, symbol, domain, categoryType);
		this.extendedType = extendedType;
	}

	public UnaryArithmeticOperatorDefintion(String name, String extendedID, int position, String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public UnaryArithmeticOperatorDefintion(String name, int id, IDomain domain) {
		super(name, id, domain);
		// TODO Auto-generated constructor stub
	}

	public UnaryArithmeticOperatorDefintion(String name, int id, IDomain domain, int categoryType) {
		super(name, id, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	public UnaryArithmeticOperatorDefintion(String name, int id, int position, IDomain domain) {
		super(name, id, position, domain);
		// TODO Auto-generated constructor stub
	}

	public UnaryArithmeticOperatorDefintion(String name, int id, String symbol, IDomain domain) {
		super(name, id, symbol, domain);
		// TODO Auto-generated constructor stub
	}
	public UnaryArithmeticOperatorDefintion(String name, int id, String symbol, IDomain domain , int categoryType) {
		super(name, id, symbol, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	
	public UnaryArithmeticOperatorDefintion(String name, int id, int position, String symbol, IDomain domain) {
		super(name, id, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public UnaryArithmeticOperatorDefintion(String name, int id, int position, String symbol, IDomain domain, int categoryType) {
		super(name, id, position, symbol, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain any = new DomainAny();
		any.setContentAssistLabel("any");
		type.add(any);
		poly.add(type);
		return poly;
	}



}
