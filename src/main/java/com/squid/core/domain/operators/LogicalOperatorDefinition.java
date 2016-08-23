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

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.DomainConditional;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;

/**
 * Logical operator takes conditional arguments and produce conditional result
 * @author Serge Fantino
 *
 */
public class LogicalOperatorDefinition extends ConditionalOperatorDefinition {

	public LogicalOperatorDefinition(String name, String extendedID,
			int position, String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public LogicalOperatorDefinition(String name, int id, int position) {
		super(name, id, position);
		// TODO Auto-generated constructor stub
	}

	public LogicalOperatorDefinition(String name, int id, String symbol) {
		super(name, id, symbol);
		// TODO Auto-generated constructor stub
	}

	public LogicalOperatorDefinition(String name, int id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}


	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain cond = new DomainConditional();
		cond.setContentAssistLabel("condition");
		type.add(cond);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(cond);
		type.add(cond);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(cond);
		type.add(cond);
		type.add(cond);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(cond);
		type.add(cond);
		type.add(cond);
		type.add(cond);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(cond);
		type.add(cond);
		type.add(cond);
		type.add(cond);
		type.add(cond);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(cond);
		type.add(cond);
		type.add(cond);
		type.add(cond);
		type.add(cond);
		type.add(cond);
		poly.add(type);
		return poly;
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		if (sourceDomain.size()==1) {
			return sourceDomain.get(0);
		} else if (sourceDomain.size()==2) {
			IDomain first = sourceDomain.get(0);
			IDomain second = sourceDomain.get(1);
			return computeImageDomain(first,second);
		} else if (sourceDomain.size()>2) {
			IDomain first = sourceDomain.get(0);
			for (int i=1;i<sourceDomain.size();i++) {
				IDomain second = sourceDomain.get(i);
				first = computeImageDomain(first,second);
			}
			return first;
		} else {
			return IDomain.UNKNOWN;
		}
	}
	
	protected IDomain computeImageDomain(IDomain first, IDomain second) {
		// compute lowest domain
		IDomain lowest = IDomain.UNKNOWN;
		if (first.isInstanceOf(second)) {
			lowest = first;
		} else if (second.isInstanceOf(first)) {
			lowest = second;
		}
		// normalize lowest domain
		if (lowest.isInstanceOf(IDomain.META)) {
			return ((IDomainMetaDomain)lowest).createMetaDomain(IDomain.CONDITIONAL);
		} else if (lowest!=IDomain.UNKNOWN){
			return IDomain.CONDITIONAL;
		} else
			return IDomain.UNKNOWN;
	}

}
