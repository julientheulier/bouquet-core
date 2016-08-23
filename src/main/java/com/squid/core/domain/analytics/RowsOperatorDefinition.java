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

import com.squid.core.domain.DomainString;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class RowsOperatorDefinition extends WindowingOperatorDefinition {
	
	private static final String HINT = "ROWS(<preceding window boundary>) or ROWS(<preceding window boundary>,<following window boundary>) where the window boundary is defined using either: UNBOUNDED(), PRECEDING(n),CURRENT(),FOLLOWING(n)";

	public RowsOperatorDefinition(String name, String ID) {
		super(name,ID);
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add(HINT);
		return hint;
	}


	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		type.add(WindowingDomain.DOMAIN);
		poly.add(type);
		type = new ArrayList<IDomain>();

		type.add(WindowingDomain.DOMAIN);
		type.add(WindowingDomain.DOMAIN);
		poly.add(type);

		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()<1 || imageDomains.size()>2) {
			return new OperatorDiagnostic("invalid use of "+getName()+"()",HINT);
		}
		if (imageDomains.size()>=1) {
			// check preceding domain
			IDomain d = imageDomains.get(0);
			if(d.isInstanceOf(IDomain.ANY)){
				return new OperatorDiagnostic("invalid use of "+getName()+"()",HINT);
			}
			if (d.isInstanceOf(WindowingDomain.DOMAIN)) {
				WindowingDomain w = (WindowingDomain)d;
				switch (w.getExpression().getType()) {
				case CURRENT:
				case PRECEDING:
				case UNBOUNDED:
					return OperatorDiagnostic.IS_VALID;
				case FOLLOWING:
				default:
					return new OperatorDiagnostic("invalid use of "+getName()+"()",HINT);
				}
			} else {
				return new OperatorDiagnostic("invalid use of "+getName()+"()",HINT);
			}
		}
		if (imageDomains.size()==2) {
			// check following domain
			IDomain d = imageDomains.get(1);
			if (d.isInstanceOf(WindowingDomain.DOMAIN)) {
				WindowingDomain w = (WindowingDomain)d;
				switch (w.getExpression().getType()) {
				case CURRENT:
				case PRECEDING:
				case UNBOUNDED:
					return OperatorDiagnostic.IS_VALID;
				default:
					return new OperatorDiagnostic("invalid use of "+getName()+"()",HINT);
				}
			} else {
				return new OperatorDiagnostic("invalid use of "+getName()+"()",HINT);
			}
		}
		//
		return OperatorDiagnostic.IS_VALID;
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		// the source are supposed to be valid
		if (imageDomains.size()==1) {
			IDomain d1 = imageDomains.get(0);
			//
			if (d1.isInstanceOf(WindowingDomain.DOMAIN)) {
				WindowingDomain w1 = (WindowingDomain)d1;
				return WindowingDomainImp.createRowsDomain(w1);
			}
			//
		} else if (imageDomains.size()==2) {
			IDomain d1 = imageDomains.get(0);
			IDomain d2 = imageDomains.get(1);
			//
			if (d1.isInstanceOf(WindowingDomain.DOMAIN)
				&& d2.isInstanceOf(WindowingDomain.DOMAIN)) {
				WindowingDomain w1 = (WindowingDomain)d1;
				WindowingDomain w2 = (WindowingDomain)d2;
				return WindowingDomainImp.createRowsDomain(w1,w2);
			}
			//
		}
		// else
		return IDomain.UNKNOWN;
	}

}
