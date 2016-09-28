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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.database.impl.DriverLoader;
//import com.squid.core.database.impl.PluginSupportRegistry;
import com.squid.core.database.model.DatabaseProduct;
import com.squid.core.sql.render.SQLSkin;

/**
 * Register skins provided by plugins
 * @author serge fantino
 *
 */
public class SkinRegistry 
{
	
	public static SkinRegistry INSTANCE = new SkinRegistry();
	
	private ArrayList<ISkinProvider> registry = new ArrayList<ISkinProvider>();
	private DefaultSkinProvider defaultProvider = null;
	private SQLSkin defaultSkin = null;
	
    static final Logger logger = LoggerFactory.getLogger(SkinRegistry.class);

	protected SkinRegistry() {
		loadRegistry();
		defaultProvider = new DefaultSkinProvider();
		defaultSkin = defaultProvider.createSkin(null);
	}

	public ISkinProvider findSkinProvider(Class<? extends ISkinProvider> type) {
		//update list of registry first

		for (Iterator<ISkinProvider> iter = registry.iterator();iter.hasNext();) {
			ISkinProvider provider = iter.next();
			// Compare only the class name without the package //T436 about moving packages.
			if (provider.getClass().getSimpleName().equals(type.getSimpleName())) return provider;
		}
		// else
		return defaultProvider;
	}
	
	public ISkinProvider findSkinProvider(DatabaseProduct product) {
		return findSkinProvider(product,true);
	}
	
	/**
	 * return the provider associated to the product. Return the default provider if useDefault is true.
	 * @param product
	 * @param useDefault
	 * @return
	 */
	public ISkinProvider findSkinProvider(DatabaseProduct product, boolean useDefault) {
		if (product==null) return defaultProvider;
		//

		double score = ISkinProvider.NOT_APPLICABLE;
		ISkinProvider winner = null;
		for (Iterator<ISkinProvider> iter = registry.iterator();iter.hasNext();) {
			ISkinProvider provider = iter.next();
			double compute = provider.computeAccuracy(product);
			if (compute>score) {
				score = compute;
				winner = provider;
			}
		}
		if (winner!=null) {
			if(logger.isDebugEnabled()){logger.debug(("Best provider is "+winner));}
			return winner;
		} else {
			if(logger.isDebugEnabled()){logger.debug(("Best provider is "+defaultProvider));}
			return useDefault?defaultProvider:null;
		}
	}
	
	public ISkinProvider getDefaultSkinProvider() {
		return defaultProvider;
	}
	
	public SQLSkin getDefaultSkin() {
		return defaultSkin;
	}

	private void loadRegistry() {
		ServiceLoader<ISkinProvider> loader = ServiceLoader.load(ISkinProvider.class, DriverLoader.DRIVER_LOADER);
	    Iterator<ISkinProvider> skinProviders = loader.iterator();
	    while(skinProviders.hasNext()){
	    	ISkinProvider skinProvider = skinProviders.next();
	    	LoggerFactory.getLogger(this.getClass()).debug("SkinProviderAvailable "+skinProvider.getClass());
	    	register(skinProvider);
	    }
	}

	private void register(ISkinProvider provider) {
		registry.add(provider);
	}

}
