package com.squid.core.database.plugins;

import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;

public class BaseBouquetPlugin implements IBouquetPlugin {

	protected ArrayList<Driver> drivers;

	protected URLClassLoader driverCL;

	@Override
	public void loadDriver() {
		// TO OVERRIDE
	}

	@Override
	public ArrayList<Driver> getDrivers() {
		return drivers;
	}

}
