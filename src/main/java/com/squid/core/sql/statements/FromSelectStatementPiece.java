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
package com.squid.core.sql.statements;

import java.util.ArrayList;
import java.util.Collection;

import com.squid.core.sql.model.Aliaser;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.BoundPiece;
import com.squid.core.sql.render.IFromPiece;
import com.squid.core.sql.render.IJoinDecorator;
import com.squid.core.sql.render.ISamplingDecorator;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;

/**
 * Wrap a SelectStatement so it can be used as a sub select
 *
 */
public class FromSelectStatementPiece extends BoundPiece
implements IFromPiece {

	private IStatement statement;
	private ISamplingDecorator sampling;
	private Collection<IJoinDecorator> joins;
	private String alias;
	private SelectStatement select;
	private Aliaser aliaser;
	private IJoinDecorator definingJoinDecorator;

	public FromSelectStatementPiece(IStatement statement, Scope scope, SelectStatement select,String alias) {
		super(scope);
		this.statement = statement;
		if (alias == null){
			this.aliaser = new Aliaser();
			this.alias = aliaser.getUniqueAlias();
		}else{
			this.alias = alias;
		}
		this.select = select;
		// TODO Auto-generated constructor stub
	}
	
	public IStatement getStatement() {
		return statement;
	}

	public void addJoinDecorator(IJoinDecorator join) {
		//
		if (joins==null) joins = new ArrayList<IJoinDecorator>();
		this.joins.add(join);
	}

	public Collection<IJoinDecorator> getJoinDecorators() {
		//
		return joins;
	}

	public ISamplingDecorator getSamplingDecorator() {
		//
		return sampling;
	}

	public void setSamplingDecorator(ISamplingDecorator sampling) {
		//
		this.sampling = sampling;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		// 
		return skin.render(skin,this);
	}

	public String getAlias() {
		//
		return alias;
	}

	public SelectStatement getSelect(){
		//
		return select;
	}
	
	@Override
	public boolean isDense() {
		// TODO check that hypothesis
		return true;
	}

	@Override
	public void setDefiningJoinDecorator(IJoinDecorator joinDecorator) {
		this.definingJoinDecorator = joinDecorator;
	}

	@Override
	public IJoinDecorator getDefiningJoinDecorator() {
		return this.definingJoinDecorator;
	}
	
}
