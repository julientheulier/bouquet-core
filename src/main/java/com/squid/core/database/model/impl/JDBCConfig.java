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
package com.squid.core.database.model.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCConfig {

	static final Logger logger = LoggerFactory
			.getLogger(JDBCConfig.class);

    private String jdbcUrl;
    private String username;
    private String password;
    
	public JDBCConfig(String jdbcUrl, String username, String password) throws IllegalArgumentException {
		super();
		if (jdbcUrl == null || jdbcUrl == ""
				|| !jdbcUrl.toLowerCase().startsWith("jdbc:")) {
			throw new IllegalArgumentException("invalid jdbc url");
		}
		if (jdbcUrl.toLowerCase().equals("jdbc:test:")) {
			throw new IllegalArgumentException("jdbc test url");
		}
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		if (password == null || password == "") {
			logger.warn("connection " + jdbcUrl + " has no password set");
			password = "";// jdbc driver doesn't like null values
		}
		this.password = password;
	}
    
    public String getJdbcUrl() {
		return jdbcUrl;
	}
    
    public String getUsername() {
		return username;
	}
    
    public String getPassword() {
		return password;
	}
}
