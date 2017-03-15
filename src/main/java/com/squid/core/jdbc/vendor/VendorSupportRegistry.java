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
package com.squid.core.jdbc.vendor;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.database.model.DatabaseProduct;

public class VendorSupportRegistry {

	public final static VendorSupportRegistry INSTANCE = new VendorSupportRegistry();

	static final Logger logger = LoggerFactory.getLogger(VendorSupportRegistry.class);

	private IVendorSupport defaultVendorSupport = new DefaultVendorSupport();
	private ArrayList<IVendorSupport> vendors = new ArrayList<IVendorSupport>();

	protected VendorSupportRegistry() {
		// register();
		// PluginSupportRegistry plugin = new PluginSupportRegistry();
	}

	/**
	 * return the given VendorSupport
	 * 
	 * @param vendorId
	 * @return
	 */
	public IVendorSupport getVendorSupportByID(String vendorId) {
		for (IVendorSupport vendor : vendors) {
			if (vendor.getVendorId().equalsIgnoreCase(vendorId)) {
				return vendor;
			}
		}
		// else if not found
		return null;
	}

	/**
	 * return the specific vendor support library, or default support if don't
	 * know better
	 * 
	 * @param product
	 * @return
	 */
	public IVendorSupport getVendorSupport(DatabaseProduct product) {
		if (product != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(("product is " + product.getProductName()));
			}
		}
		if (product == null) {
			return defaultVendorSupport;
		}

		for (IVendorSupport vendor : vendors) {
			if (vendor.isSupported(product)) {
				if (product != null) {
					if (logger.isDebugEnabled()) {
						logger.debug(("Chosen vendor is " + vendor.getVendorId() + ", version: "
								+ vendor.getVendorVersion()));
					}
				}
				return vendor;
			}
		}
		// else
		if (logger.isDebugEnabled()) {
			logger.debug(("Chosen vendor is " + defaultVendorSupport.getVendorId()));
		}
		return defaultVendorSupport;
	}

	public void register(URLClassLoader cl) {
		ServiceLoader<IVendorSupport> loader = ServiceLoader.load(IVendorSupport.class, cl);
		Iterator<IVendorSupport> vendorSupports = loader.iterator();
		// LoggerFactory.getLogger(this.getClass()).debug("List of vendorSupport
		// Providers");
		while (vendorSupports.hasNext()) {
			IVendorSupport vendorSupport = vendorSupports.next();
			LoggerFactory.getLogger(this.getClass()).debug("vendorSupport available " + vendorSupport.getClass());
			register(vendorSupport);
		}
		LoggerFactory.getLogger(this.getClass()).debug("End of vendorSupport Providers");

	}

	// Use for plugin versions.
	public ArrayList<IVendorSupport> listVendors() {
		return this.vendors;
	}

	private void register(IVendorSupport vendor) {
		vendors.add(vendor);
	}

}
