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
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;

/**
 * A conditional operator return a CONDITIONAL expression (true or false)
 * @author sfantino
 *
 */
public class ConditionalOperatorDefinition extends AlgebraicOperatorDefinition {

    public ConditionalOperatorDefinition(String name, String extendedID,
			int position, String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public ConditionalOperatorDefinition(String name, int id) {
		super(name, id, IDomain.CONDITIONAL);
		this.setCategoryType(OperatorDefinition.LOGICAL_TYPE);
			}

	public ConditionalOperatorDefinition(String name, int id, int position) {
		super(name, id, position, IDomain.CONDITIONAL);
		this.setCategoryType(OperatorDefinition.LOGICAL_TYPE);
			}

	public ConditionalOperatorDefinition(String name, int id, String symbol) {
		super(name, id, symbol, IDomain.CONDITIONAL);
		this.setCategoryType(OperatorDefinition.LOGICAL_TYPE);
	}

	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return fixExtendedTypeDomain(ExtendedType.BOOLEAN,types);
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		if (sourceDomain.size()==2) {
			IDomain first = sourceDomain.get(0);
			IDomain second = sourceDomain.get(1);
			if (first.isInstanceOf(IDomain.ANY)){
				return IDomain.UNKNOWN;
			}else if (first.isInstanceOf(IDomain.META)) {
				IDomainMetaDomain meta = (IDomainMetaDomain)first;
				return meta.createMetaDomain(IDomain.CONDITIONAL);
			} else if (second.isInstanceOf(IDomain.META)) {
				IDomainMetaDomain meta = (IDomainMetaDomain)second;
				return meta.createMetaDomain(IDomain.CONDITIONAL);
			} else {
				return IDomain.CONDITIONAL;
			}
		} else if (sourceDomain.size()==1) {
			IDomain first = sourceDomain.get(0);
			if(first.isInstanceOf(IDomain.ANY)){
				return IDomain.UNKNOWN;
			} else if (first.isInstanceOf(IDomain.META)) {
				IDomainMetaDomain meta = (IDomainMetaDomain)first;
				return meta.createMetaDomain(IDomain.CONDITIONAL);
			} else {
				return IDomain.CONDITIONAL;
			}
		} else {
			return IDomain.UNKNOWN;
		}
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
