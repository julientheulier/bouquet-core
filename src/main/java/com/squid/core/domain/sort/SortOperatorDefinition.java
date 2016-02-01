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

import java.sql.Types;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class SortOperatorDefinition extends OperatorDefinition {
	
	public static final String ASC_ID = "com.squid.domain.model.sort.ASC";
	public static final String DESC_ID = "com.squid.domain.model.sort.DESC";
	
	public static final ExtendedType SORT_TYPE = new ExtendedType(DomainSort.SORT,Types.NULL,0,0);
	
	public SortOperatorDefinition(String name, String id, IDomain domain, int categoryType) {
		super(name,id,OperatorDefinition.PREFIX_POSITION,name,domain, categoryType);
		setParamCount(1);
	}
	
	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
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
			return SORT_TYPE;
		} else {
			return ExtendedType.UNDEFINED;
		}
	}

}
