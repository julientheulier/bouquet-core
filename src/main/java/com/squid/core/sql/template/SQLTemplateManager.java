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
package com.squid.core.sql.template;

import org.apache.velocity.Template;

import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.velocity.VelocityTemplateManager;

/**
 * Manage loading velocity templates
 * @author serge fantino
 *
 */
public class SQLTemplateManager 
extends VelocityTemplateManager 
implements ITemplateManager
{
	
	public SQLTemplateManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public ITemplate getTemplate(String templateID, SQLSkin skin) throws RenderingException {
		return getTemplate(templateID,skin,skin.getSkinHandler());
	}

	/**
	 * 
	 */
	protected ITemplate getTemplate(String templateID, SQLSkin skin, ISkinHandler handler) throws RenderingException {
		try {
			String QName = "sql/"+handler.getSkinPrefix(skin.getProduct())+"/"+templateID+".vm";
			if (templateExists(QName)) {
				Template template = getVelocityTemplate(QName);
				return new VelocityTemplate(template);
			} else {
				ISkinHandler parent = handler.getParentSkinProvider();
				if (parent!=null) {
					return getTemplate(templateID,skin,parent);
				} else {
					throw new RenderingException("cannot locate template "+QName);
				}
			}
		} catch (Exception e) {
			throw new RenderingException(e);
		}
	}
	
}
