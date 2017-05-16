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

import java.util.Collections;
import java.util.List;

import com.squid.core.domain.IDomain;

/**
 * Support for compareTo() and growth() pseudo operators.
 * These operators are not supported by the SQL database; they are directly handled by the analytics API.
 * In order to be able to easily reference the resulting columns, we introduced these operators so you can write a valid expression that reference the comparison column.
 * T2511
 * @author sergefantino
 *
 */
public class CompareToOperatorDefinition extends OperatorDefinition {
	
	private List parameters = null;
	
	public static final String GROWTH= "COMPARETO_GROWTH";
	public static final String COMPARE_TO = "COMPARETO_COMPARETO";

	
	public CompareToOperatorDefinition(String name) {
		super(name, "compareTo"+name, OperatorDefinition.PREFIX_POSITION, name, IDomain.AGGREGATE);
		// init parameters
		parameters = Collections.singletonList(Collections.singletonList(IDomain.AGGREGATE));
	}
	public CompareToOperatorDefinition(String name, String id) {
		super(name, id, OperatorDefinition.PREFIX_POSITION, name, IDomain.AGGREGATE);
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.domain.operators.OperatorDefinition#getParametersTypes()
	 */
	@Override
	public List getParametersTypes() {
		return parameters;
	}

	/* (non-Javadoc)
	 * @see com.squid.core.domain.operators.OperatorDefinition#getType()
	 */
	@Override
	public int getType() {
		return AGGR_TYPE;
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.domain.operators.OperatorDefinition#computeExtendedType(com.squid.core.domain.operators.ExtendedType[])
	 */
	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		if (types.length==1) return types[0];
		// else
		return ExtendedType.UNDEFINED;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}


}
