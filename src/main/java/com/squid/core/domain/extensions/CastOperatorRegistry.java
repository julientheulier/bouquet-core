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

import com.squid.core.domain.extensions.cast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorRegistry;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.OperatorScopeException;

public class CastOperatorRegistry implements OperatorRegistry {
	
	private final static Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);
	
	public CastOperatorRegistry(OperatorScope scope) {
		try {
			apply(scope);
			logger.info("init CastOperatorRegistry");
		} catch (OperatorScopeException e) {
			logger.error("unable to init the CastOperatorRegistry", e);
		}
	}

	@Override
	public void apply(OperatorScope scope) throws OperatorScopeException {
		// TODO Auto-generated method stub
		CastToCharOperatorDefinition to_char = new CastToCharOperatorDefinition("TO_CHAR",IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(to_char);
		CastToDateOperatorDefinition to_date = new CastToDateOperatorDefinition("TO_DATE",IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(to_date);
		CastToNumberOperatorDefinition to_number = new CastToNumberOperatorDefinition("TO_NUMBER",IDomain.NUMERIC, OperatorDefinition.MATHS_TYPE);
		scope.registerExtension(to_number);
		CastToIntegerOperatorDefinition to_integer = new CastToIntegerOperatorDefinition("TO_INTEGER",IDomain.NUMERIC, OperatorDefinition.MATHS_TYPE);
		scope.registerExtension(to_integer);
		CastToTimestampOperatorDefinition to_timestamp = new CastToTimestampOperatorDefinition("TO_TIMESTAMP",IDomain.TIMESTAMP, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(to_timestamp);

	}

}
