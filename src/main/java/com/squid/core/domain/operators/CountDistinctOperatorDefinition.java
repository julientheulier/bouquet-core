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
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.analytics.AnalyticDomain;

public class CountDistinctOperatorDefinition
extends AggregateOperatorDefinition {

	public CountDistinctOperatorDefinition(String name, int id) {
		super(name, id);
		setParamCount(1);
	}

	@Override
	public ListContentAssistEntry getListContentAssistEntry() {
		if (super.getListContentAssistEntry() == null) {

			List<String> descriptions = new ArrayList<String>();
			List types = getParametersTypes();
			for(int i = 0; i<types.size();i++){
				descriptions.add("Returns the number of distinct rows");
			}
			ListContentAssistEntry entry = new ListContentAssistEntry(descriptions, types);
			setListContentAssistEntry(entry);

		}
		return super.getListContentAssistEntry();
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain any1 = new DomainAny();
		any1.setContentAssistLabel("any");
		any1.setContentAssistProposal("${1:any}");

		type.add(any1);
		poly.add(type);

		return poly;
	}


	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length<=1) {
			return new ExtendedType(AggregateDomain.MANAGER.createMetaDomain(IDomain.NUMERIC),ExtendedType.INTEGER);
		} else {
			return new ExtendedType(AnalyticDomain.MANAGER.createMetaDomain(IDomain.NUMERIC),ExtendedType.INTEGER);
		}
	}

    @Override
    public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		if (sourceDomain.size()<=1) {
			return AggregateDomain.MANAGER.createMetaDomain(IDomain.NUMERIC, Operators.SUM);
		} else {
			return AnalyticDomain.MANAGER.createMetaDomain(IDomain.NUMERIC);
		}
    }

}
