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
package com.squid.core.sql;

import java.io.IOException;

import com.squid.core.database.model.DatabaseProduct;
import com.squid.core.database.model.Table;
import com.squid.core.sql.db.templates.SkinFactory;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.statements.DDLAddPrimaryKey;
import com.squid.core.sql.statements.DDLCreateTableStatement;
import com.squid.core.sql.statements.DDLDropTableStatement;
import com.squid.core.sql.statements.DDLTruncateTableStatement;

public class DDLStatementUtils {
	
	public String genCode(DatabaseProduct product, Table table) throws RenderingException, IOException {
		SQLSkin skin = SkinFactory.INSTANCE.createSkin(product);
		return genCode(skin, table);
	}
	
	public String genCode(SQLSkin skin, Table table) throws RenderingException, IOException {
		return genCode(skin, table, false);
	}
	
	public String genCode(SQLSkin skin, Table table, boolean checkShareNothing) throws RenderingException, IOException {
		DDLCreateTableStatement stat = new DDLCreateTableStatement(table, skin.isShareNothing());
		return stat.render(skin);
	}
	
	public String genCode(Table table) throws RenderingException, IOException {
		DatabaseProduct product = table.getSchema().getDatabase();
		SQLSkin skin = SkinFactory.INSTANCE.createSkin(product);
		return genCode(skin, table);
	}
	
	public String genCode(Table table, boolean checkShareNothing) throws RenderingException, IOException {
		DatabaseProduct product = table.getSchema().getDatabase();
		SQLSkin skin = SkinFactory.INSTANCE.createSkin(product);
		return genCode(skin, table, checkShareNothing);
	}
	
	public String genAddPrimaryKey(Table table) throws RenderingException, IOException {
		DatabaseProduct product = table.getSchema().getDatabase();
		SQLSkin skin = SkinFactory.INSTANCE.createSkin(product);
		DDLAddPrimaryKey stat = new DDLAddPrimaryKey(table);
		if (skin.isShareNothing()) {
			return null;
		} else {
			return stat.render(skin);
		}
	}
	
	public String genTruncateTable(Table table) throws RenderingException, IOException {
		DatabaseProduct product = table.getSchema().getDatabase();
		SQLSkin skin = SkinFactory.INSTANCE.createSkin(product);
		DDLTruncateTableStatement stat = new DDLTruncateTableStatement(table);
		return stat.render(skin);
	}
	
	public String genDropTable(Table table) throws RenderingException, IOException {
		DatabaseProduct product = table.getSchema().getDatabase();
		SQLSkin skin = SkinFactory.INSTANCE.createSkin(product);
		DDLDropTableStatement stat = new DDLDropTableStatement(table);
		return stat.render(skin);
	}

}
