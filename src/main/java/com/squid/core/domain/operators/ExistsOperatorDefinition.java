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
import com.squid.core.domain.set.SetDomain;

public class ExistsOperatorDefinition 
extends UnaryLogicalOperatorDefinition {

	public ExistsOperatorDefinition(String name, int id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return ExtendedType.CONDITIONAL;
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		return IDomain.CONDITIONAL;
	}
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()!=1) {
			return new OperatorDiagnostic("takes one argument and only","EXISTS(conditional expression)");
		} else {
			IDomain arg0 = imageDomains.get(0);
			if (!(arg0.isInstanceOf(SetDomain.SET) && (arg0.isInstanceOf(IDomain.CONDITIONAL)||arg0.isInstanceOf(IDomain.OBJECT)))) {
				return new OperatorDiagnostic("invalid argument type, the argument must be a set and define a condition or reference an entity","EXISTS(conditional expression)");
			} else {
				return OperatorDiagnostic.IS_VALID;
			}
		}
	}

}
