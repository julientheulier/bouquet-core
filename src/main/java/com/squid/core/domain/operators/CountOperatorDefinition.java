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

import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.analytics.AnalyticDomain;

public class CountOperatorDefinition
extends OrderedAnalyticOperatorDefinition {

	public CountOperatorDefinition(String name, int id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		if (sourceDomain.size()<=1) {
			return AggregateDomain.MANAGER.createMetaDomain(IDomain.NUMERIC, OperatorScope.getDefault().lookupByID(IntrinsicOperators.SUM));
		} else {
			return AnalyticDomain.MANAGER.createMetaDomain(IDomain.NUMERIC);
		}
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		if  (getId()==OperatorScope.COUNT_DISTINCT) {
			hint.add("Returns the number of distinct rows");
		} else {
			hint.add("Returns the number of rows");
		}
		return hint;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()==0 ) {
			// count(*)
			return getId()==OperatorScope.COUNT_DISTINCT? new OperatorDiagnostic(
					"Invalid count distinct",
					getName()
					+ ": must have a parameter"):OperatorDiagnostic.IS_VALID;
		} else {
			return super.validateParameters(imageDomains);
		}
	}


	@Override
	public String getSymbol() {
		if (this.getId() == IntrinsicOperators.COUNT) {
			return "COUNT";
		} else {
			return "COUNT_DISTINCT";
		}
	}

}
