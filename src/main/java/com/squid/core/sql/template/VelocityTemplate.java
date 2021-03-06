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

import java.io.Writer;

import org.apache.velocity.VelocityContext;

import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.statements.Statement;

public class VelocityTemplate 
implements ITemplate
{
	
	private org.apache.velocity.Template template;
	
	public VelocityTemplate(org.apache.velocity.Template template) {
		super();
		this.template = template;
	}
	
	public void render(SQLSkin skin, Statement statement, Writer writer) throws RenderingException {
		//
        VelocityContext context = new VelocityContext();
        //
        context.put("stat",statement);
        context.put("skin",skin);
        context.put("writer", writer);
        //
        try {
			this.template.merge(context,writer);
		} catch (Exception e) {
			throw new RenderingException(e);
		}
	}

}
