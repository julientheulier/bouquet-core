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

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.IDomain;

public class ComparisonOperatorDefinition 
extends ConditionalOperatorDefinition {

	public ComparisonOperatorDefinition(String name, int id, int position) {
		super(name, id, position);
		// TODO Auto-generated constructor stub
	}

	public ComparisonOperatorDefinition(String name, int id, String symbol) {
		super(name, id, symbol);
		// TODO Auto-generated constructor stub
	}

	public ComparisonOperatorDefinition(String name, int id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}

	public ComparisonOperatorDefinition(String name, String extendedID,
			int position, String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()!=2) {
			return new OperatorDiagnostic("takes exactly two arguments",prettyPrint(new String[]{"A","B"}, false));
		} else {
			IDomain first = imageDomains.get(0);
			IDomain second = imageDomains.get(1);
			if (first.isInstanceOf(second)) {
				return OperatorDiagnostic.IS_VALID;
			} else if (second.isInstanceOf(first)) {
				return OperatorDiagnostic.IS_VALID;
			} else if (first.isInstanceOf(IDomain.NUMERIC) && second.isInstanceOf(IDomain.NUMERIC)) {
				return OperatorDiagnostic.IS_VALID;
			} else {
				// handle special cases
				if (this.getId()==IntrinsicOperators.EQUAL || this.getId()==IntrinsicOperators.NOT_EQUAL) {
					// ticket:2991 - NUMERIC EQUAL STRING
					if (first.isInstanceOf(IDomain.NUMERIC) && second.isInstanceOf(IDomain.STRING)) {
						return OperatorDiagnostic.IS_VALID;
					} else if (second.isInstanceOf(IDomain.NUMERIC) && first.isInstanceOf(IDomain.STRING)) {
						return OperatorDiagnostic.IS_VALID;
					}
					// T377: handles boolean==0|1
					if (first.isInstanceOf(IDomain.BOOLEAN) && second.isInstanceOf(IDomain.NUMERIC)) {
						if (second instanceof DomainNumericConstant) {
							DomainNumericConstant constant = (DomainNumericConstant)second;
							double value = constant.getValue();
							if (value==0 || value==1) {
								return OperatorDiagnostic.IS_VALID;// legacy
							} else {
								return new OperatorDiagnostic("type mismatch error","cannot compare argument of type BOOLEAN with numeric constant "+value);
							}
						}
					}
				}
				return new OperatorDiagnostic("type mismatch error","cannot compare the two arguments of types "+first+","+second);
			}
		}
	}

}
