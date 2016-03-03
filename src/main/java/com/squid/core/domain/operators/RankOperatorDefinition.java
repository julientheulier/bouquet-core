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

import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.analytics.AnalyticDomain;
import com.squid.core.domain.sort.DomainSort;

/**
 * @author serge fantino
 *
 */
public class RankOperatorDefinition 
//extends AggregateOperatorDefinition {
extends OrderedAnalyticOperatorDefinition {

	public static final String RANK_ID = "com.squid.domain.model.operators.RANK";
	public static final String ROWNUMBER_ID = "com.squid.domain.model.operators.ROWNUMBER";

	/**
	 * @param name
	 * @param id
	 */
	public RankOperatorDefinition(String name, String extendedId) {
		super(name, extendedId, PREFIX_POSITION, name, IDomain.NUMERIC);
	}
	
	protected RankOperatorDefinition(String name, int id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return ALGEBRAIC_TYPE;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return ExtendedType.INTEGER;
		//return super.computeExtendedType(types);
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		return AnalyticDomain.DOMAIN.createMetaDomain(IDomain.NUMERIC);
		//return super.computeImageDomain(sourceDomain);
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		boolean orderBy = false;
		int i=0;
		for (IDomain domain : imageDomains) {
			i++;
			if (!orderBy) {
				//loi Ticket #1200 and Ticket #1215
				//Rank function rank(expr,sort) can handle not only numeric as expr (first arg)
				//but also string & date or timestamp
				if (domain.isInstanceOf(IDomain.NUMERIC)
						|| domain.isInstanceOf(IDomain.STRING)
						|| domain.isInstanceOf(IDomain.DATE)
						|| domain.isInstanceOf(IDomain.TIMESTAMP)) {
					// ok
					continue;
				} else {
					orderBy = true;
				}
			}
			if (orderBy) {
				if (!domain.isInstanceOf(DomainSort.DOMAIN)) {
					return OperatorDiagnostic.invalidType(i,domain,"Sort expression","([partitionBy,[partitionBy,]ASC|DESC(orderBy)[,ASC|DESC(orderBy)]");
				}
			}
		}
		//
		return OperatorDiagnostic.IS_VALID;
	}

}
