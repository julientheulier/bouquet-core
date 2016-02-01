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
import com.squid.core.database.model.impl.DatabaseProductImpl;
import com.squid.core.sql.render.SQLSkin;

/**
 * A factory responsible for creating SQL skin
 * @author serge fantino
 *
 */
public class SkinFactory {
	
	public static SkinFactory INSTANCE = new SkinFactory();
	
	private SkinFactory() {
		// on purpose
	}
	
	private SQLSkin defaultSkin = null;
	
	/**
	 * for debug only
	 * @return
	 */
	public SQLSkin getDefaultSkin() {
		if (defaultSkin==null) {
			defaultSkin = SkinRegistry.INSTANCE.getDefaultSkinProvider().createSkin(new DatabaseProductImpl());
		}
		return defaultSkin;
	}
	
	/**
	 * Create a skin for the given DatabaseProduct; return null if there is no skin associated with the product
	 * @param handler
	 * @return
	 */
	public SQLSkin createSkin(DatabaseProduct product) {
		ISkinProvider provider = SkinRegistry.INSTANCE.findSkinProvider(product);
		if (provider==null) {
			return null;
		} else {
			SQLSkin skin = provider.createSkin(product);
			return skin;
		}
	}
	
}
