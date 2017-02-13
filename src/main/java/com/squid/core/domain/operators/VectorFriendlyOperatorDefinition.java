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
import com.squid.core.domain.vector.ProxyVectorDomain;
import com.squid.core.domain.vector.VectorDomain;

public class VectorFriendlyOperatorDefinition extends
		OrderedAnalyticOperatorDefinition {

	public VectorFriendlyOperatorDefinition(String name, int id, int position) {
		super(name, id, position);
	}

	public VectorFriendlyOperatorDefinition(String name, int id, String symbol) {
		super(name, id, symbol);
	}

	public VectorFriendlyOperatorDefinition(String name, int id) {
		super(name, id);
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		// vector
		if (sourceDomain.size()==1 && sourceDomain.get(0).isInstanceOf(VectorDomain.DOMAIN)) {
			IDomain domain = sourceDomain.get(0);
			if (domain instanceof ProxyVectorDomain) {
				ProxyVectorDomain vector = (ProxyVectorDomain)domain;
				return vector.getSubdomain();
			} 
		}
		// else
		return super.computeImageDomain(sourceDomain);
	}
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()==1 && imageDomains.get(0).isInstanceOf(VectorDomain.DOMAIN)) {
			IDomain domain = imageDomains.get(0);
			if (domain instanceof ProxyVectorDomain) {
				ProxyVectorDomain vector = (ProxyVectorDomain)domain;
				OperatorDiagnostic validate = super.validateParameters(imageDomains);
				if (validate.isValid() && vector.getSize()>1) {
					return OperatorDiagnostic.IS_VALID;
				}
			} 
			//else
			return new OperatorDiagnostic("Invalid VECTOR usage",1,getName()+"(VECTOR(a,b,...))");
		} else {
			return super.validateParameters(imageDomains);
		}
	}

}
