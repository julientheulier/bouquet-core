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

import com.squid.core.domain.DomainNumeric;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd 
 * Sign function definition
 * http://docs.oracle.com/cd/B19306_01/server.102/b14200/functions145.htm
 */
public class SignOperatorDefinition extends OperatorDefinition {

	public static final String SIGN = MathsOperatorRegistry.MATHS_BASE + "SIGN";

	public SignOperatorDefinition(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
        this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}

	public SignOperatorDefinition(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
        this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}
		
	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("SIGN returns the sign of n (-1 for negative numbers, 0 for 0 and 1 for strictly positive numbers)");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List type = new ArrayList<IDomain>();
		IDomain number = new DomainNumeric();
		type.add(number);

		List poly = new ArrayList<List>();
		poly.add(type);
		return poly;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		return ExtendedType.INTEGER;
	}

}
