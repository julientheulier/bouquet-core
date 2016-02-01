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
package com.squid.core.velocity;

import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocityTemplateManager {

	private static final Logger logger = LoggerFactory
			.getLogger(VelocityTemplateManager.class);

	public VelocityTemplateManager() {
	}

	public boolean templateExists(String templateID) {
		return getEngine().resourceExists(templateID);
	}

	public Template getVelocityTemplate(String templateID) throws Exception {
		return getEngine().getTemplate(templateID);
	}

	private static VelocityEngine INSTANCE;

	public static void initEngine() {
		INSTANCE = createEngine();
	}
	
	public static void initEngine(String path) {
		INSTANCE = createEngine(path);
	}

	private static VelocityEngine getEngine() {
		if (INSTANCE == null) {
			INSTANCE = createEngine();
		}
		return INSTANCE;
	}

	private static VelocityEngine createEngine() {
		//
		Properties properties = new Properties();
		properties.put("resource.loader", "class");
		properties
				.put("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		properties.put(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.Log4JLogChute");
		properties.put("runtime.log.logsystem.log4j.logger", "root");
		VelocityEngine engine = new VelocityEngine(properties);
		//
		return engine;
	}

	private static VelocityEngine createEngine(String path) {
		logger.info("file.resource.loader.path : " + path);
		//
		Properties properties = new Properties();
		properties.put("resource.loader", "file");
		properties
				.put("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		properties.put("file.resource.loader.path", path);
		properties.put("file.resource.loader.cache", true);
		properties.put(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.Log4JLogChute");
		properties.put("runtime.log.logsystem.log4j.logger", "root");
		VelocityEngine engine = new VelocityEngine(properties);
		//
		return engine;
	}

}
