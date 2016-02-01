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
package com.squid.core.sql.db.templates;

import com.squid.core.database.model.DatabaseProduct;
import com.squid.core.sql.render.ISkinFeatureSupport;
import com.squid.core.sql.render.ISkinPref;
import com.squid.core.sql.template.ISkinHandler;

public interface ISkinProvider
extends ISkinHandler
{
	
	// the provider is not applicable
	public static final int NOT_APPLICABLE = -1;
	// the lowest acceptable accuracy
	public static final int LOWEST_APPLICABLE = 0;
	// acceptable accuracy
	public static final int APPLICABLE = 1;
	// the perfect match
	public static final int PERFECT_MATCH = 2;
	
	/**
	 * compute the provider accuracy for the given handler
	 * @param handler
	 * @return
	 */
	public double computeAccuracy(DatabaseProduct product);
	
	public DelegateOperatorRendererRegistry getDelegateRendererRegistry();

	// experimental
	/**
	 * return information regarding the feature support; if the feature is unknown to the system, return null;
	 * The resulting object may be interpreted depending on the feature.
	 */
	public ISkinFeatureSupport getFeatureSupport(DefaultJDBCSkin skin, String featureID);
	
	public ISkinPref getPreferences(DefaultJDBCSkin skin, String featureID);

	
}
