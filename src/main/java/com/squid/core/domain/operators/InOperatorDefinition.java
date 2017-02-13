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
import com.squid.core.domain.IDomainMetaDomain;

public class InOperatorDefinition extends ComparisonOperatorDefinition {

	public InOperatorDefinition(String name, int id, int position) {
		super(name, id, position);
		// TODO Auto-generated constructor stub
	}

	public InOperatorDefinition(String name, int id, String symbol) {
		super(name, id, symbol);
		// TODO Auto-generated constructor stub
	}
	
	public InOperatorDefinition(String name, int id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}

	public InOperatorDefinition(String name, String extendedID, int position,
			String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		// TODO Auto-generated method stub
		return super.validateParameters(imageDomains);
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		// ticket:3022
		if (sourceDomain.size()==2) {
			IDomain first = sourceDomain.get(0);
			if (!first.isInstanceOf(IDomain.ANY) && first.isInstanceOf(IDomain.META)) {
				IDomainMetaDomain meta = (IDomainMetaDomain)first;
				return meta.createMetaDomain(IDomain.CONDITIONAL);
			} else {
				return IDomain.CONDITIONAL;
			}
		} else {
			return IDomain.UNKNOWN;
		}
	}

}
