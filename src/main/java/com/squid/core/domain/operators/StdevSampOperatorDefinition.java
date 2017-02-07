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
package com.squid.core.domain.operators;

/**
 * Static definition of intrinsic operators
 * @author xavier bebin
 */

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.IDomain;

public class StdevSampOperatorDefinition extends AggregateOperatorDefinition {

	/**
	 * @param name
	 * @param id
	 */
	public StdevSampOperatorDefinition(String name, String extendedId) {
		super(name, extendedId, PREFIX_POSITION, name, IDomain.NUMERIC);
		setDomain(IDomain.NUMERIC);
	}
	
	public StdevSampOperatorDefinition(String name, int id) {
		super(name, id);
		setDomain(IDomain.NUMERIC);
	}
	public StdevSampOperatorDefinition(String name, int id, int categoricalType) {
		super(name, id, categoricalType);
		setDomain(IDomain.NUMERIC);
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return ExtendedType.FLOAT;
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> sourceDomain) {
		return IDomain.NUMERIC;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		type.add(IDomain.NUMERIC);
		poly.add(type);
		return poly;
	}
	
	public String prettyPrint(String symbol, int position, String[] args, boolean showBrackets) {
		if (args.length == 1) {
			return "STDDEV_SAMP("+ args[0]+")";
		}
		return "UNDEFINED()";
		
	}
	
}
