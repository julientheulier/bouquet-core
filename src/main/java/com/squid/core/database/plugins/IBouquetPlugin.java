package com.squid.core.database.plugins;

import java.sql.Driver;
import java.util.ArrayList;

public interface IBouquetPlugin {
	
	
	public void loadDriver();

	public ArrayList<Driver> getDrivers();
}
