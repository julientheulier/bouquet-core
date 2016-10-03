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
package com.squid.core.database.impl;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DriverLoader class is responsible for loading driver plugins from the plugin directory.
 * The plugin directory is defined by the system property kraken.plugin.dir
 * @author sergefantino
 *
 */
public class DriverLoader extends URLClassLoader {

	static final Logger logger = LoggerFactory.getLogger(DriverLoader.class);

	private static final String patternPlugin = ".jar";

	private Map<String, Class<?>> _classes = new HashMap<String, Class<?>>();

	private static DriverLoader DRIVER_LOADER;//new DriverLoader(PLUGIN_DIR);
	
	public static final DriverLoader getDriverLoader() {
		if (DRIVER_LOADER!=null) {
			return DRIVER_LOADER;
		} else {
			synchronized (DriverLoader.class) {
				if (DRIVER_LOADER==null) {
					String plugin = new String(System.getProperty("kraken.plugin.dir", ""));
					DRIVER_LOADER = new DriverLoader(plugin);
				}
			}
			return DRIVER_LOADER;
		}
	}

	public static URL[] findPlugins(String path) {
		logger.info("Looking for plugins in : "+path);
		if (path==null || path.equals("")) {
			throw new IllegalArgumentException("undefined JDBC plugin directory: please set kraken.plugin.dir property");
		}
		URL[] result = null;
		File folder = new File(path);
		if (!folder.exists()) {
			throw new IllegalArgumentException("JDBC plugin directory does not exist: "+path+" defined by kraken.plugin.dir property");
		}
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles==null || listOfFiles.length==0) {
			throw new IllegalArgumentException("JDBC plugin directory does not exist or is empty: "+path+" defined by kraken.plugin.dir property");
		}
		ArrayList<URL> listOfPlugins = new ArrayList<URL>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(patternPlugin)) {
				try {
					URL url = listOfFiles[i].toURI().toURL();
					listOfPlugins.add(url);
					if(logger.isDebugEnabled()){logger.debug("Plugin found : "+url);}
				} catch (MalformedURLException e) {
					logger.warn(e.getLocalizedMessage());
				}
			}
		}
		result = listOfPlugins.toArray(new URL[listOfPlugins.size()]);
		return result;
	}

	public DriverLoader(String path) {
		this(findPlugins(path));
	}

	public DriverLoader(URL url) {
		this(new URL[] { url });
	}

	public DriverLoader(URL[] urls) {
		super(urls, Thread.currentThread().getContextClassLoader());
	}

	@Override
	protected synchronized Class<?> findClass(String className) throws ClassNotFoundException {
		Class<?> cls = _classes.get(className);
		if (cls == null) {
			cls = super.findClass(className);
			_classes.put(className, cls);
		}
		return cls;
	}
	
	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#findResource(java.lang.String)
	 */
	@Override
	public URL findResource(String name) {
		// TODO Auto-generated method stub
		URL debug = super.findResource(name);
		if (debug!=null) {
			logger.info("found resource '"+name+"'="+debug.toString());
		} else {
			logger.error("cannot found resource '"+name+"'");
		}
		return debug;
	}
	
	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#getResourceAsStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceAsStream(String name) {
		// TODO Auto-generated method stub
		return super.getResourceAsStream(name);
	}

}
