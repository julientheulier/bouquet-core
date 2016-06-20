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
package com.squid.core.domain.sort;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.domain.sort.DomainSort.SortDirection;

public class SortOperatorDefinition extends OperatorDefinition {
	
	public static final String ASC_ID = "com.squid.domain.model.sort.ASC";
	public static final String DESC_ID = "com.squid.domain.model.sort.DESC";
	
	private SortDirection direction;
	
	public SortOperatorDefinition(String name, String id, SortDirection direction, int categoryType) {
		super(name,id,OperatorDefinition.PREFIX_POSITION,name,DomainSort.DOMAIN, categoryType);
		setParamCount(1);
		this.direction=direction;
	}


	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain any = new DomainAny();
		any.setContentAssistLabel("any");
		type.add(any);
		poly.add(type);
		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()!=1) {
			return new OperatorDiagnostic("invalid number of argument",getSymbol()+"(X)");
		} else
			return OperatorDiagnostic.IS_VALID;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length==1) {
			ExtendedType copy = new ExtendedType(types[0]);
			return fixExtendedTypeDomain(copy, types);
		} else {
			return ExtendedType.UNDEFINED;
		}
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()==1) {
			return DomainSort.DOMAIN.createMetaDomain(imageDomains.get(0), this.direction);
		} else {
			return IDomain.UNKNOWN;
		}
	}

}
