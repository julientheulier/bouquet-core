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

import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd
 * Floor definition
 */
public class FloorOperatorDefintion extends OperatorDefinition {

	public static final String FLOOR = MathsOperatorRegistry.MATHS_BASE
			+ "FLOOR";

	public FloorOperatorDefintion(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
	}

	public FloorOperatorDefintion(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
	}

	public FloorOperatorDefintion(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.NUMERIC, categoryType);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size() != 1) {
			return new OperatorDiagnostic("Invalid number of parameters",
					getName());
		}
		// check if parameter is valid?
		if (!imageDomains.get(0).isInstanceOf(IDomain.NUMERIC)) {
			return new OperatorDiagnostic("Parameter must be a number",
					getName());
		}

		return OperatorDiagnostic.IS_VALID;
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argument0 = imageDomains.get(0);
		boolean is_aggregate = argument0.isInstanceOf(AggregateDomain.DOMAIN);
		IDomain domain = IDomain.NUMERIC;
        if (is_aggregate) {
        	// compose with Aggregate
        	domain = AggregateDomain.MANAGER.createMetaDomain(domain);
        }
        //
        return domain;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return ExtendedType.INTEGER;
	}

}
