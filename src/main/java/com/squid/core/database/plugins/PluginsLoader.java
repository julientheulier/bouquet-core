package com.squid.core.database.plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.database.impl.DriverShim;
import com.squid.core.jdbc.vendor.VendorSupportRegistry;
import com.squid.core.sql.db.templates.SkinRegistry;


public class PluginsLoader {
	private static final Logger logger = LoggerFactory.getLogger(PluginsLoader.class);

	
	public static  PluginsLoader INSTANCE = new PluginsLoader();
	
	private static final String patternPlugin = ".jar";

	public  void loadPlugins() {
		String path = new String(System.getProperty("kraken.plugin.dir", ""));
		URL[] pluginsList = findPlugins(path);
		ClassLoader rollback = Thread.currentThread().getContextClassLoader();

		URLClassLoader cl= new URLClassLoader(pluginsList, rollback);
		
		ServiceLoader<com.squid.core.database.plugins.IBouquetPlugin> loader = ServiceLoader.load(com.squid.core.database.plugins.IBouquetPlugin.class, cl);
		Iterator<IBouquetPlugin> pluginsIt = loader.iterator();
		
		while (pluginsIt.hasNext()) {
			IBouquetPlugin  plugin  = pluginsIt.next();
			plugin.loadDriver();
			for (Driver d : plugin.getDrivers()){
				DriverShim shim = new DriverShim(d);
				try {
					DriverManager.registerDriver(shim);
					logger.info("Registering driver " +d.getClass().toString() + " for plug in "+plugin.getClass().toString());
				} catch (SQLException e) {
					logger.info("Failed to register driver " +d.getClass().toString() + " for plug in "+plugin.getClass().toString());
					e.printStackTrace();
				}
			}
		}		
		
		SkinRegistry.INSTANCE.loadRegistry(cl);
		VendorSupportRegistry.INSTANCE.register(cl);
	}
	
	

	public static URL[] findPlugins(String path) {
		logger.info("Looking for plugins in : "+path);
		if (path==null || path.equals("")) {
			throw new IllegalArgumentException("undefined Bouquet plugin directory: please set kraken.plugin.dir property");
		}
		URL[] result = null;
		File folder = new File(path);
		if (!folder.exists()) {
			throw new IllegalArgumentException("JDBC Bouquet directory does not exist: "+path+" defined by kraken.plugin.dir property");
		}
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles==null || listOfFiles.length==0) {
			throw new IllegalArgumentException("JDBC Bouquet directory does not exist or is empty: "+path+" defined by kraken.plugin.dir property");
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


}
