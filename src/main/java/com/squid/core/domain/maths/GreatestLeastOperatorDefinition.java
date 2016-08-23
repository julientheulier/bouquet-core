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
package com.squid.core.domain.maths;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.DomainConditional;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class GreatestLeastOperatorDefinition extends OperatorDefinition {

	public static final String GREATEST = MathsOperatorRegistry.MATHS_BASE+"GREATEST";
	public static final String LEAST = MathsOperatorRegistry.MATHS_BASE+"LEAST";


	public GreatestLeastOperatorDefinition(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
	}
	
	public GreatestLeastOperatorDefinition(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
	}
	
	public GreatestLeastOperatorDefinition(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.NUMERIC, categoryType);
	}
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain any = new DomainAny();
		any.setContentAssistLabel("any");
		type.add(any);
		type.add(any);
		poly.add(type);
		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {

		return OperatorDiagnostic.IS_VALID;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length>0) {
			//setDomain(types[0].getDomain());
			return types[0];
		} else {
			return ExtendedType.INTEGER;
		}
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()>0) {
			//setDomain(types[0].getDomain());
			return imageDomains.get(0);
		} else {
			return IDomain.NUMERIC;
		}
	}
	
}
