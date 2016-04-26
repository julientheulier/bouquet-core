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
package com.squid.core.sql.render;

import java.sql.Connection;
import java.util.List;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.DatabaseProduct;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.render.groupby.IGroupByElementPiece;
import com.squid.core.sql.statements.SelectStatement;
import com.squid.core.sql.statements.Statement;
import com.squid.core.sql.template.ISkinHandler;

public class SQLSkinProxy 
implements SQLSkin
{
	
	private SQLSkin skin;

	public SQLSkinProxy(SQLSkin skin) {
		super();
		this.skin = skin;
	}

	public boolean canRender(String id) {
		return this.skin.canRender(id);
	}

	public List<String> canRender() {
		return this.skin.canRender();
	}

	public String comment(String text) {
		return this.skin.comment(text);
	}
	
	@Override
	public ExtendedType createExtendedType(IDomain domain) {
		return this.skin.createExtendedType(domain);
	}
	
	@Override
	public ExtendedType createExtendedType(IDomain domain, int dataType,
			String format, int size, int precision) {
		return this.skin.createExtendedType(domain, dataType, format, size, precision);
	}

	public ISamplingDecorator createSamplingDecorator(
			DelegateSamplingDecorator sampling) throws RenderingException {
		return this.skin.createSamplingDecorator(sampling);
	}

	public ISkinFeatureSupport getFeatureSupport(String featureID) {
		return this.skin.getFeatureSupport(featureID);
	}
	
	public ISkinPref getPreferences(String featureID) {
		return this.skin.getPreferences(featureID);
	}

	public String getSkinPrefix() {
		return this.skin.getSkinPrefix();
	}

	public String getToken(int token) throws RenderingException {
		return this.skin.getToken(token);
	}

	public OperatorDefinition overrideOperatorDefinition(OperatorDefinition op,
			ExtendedType[] args) {
		return this.skin.overrideOperatorDefinition(op, args);
	}

	public String overrideTemplateID(String templateID) {
		return this.skin.overrideTemplateID(templateID);
	}

	public String quoteColumnIdentifier(Column column) {
		return this.skin.quoteColumnIdentifier(column);
	}

	public String quoteColumnIdentifier(String identifier) {
		return this.skin.quoteColumnIdentifier(identifier);
	}

	public String quoteComment(String text) {
		return this.skin.quoteComment(text);
	}
	
	@Override
	public void setComments(boolean comments) {
		this.skin.setComments(comments);
	}

	@Override
	public boolean isComments() {
		return this.skin.isComments();	
	}
	
	public String quoteConstant(Object value, IDomain domain) {
		return this.skin.quoteConstant(value, domain);
	}

	public String quoteIdentifier(String identifier) {
		return this.skin.quoteIdentifier(identifier);
	}

	public String quoteLiteral(String literal) {
		return this.skin.quoteLiteral(literal);
	}

	@Override
	public String quoteEndOfStatement(String statement) {
		return this.skin.quoteEndOfStatement(statement);
	}

	public String quoteSchemaIdentifier(String identifier) {
		return this.skin.quoteSchemaIdentifier(identifier);
	}
	
	@Override
	public String quoteSchemaIdentifier(Schema schema) {
		return this.skin.quoteSchemaIdentifier(schema);
	}

	public String quoteTableIdentifier(String identifier) {
		return this.skin.quoteTableIdentifier(identifier);
	}
	
	@Override
	public String quoteTableIdentifier(Table table) {
		return this.skin.quoteTableIdentifier(table);
	}

	@Override
	public String render(SQLSkin skin, IPiece piece) throws RenderingException {
		return this.skin.render(skin,piece);
	}

	@Override
	public String render(SQLSkin skin, OperatorPiece piece,
			OperatorDefinition opDef, String[] args) throws RenderingException {
		return this.skin.render(skin, piece, opDef, args);
	}
	
	@Override
	public String render(SQLSkin skin, IGroupByElementPiece piece) throws RenderingException {
	    return this.skin.render(skin, piece);
	}

	@Override
	public ISkinHandler getSkinHandler() {
		return this.skin.getSkinHandler();
	}
	
	@Override
	public String getTypeDefinition(Column column) {
		return this.skin.getTypeDefinition(column);
	}
	
	@Override
	public String getTypeDefinition(ExtendedType type) {
		return this.skin.getTypeDefinition(type);
	}
	
	@Override
	public String fullyQualified(Table table) {
		return this.skin.fullyQualified(table);
	}

	@Override
	public String renderEmptyFromClause() {
		return this.skin.renderEmptyFromClause();
	}
	
	@Override
	public Statement prepareStatement(SelectStatement statement) {
		// TODO Auto-generated method stub
		return this.skin.prepareStatement(statement);
	}

	@Override
	public String getTypeName(int SQLType) {
		// TODO Auto-generated method stub
		return this.skin.getTypeName(SQLType);
	}

	@Override
	public boolean isShareNothing() {
		// TODO Auto-generated method stub
		return this.skin.isShareNothing();
	}
	
	@Override
	public void initializeConnection(Connection conn) {
		this.skin.initializeConnection(conn);
	}

	@Override
	public DatabaseProduct getProduct() {
		return this.skin.getProduct();
	}
}
