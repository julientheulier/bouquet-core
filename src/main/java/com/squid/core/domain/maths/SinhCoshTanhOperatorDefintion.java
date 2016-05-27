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
 * Sinh, Cosh, Tanh functions definition
 * http://docs.oracle.com/cd/B19306_01/server.102/b14200/functions147.htm
 */
public class SinhCoshTanhOperatorDefintion extends OperatorDefinition {

	public static final String SINH = MathsOperatorRegistry.MATHS_BASE + "SINH";
	public static final String COSH = MathsOperatorRegistry.MATHS_BASE + "COSH";
	public static final String TANH = MathsOperatorRegistry.MATHS_BASE + "TANH";

	public SinhCoshTanhOperatorDefintion(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
	}

	public SinhCoshTanhOperatorDefintion(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
	}

	public SinhCoshTanhOperatorDefintion(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.CONTINUOUS, categoryType);
	}


	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public ListContentAssistEntry getListContentAssistEntry(){
		if(super.getListContentAssistEntry()==null){

			List <String> descriptions = new ArrayList<String>();
			descriptions.add("This function returns the hyperbolic sine, cosine or tangent of n");

			ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
			setListContentAssistEntry(entry);

		}
		return super.getListContentAssistEntry();
	}

	@Override
	public List getParametersTypes() {
		List type = new ArrayList<IDomain>();
		IDomain number = new DomainNumeric();
		number.setContentAssistLabel("Numeric n");
		number.setContentAssistProposal("${1:n}");
		type.add(number);

		List poly = new ArrayList<List>();
		poly.add(type);
		return poly;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
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
