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
package com.squid.core.domain.extensions;

import java.sql.Types;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class ExtractOperatorDefinition extends OperatorDefinition {

	public static final String EXTRACT_BASE = "com.squid.domain.operator.extract.";
	public static final String EXTRACT_DAY = EXTRACT_BASE+"DAY";
	public static final String EXTRACT_MONTH = EXTRACT_BASE+"MONTH";
	public static final String EXTRACT_YEAR = EXTRACT_BASE+"YEAR";
	public static final String EXTRACT_HOUR = EXTRACT_BASE+"HOUR";
	public static final String EXTRACT_MINUTE = EXTRACT_BASE+"MINUTE";
	public static final String EXTRACT_SECOND = EXTRACT_BASE+"SECOND";
	public static final String EXTRACT_DAY_OF_WEEK = EXTRACT_BASE+"DAY_OF_WEEK";
	public static final String EXTRACT_DAY_OF_YEAR = EXTRACT_BASE+"DAY_OF_YEAR";

	public ExtractOperatorDefinition(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.NUMERIC,categoryType);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()!=1) {
			return new OperatorDiagnostic("Invalid number of parameters",getName()+"(temporal)");
		}
		if (getExtendedID()==EXTRACT_HOUR||getExtendedID()==EXTRACT_MINUTE||getExtendedID()==EXTRACT_SECOND) {
			if (!imageDomains.get(0).isInstanceOf(IDomain.TIME)
					&& !imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP)
			) {
				return new OperatorDiagnostic("Parameter #1 must be a time or timestamp but it is a "+imageDomains.get(0).getName(),1,getName()+"(time or timestamp)");
			}
		} else {
			if (!imageDomains.get(0).isInstanceOf(IDomain.DATE)) {
				return new OperatorDiagnostic("Parameter #1 must be a date but it is a "+imageDomains.get(0).getName(),1,getName()+"(date)");
			}
		}
		return OperatorDiagnostic.IS_VALID;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		int size=2;
		if (getExtendedID()==EXTRACT_YEAR) {
			size=4;
		} else if (getExtendedID()==EXTRACT_DAY_OF_YEAR) {
			size=3;
		} else if (getExtendedID()==EXTRACT_DAY_OF_WEEK) {
			size=1;
		}
		return new ExtendedType(IDomain.NUMERIC,Types.NUMERIC,0,size);
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

}
