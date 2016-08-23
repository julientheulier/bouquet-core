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

/**
 * @author sergefantino
 *
 */
public class JdbcUrlParameter {
	
	private String name;
	
	private boolean optional;
	
	private String defaultValue;
	
	/**
	 * 
	 */
	public JdbcUrlParameter(String name, boolean optional) {
		this.name = name;
		this.optional = optional;
	}
	
	/**
	 * 
	 */
	public JdbcUrlParameter(String name, boolean optional, String defaultValue) {
		this.name = name;
		this.optional = optional;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}
	
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder json = new StringBuilder();
		json.append("{\"name\":\""+name+"\",");
		json.append("\"optional\":\""+optional+"\"");
		if (defaultValue!=null) {
			json.append(","+"\"defaultValue\":\""+defaultValue+"\"");
		}
		json.append("}");
		return json.toString();
	}

}
