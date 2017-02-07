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
package com.squid.core.domain.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorRegistry;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.OperatorScopeException;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd 
 * Registry ANSI functions
 */

public class StatsOperatorRegistry 
implements OperatorRegistry
{
	public static final String STATS_BASE = "com.squid.domain.operator.stats.";
	
	private final static Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);
	
	public StatsOperatorRegistry(OperatorScope scope) {
		try {
			apply(scope);
			logger.info("init StatsOperatorRegistry");
		} catch (OperatorScopeException e) {
			logger.error("unable to init the StatsOperatorRegistry", e);
		}
	}
	
	@Override
	public void apply(OperatorScope scope) throws OperatorScopeException {
		scope.registerExtension(new PercentileOperatorDefintion("PERCENTILE",PercentileOperatorDefintion.PERCENTILE, OperatorDefinition.NUMERIC_TYPE));
	}

}
