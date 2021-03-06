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
import com.squid.core.domain.analytics.AnalyticDomain;

public class PercentileOperatorDefinition extends AggregateOperatorDefinition {

	/**
	 * @param name
	 * @param id
	 */
	public PercentileOperatorDefinition(String name, String extendedId) {
		super(name, extendedId, PREFIX_POSITION, name, IDomain.NUMERIC);
		setDomain(AnalyticDomain.MANAGER.createMetaDomain(IDomain.NUMERIC));
		this.setCategoryType(OperatorDefinition.AGGR_TYPE);
	}
	
	public PercentileOperatorDefinition(String name, int id) {
		super(name, id);
		setDomain(AnalyticDomain.MANAGER.createMetaDomain(IDomain.NUMERIC));
		this.setCategoryType(OperatorDefinition.AGGR_TYPE);
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return ExtendedType.FLOAT;
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		return AnalyticDomain.MANAGER.createMetaDomain(IDomain.NUMERIC);
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("Compute the percentile using the first argument (double between 0 and 1) and the second argument if any as expression");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		type.add(IDomain.ANY);
		poly.add(type);
		type = new ArrayList<IDomain>();

		type.add(IDomain.ANY);
		type.add(IDomain.ANY);
		poly.add(type);

		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size() == 1 || imageDomains.size() == 2) {
			return OperatorDiagnostic.IS_VALID;
		} else {
			return new OperatorDiagnostic("Invalid number of parameters",
					getName());
		}
	}
	/*
	public String prettyPrint(String symbol, int position, String[] args, boolean showBrackets) {
		if (args.length == 2) {
			return "PERCENTILE("+ args[0]+","+ args[1]+")";
		}
		return "UNDEFINED()";
		
	}
	*/
}
