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
package com.squid.core.domain.aggregate;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * The quotient operator provides quotient operation on aggregate, e.g. to compute:
 *  SUM(AMOUNT) ON VOICE_TRAFFIC
 *  COUNT() ON (BID AND SUCCESFULL)
 *  
 *  
 * @author sfantino
 *
 */
public class QuotientOperatorDefinition extends OperatorDefinition {
	
	public static final String ID = AggregateOperatorRegistry.REGISTRY_BASE + ".Quotient";
	
	public static final String ERROR_MESSAGE_ARG_1 = "ON operator applies only to aggregate expression";
	public static final String ERROR_MESSAGE_ARG_2 = "ON operator defines a filter";
	public static final String ERROR_MESSAGE_HINT = "SUM(quantity) ON (quarter=Q1)";

	public QuotientOperatorDefinition(int categoryType) {
		super("ON",ID,INFIX_POSITION," ON ",IDomain.AGGREGATE, categoryType);
	}

	@Override
	public int getType() {
		return AGGREGATE_TYPE;
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("sum (arg1) with the condition arg2");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		// GROUPING takes a single expression as parameter
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain any = new DomainAny();
		type.add(AggregateDomain.DOMAIN); // && 		type.add(AggregateDomain.NUMERIC);
		type.add(IDomain.CONDITIONAL);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(AggregateDomain.NUMERIC);
		type.add(IDomain.CONDITIONAL);
		poly.add(type);

		return poly;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length==2) {
			return types[0];
		} else {
			return ExtendedType.UNDEFINED;
		}
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()==2) {
			return imageDomains.get(0);
		} else {
			return IDomain.UNKNOWN;
		}
	}
	
}
