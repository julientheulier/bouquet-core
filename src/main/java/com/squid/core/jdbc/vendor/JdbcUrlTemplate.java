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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sergefantino
 *
 */
public class JdbcUrlTemplate {
	
	private String name = "";
	
	private String template = "";
	
	private List<JdbcUrlParameter> parameters = new ArrayList<>();
	
	private Map<String, JdbcUrlParameter> lookup = new HashMap<>();

	/**
	 * 
	 */
	public JdbcUrlTemplate(String template) {
		this.template = template;
	}
	
	/**
	 * 
	 */
	public JdbcUrlTemplate(String name, String template) {
		this.name = name;
		this.template = template;
	}
	
	public JdbcUrlTemplate add(JdbcUrlParameter parameter) {
		parameters.add(parameter);
		lookup.put(parameter.getName(), parameter);
		return this;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @return the parameters
	 */
	public List<JdbcUrlParameter> getParameters() {
		return parameters;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"name\":\""+name+"\",");
		json.append("\"template\":\""+template+"\",");
		json.append("\"parameters\":[");
		boolean first = true;
		for (JdbcUrlParameter parameter : parameters) {
			if (!first) json.append(",");
			json.append(parameter);
			first = false;
		}
		json.append("]}");
		//
		return json.toString();
	}
	
}
