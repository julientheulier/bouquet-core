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
import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd 
 * Power function definition
 */
public class PowerOperatorDefintion extends OperatorDefinition {

	public static final String POWER = MathsOperatorRegistry.MATHS_BASE
			+ "POWER";

	public PowerOperatorDefintion(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
		this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}
	
	public PowerOperatorDefintion(String name, String ID, IDomain domain) {
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
		hint.add("Function that take two arguments: a number and an exponent");
		return hint;
	}

	
	@Override
	public List getSimplifiedParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		IDomain number = new DomainNumeric();
		IDomain exponent = new DomainNumeric();
		exponent.setContentAssistLabel("exponent");

		type.add(number);
		type.add(exponent);

		poly.add(type);
		
		return poly;
	}
	
	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		IDomain number = new DomainNumeric();
		IDomain exponent = new DomainNumeric();
		exponent.setContentAssistLabel("exponent");
		IDomain cst1 = new DomainNumericConstant(0.0);
		IDomain cst2 = new DomainNumericConstant(0.0);
		cst2.setContentAssistLabel("exponent");

		type.add(number);
		type.add(exponent);

		poly.add(type);
		type = new ArrayList<IDomain>();

		type.add(cst1);
		type.add(exponent);

		poly.add(type);
		type = new ArrayList<IDomain>();


		type.add(number);
		type.add(cst2);

		poly.add(type);
		type = new ArrayList<IDomain>();

		type.add(cst1);
		type.add(cst2);

		poly.add(type);


		return poly;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		return ExtendedType.FLOAT;
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argument0 = imageDomains.get(0);
		boolean is_aggregate = argument0.isInstanceOf(AggregateDomain.DOMAIN);
		IDomain domain = IDomain.CONTINUOUS;
        if (is_aggregate) {
        	// compose with Aggregate
        	domain = AggregateDomain.MANAGER.createMetaDomain(domain);
        }
        //
        return domain;
	}

}
