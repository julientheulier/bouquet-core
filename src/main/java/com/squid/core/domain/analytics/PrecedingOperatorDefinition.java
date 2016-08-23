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
package com.squid.core.domain.analytics;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class PrecedingOperatorDefinition 
extends WindowingOperatorDefinition
{
	
	public static final String HINT = "PRECEDING(<number of rows>)";
	
	public PrecedingOperatorDefinition(String name, String ID) {
		super(name, ID);
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add(HINT);
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain double1 = new DomainNumericConstant(0.0);
		double1.setContentAssistLabel("double");
		type.add(double1);
		poly.add(type);
		return poly;
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()!=1) {
			return IDomain.UNKNOWN;
		} else {
			IDomain d = imageDomains.get(0);
			if (d.isInstanceOf(DomainNumericConstant.DOMAIN)) {
				DomainNumericConstant c = (DomainNumericConstant)d;
				double v = c.getValue();
				return WindowingDomainImp.createPrecedingDomain((int)v);
			} else {
				return IDomain.UNKNOWN;
			}
		}
	}

}
