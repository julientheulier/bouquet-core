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
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd 
 * Round function definition
 */
public class RoundOperatorDefintion extends OperatorDefinition {

	public static final String ROUND = MathsOperatorRegistry.MATHS_BASE
			+ "ROUND";
	private ExtendedType extendedType = ExtendedType.INTEGER;

	public RoundOperatorDefintion(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
	}
	
	public RoundOperatorDefintion(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
	}
	
	public RoundOperatorDefintion(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.NUMERIC, categoryType);
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("ROUND returns n rounded to 0 places to the right of the decimal point");
		hint.add("Takes two arguments to compute ROUND(column_name,decimals)");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		IDomain number = new DomainNumeric();
		type.add(number);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(number);
		IDomain upperbound = new DomainNumeric();
		upperbound.setContentAssistLabel("decimals");
		type.add(upperbound);
		poly.add(type);
		return poly;
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length==1 || types[0].isInteger()) {
			return ExtendedType.INTEGER;
		} else {
			return ExtendedType.FLOAT;
		}
	}
	
	public ExtendedType getExtendedType() {
		return extendedType;
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argument0 = imageDomains.get(0);
		boolean is_aggregate = argument0.isInstanceOf(AggregateDomain.DOMAIN);
		IDomain domain = IDomain.UNKNOWN;
		if (imageDomains.size()==1) {
			domain = IDomain.NUMERIC;
		} else {
			domain = IDomain.CONTINUOUS;
		}
        if (is_aggregate) {
        	// compose with Aggregate
        	domain = AggregateDomain.MANAGER.createMetaDomain(domain);
        }
        //
        return domain;
	}

}
