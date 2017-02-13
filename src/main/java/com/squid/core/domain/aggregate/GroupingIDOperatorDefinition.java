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
package com.squid.core.domain.aggregate;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.DomainString;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorDefinition;

/**
 * This operator is equivalent to GROUPING_ID() function available on some databases
 * Oracle reference for instance: http://docs.oracle.com/cd/B19306_01/server.102/b14200/functions063.htm
 *  
 * @author sfantino
 *
 */
public class GroupingIDOperatorDefinition extends OperatorDefinition {
	
	public static final int ID = IntrinsicOperators.GROUPING_ID;

	public GroupingIDOperatorDefinition() {
		super("GROUPING_ID",ID,PREFIX_POSITION,"GROUPING_ID",IDomain.NUMERIC);
		this.setCategoryType(OperatorDefinition.AGGR_TYPE);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("takes a list of columns as input");
		return hint;
	}

	@Override
	public List getParametersTypes() { //Up to five level of path_elements.
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		poly.add(type);
		type = new ArrayList<IDomain>();
		IDomain any = new DomainAny();
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		poly.add(type);
		
		return poly;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length==0) {
			return ExtendedType.INTEGER;
		} else {
			return ExtendedType.UNDEFINED;
		}
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()==0) {
			return IDomain.NUMERIC;
		} else {
			return IDomain.UNKNOWN;
		}
	}
	
}
