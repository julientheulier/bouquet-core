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

import java.util.Collections;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.analytics.AnalyticDomain;
import com.squid.core.domain.analytics.WindowingDomain;
import com.squid.core.domain.analytics.WindowingType;
import com.squid.core.domain.sort.DomainSort;

public class OrderedAnalyticOperatorDefinition extends
		AggregateOperatorDefinition {

	public OrderedAnalyticOperatorDefinition(String name, int id, int position) {
		super(name, id, position);
		// TODO Auto-generated constructor stub
	}

	public OrderedAnalyticOperatorDefinition(String name, int id, String symbol) {
		super(name, id, symbol);
		// TODO Auto-generated constructor stub
	}

	public OrderedAnalyticOperatorDefinition(String name, int id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}

	public OrderedAnalyticOperatorDefinition(String name, String extendedId,
			int position, String symbol, IDomain domain) {
		super(name, extendedId, position, symbol, domain);
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length<=1) {
			// non OA version
			return super.computeExtendedType(types);
		} else {
			// OA version
			return types[0];
		}
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		if (sourceDomain.size()==0) {
			return IDomain.UNKNOWN;
		} else if (sourceDomain.size()==1) {
			switch (getId()) {
				case IntrinsicOperators.SUM:
				case IntrinsicOperators.MIN:
				case IntrinsicOperators.MAX:
					// associative operator
					return AggregateDomain.MANAGER.createMetaDomain(sourceDomain.get(0), this);
				default:
					return AggregateDomain.MANAGER.createMetaDomain(sourceDomain.get(0));
			}
		} else {
			return AnalyticDomain.MANAGER.createMetaDomain(sourceDomain.get(0));
		}
	}
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()<=1) {
			return super.validateParameters(imageDomains);
		} else {
			return validateAnalyticParameters(imageDomains);
		}
	}
	
	protected String getHint() {
		return getName()+
		"([ <partitionBy> [, <partitionBy>]] , [ ASC|DESC <orderBy>) [ , ASC|DESC <orderBy> ]] , [ <windowing > ]";
	}
	
	/**
	 * Hypothesis: imageDomains .size>1
	 * @param imageDomains
	 * @return
	 */
	protected OperatorDiagnostic validateAnalyticParameters(List<IDomain> imageDomains) {
		int state = 0;// 0=aggregate, 1=partition, 2=order-by, 3=windowing, 4=end
		int i = 0;
		for (IDomain domain : imageDomains) {
			i++;
			if (state==0) {
				// validate single parameter
				List<IDomain> first = Collections.singletonList(domain);
				OperatorDiagnostic validateFirst = super.validateParameters(first);
				if (!validateFirst.isValid()) return validateFirst;
				//
				state++;
			}
			else if (state>0) {
				if (state==1) {
					if (domain.isInstanceOf(IDomain.NUMERIC) 
					|| domain.isInstanceOf(IDomain.TEMPORAL) 
					|| domain.isInstanceOf(IDomain.STRING)) 
					{
						// ok, it is a valid partition
					} else {
						// not a valid partition, try next
						state++;
					}
				}
				if (state==2) {
					if (domain.isInstanceOf(DomainSort.SORT)) {
						// ok, it is a valid sort
					} else {
						// not a valid sort, try next
						state++;
					}
				}
				if (state==3) {
					if (domain.isInstanceOf(WindowingDomain.DOMAIN)) {
						// ok, it is a windowing
						// check it is valid
						WindowingDomain w = (WindowingDomain)domain;
						if (w.getExpression().getType()==WindowingType.ROWS) {
							state++;// must be the last element
							continue;
						} else {
							// invalid window specs
				            return OperatorDiagnostic.invalidType(i, domain, "Windowing ROWS",getHint());
						}
					} else {
						// no more choices
                        return OperatorDiagnostic.invalidType(i, domain, "Windowing",getHint());
					}
				}
				if (state==4) {
					// not possible
                    return OperatorDiagnostic.unexpectedArgument(i,getHint());
				}
			}
		}
		// at last!
		return OperatorDiagnostic.IS_VALID;
	}
}
