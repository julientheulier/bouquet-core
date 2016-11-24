package com.squid.core.database.plugins;

import java.sql.Driver;
import java.util.ArrayList;

public class BaseBouquetPlugin implements IBouquetPlugin {
	
	protected ArrayList<Driver> drivers; 
	
	
	@Override
	public void loadDriver() {
		// TO OVERRIDE
	}
	
	
	@Override
	public ArrayList<Driver> getDrivers() {
		return drivers;
	}

}
