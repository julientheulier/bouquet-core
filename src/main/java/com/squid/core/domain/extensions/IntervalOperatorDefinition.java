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

import java.util.List;

import com.squid.core.domain.CustomTypes;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class IntervalOperatorDefinition extends OperatorDefinition {

	public static final String INTERVAL_BASE = "com.squid.domain.operator.interval.";
	public static final String INTERVAL_DAY = INTERVAL_BASE+"DAY";
	public static final String INTERVAL_MONTH = INTERVAL_BASE+"MONTH";
	public static final String INTERVAL_YEAR = INTERVAL_BASE+"YEAR";
	public static final String INTERVAL_HOUR = INTERVAL_BASE+"HOUR";
	public static final String INTERVAL_MINUTE = INTERVAL_BASE+"MINUTE";
	public static final String INTERVAL_SECOND = INTERVAL_BASE+"SECOND";

	public IntervalOperatorDefinition(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.INTERVAL,categoryType);
	}


	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()!=1) {
			return new OperatorDiagnostic("Invalid number of parameters",getName()+"(integer)");
		}
		if (!imageDomains.get(0).isInstanceOf(IDomain.NUMERIC)) {
			return new OperatorDiagnostic("Parameter #1 must be an integer but it is a "+imageDomains.get(0).getName(),1,getName()+"(integer)");
		}
		return OperatorDiagnostic.IS_VALID;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (this.getExtendedID().equals(INTERVAL_YEAR)) {
			return new ExtendedType(IDomain.INTERVAL,CustomTypes.INTERVAL,1,0);
		} else if (this.getExtendedID().equals(INTERVAL_MONTH)) {
			return new ExtendedType(IDomain.INTERVAL,CustomTypes.INTERVAL,2,0);
		} else if (this.getExtendedID().equals(INTERVAL_DAY)) {
			return new ExtendedType(IDomain.INTERVAL,CustomTypes.INTERVAL,3,0);
		} else if (this.getExtendedID().equals(INTERVAL_HOUR)) {
			return new ExtendedType(IDomain.INTERVAL,CustomTypes.INTERVAL,4,0);
		} else if (this.getExtendedID().equals(INTERVAL_MINUTE)) {
			return new ExtendedType(IDomain.INTERVAL,CustomTypes.INTERVAL,5,0);
		} else if (this.getExtendedID().equals(INTERVAL_SECOND)) {
			return new ExtendedType(IDomain.INTERVAL,CustomTypes.INTERVAL,6,0);
		}
		return ExtendedType.INTERVAL;
	}

}
