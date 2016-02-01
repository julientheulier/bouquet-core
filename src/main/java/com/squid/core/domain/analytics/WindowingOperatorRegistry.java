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
package com.squid.core.domain.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.domain.operators.OperatorRegistry;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.OperatorScopeException;

public class WindowingOperatorRegistry
implements OperatorRegistry {
	
	private final static Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);
	
	public WindowingOperatorRegistry(OperatorScope scope) {
		try {
			apply(scope);
			logger.info("init WindowingOperatorRegistry");
		} catch (OperatorScopeException e) {
			logger.error("unable to init the WindowingOperatorRegistry", e);
		}
	}
	
	public static final String WINDOWING_BASE = "com.squid.domain.windowing.";
	public static final String WINDOWING_ROWS_ID = WINDOWING_BASE+"rows";
	public static final String WINDOWING_UNBOUNDED_ID = WINDOWING_BASE+"unbounded";
	public static final String WINDOWING_PRECEDING_ID = WINDOWING_BASE+"preceding";
	public static final String WINDOWING_CURRENT_ID = WINDOWING_BASE+"current";
	public static final String WINDOWING_FOLLOWING_ID = WINDOWING_BASE+"following";

	@Override
	public void apply(OperatorScope scope) throws OperatorScopeException {
		scope.registerExtension(new RowsOperatorDefinition("ROWS",WINDOWING_ROWS_ID));
		scope.registerExtension(new UnboudedOperatorDefinition("UNBOUNDED",WINDOWING_UNBOUNDED_ID));
		scope.registerExtension(new CurrentOperatorDefinition("CURRENT",WINDOWING_CURRENT_ID));
		scope.registerExtension(new PrecedingOperatorDefinition("PRECEDING",WINDOWING_PRECEDING_ID));
		scope.registerExtension(new FollowingOperatorDefinition("FOLLOWING",WINDOWING_FOLLOWING_ID));
	}

}
