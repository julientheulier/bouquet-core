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
package com.squid.core.sql.db.features;

import com.squid.core.sql.render.ISkinFeatureSupport;

/**
 * This feature defines the rollup strategy to use for a given Skin
 * @author sergefantino
 *
 */
public interface IRollupStrategySupport
extends ISkinFeatureSupport
{

	public static final String ID = "IRollupStrategySupport";
	
	public static final IRollupStrategySupport DO_NOT_OPTIMIZE_STRATEGY = new IRollupStrategySupport() {
		@Override
		public Strategy getStrategy() {
			return Strategy.DO_NOT_OPTIMIZE;
		}
	};
	
	public static final IRollupStrategySupport OPTIMIZE_USING_TEMPORARY_STRATEGY = new IRollupStrategySupport() {
		@Override
		public Strategy getStrategy() {
			return Strategy.OPTIMIZE_USING_TEMPORARY;
		}
	};
	
	public static final IRollupStrategySupport OPTIMIZE_USING_WITH_STRATEGY = new IRollupStrategySupport() {
		@Override
		public Strategy getStrategy() {
			return Strategy.OPTIMIZE_USING_WITH;
		}
	};
	
	/**
	 * return the strategy to use
	 * @return
	 */
	public Strategy getStrategy();
	
	enum Strategy {
		USE_BUILTIN_SUPPORT, // use the native ROLLUP function
		DO_NOT_OPTIMIZE, // compute each rollup level, no optimization - this is going to be the default
		OPTIMIZE_USING_TEMPORARY, // optimize if possible, using temporary table
		OPTIMIZE_USING_WITH // optimize if possible using WITH statement
	}

}
