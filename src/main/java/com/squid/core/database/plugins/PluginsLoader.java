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

package com.squid.core.database.plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.database.impl.DriverShim;
import com.squid.core.jdbc.vendor.VendorSupportRegistry;
import com.squid.core.sql.db.templates.SkinRegistry;

public class PluginsLoader {
	private static final Logger logger = LoggerFactory.getLogger(PluginsLoader.class);

	public static PluginsLoader INSTANCE = new PluginsLoader();

	private static final String patternPlugin = ".jar";
	public ArrayList<IBouquetPlugin> plugins;

	public PluginsLoader() {
		this.plugins = new ArrayList<IBouquetPlugin>();
	}

	public void loadPlugins() {
		String path = new String(System.getProperty("kraken.plugin.dir", ""));
		URL[] pluginsList = findPlugins(path);
		ClassLoader rollback = Thread.currentThread().getContextClassLoader();

		URLClassLoader cl = new URLClassLoader(pluginsList, rollback);

		ServiceLoader<com.squid.core.database.plugins.IBouquetPlugin> loader = ServiceLoader
				.load(com.squid.core.database.plugins.IBouquetPlugin.class, cl);
		Iterator<IBouquetPlugin> pluginsIt = loader.iterator();

		while (pluginsIt.hasNext()) {
			IBouquetPlugin plugin = pluginsIt.next();
			plugin.loadDriver();
			if( plugin.getClass().toString().contains("redshift")){
				this.plugins.add(0,plugin);			
			}else{
				this.plugins.add(plugin);
			}
		}
	
			
		
		
		for(IBouquetPlugin plugin : this.plugins){
			for (DriverShim d : plugin.getDrivers()) {

				Enumeration<Driver> availableDrivers = DriverManager.getDrivers();
				Boolean duplicate = false;
				while (availableDrivers.hasMoreElements()) {
					Driver already = availableDrivers.nextElement();
					if (already instanceof DriverShim) {
						if (((DriverShim) already).getName().equals(d.getName())) {
							duplicate = true;
						}
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("driver available " + d.getClass());
				}
				;
				if (!duplicate) {
					try {
						DriverManager.registerDriver(d);
						logger.info("Registering driver " + d.getName() + " for plug in "
								+ plugin.getClass().toString());
					} catch (SQLException e) {
						logger.info("Failed to register driver " + d.getClass().toString() + " for plug in "
								+ plugin.getClass().toString());
						e.printStackTrace();
					}
				} else {
					logger.info("Duplicate driver " + d.getClass().toString() + " for plug in "
							+ plugin.getClass().toString());
				}
			}
		}

		SkinRegistry.INSTANCE.loadRegistry(cl);
		VendorSupportRegistry.INSTANCE.register(cl);
	}

	public static URL[] findPlugins(String path) {
		logger.info("Looking for plugins in : " + path);
		if (path == null || path.equals("")) {
			throw new IllegalArgumentException(
					"undefined Bouquet plugin directory: please set kraken.plugin.dir property");
		}
		URL[] result = null;
		File folder = new File(path);
		if (!folder.exists()) {
			throw new IllegalArgumentException(
					"JDBC Bouquet directory does not exist: " + path + " defined by kraken.plugin.dir property");
		}
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles == null || listOfFiles.length == 0) {
			throw new IllegalArgumentException("JDBC Bouquet directory does not exist or is empty: " + path
					+ " defined by kraken.plugin.dir property");
		}
		ArrayList<URL> listOfPlugins = new ArrayList<URL>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(patternPlugin)) {
				try {
					URL url = listOfFiles[i].toURI().toURL();
					listOfPlugins.add(url);
					if (logger.isDebugEnabled()) {
						logger.debug("Plugin found : " + url);
					}
				} catch (MalformedURLException e) {
					logger.warn(e.getLocalizedMessage());
				}
			}
		}
		result = listOfPlugins.toArray(new URL[listOfPlugins.size()]);
		return result;
	}

}
