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
package com.squid.core.sql.db.render;

import java.util.ArrayList;
import java.util.Collection;

import com.squid.core.database.model.Table;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.BoundPiece;
import com.squid.core.sql.render.IFromPiece;
import com.squid.core.sql.render.IJoinDecorator;
import com.squid.core.sql.render.ISamplingDecorator;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.statements.IStatement;

public class FromTablePiece 
extends BoundPiece
implements IFromPiece {

	private IStatement statement;
	private Table table;
	private String alias;
	
	private boolean dense = true;// the table A is dense if it exists a table B that joins to A using an INNER join
	
	private ISamplingDecorator sampling = null;
	private Collection<IJoinDecorator> joins = null;
	private IJoinDecorator join = null;
	
	public FromTablePiece(IStatement statement, Scope parent, Table table, String alias) {
		super(parent);
		this.statement = statement;
		this.table = table;
		this.alias = alias;
	}
	
	public IStatement getStatement() {
		return statement;
	}
	
	public String getAlias() {
		return alias;
	}

	public Table getTable() {
		return table;
	}

	public String render(SQLSkin skin) throws RenderingException {
		return skin.render(skin,this);
	}
	
	public String renderTableName(SQLSkin skin) {
		String render = "";
		if (table.getSchema()!=null&&!table.getSchema().isNullSchema()) {
            render += skin.quoteSchemaIdentifier(table.getSchema());
            render += ".";
        }
		render += skin.quoteTableIdentifier(table);
		return render;
	}

	public ISamplingDecorator getSamplingDecorator() {
		return sampling;
	}

	public void setSamplingDecorator(ISamplingDecorator sampling) {
		this.sampling = sampling;
	}

	public Collection<IJoinDecorator> getJoinDecorators() {
		return joins;
	}

	public void addJoinDecorator(IJoinDecorator join) {
		if (joins==null) joins = new ArrayList<IJoinDecorator>();
		this.joins.add(join);
	}

	public boolean isDense() {
		return dense;
	}

	public void setDense(boolean dense) {
		this.dense = dense;
	}
	
	@Override
	public void setDefiningJoinDecorator(IJoinDecorator joinDecorator) {
		this.join = joinDecorator;
	}
	
	@Override
	public IJoinDecorator getDefiningJoinDecorator() {
		return this.join;
	}

}
