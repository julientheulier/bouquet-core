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
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.render.groupby.IGroupByElementPiece;
import com.squid.core.sql.statements.SelectStatement;
import com.squid.core.sql.statements.Statement;
import com.squid.core.sql.template.ISkinHandler;

public interface SQLSkin {
	
	/**
	 * turn on/off comments
	 * @param comments
	 */
	public void setComments(boolean comments);

	/**
	 * check if comments are enabled
	 * @return
	 */
	boolean isComments();

	/**
	 * normalize comment text (but it does not add comment syntax)
	 * @param text
	 * @return
	 */
    public String comment(String text);
    
    /**
     * output a comment (add the comment syntax)
     * @param text
     * @return
     */
    public String quoteComment(String text);

    public String quoteIdentifier(String identifier);

	public String quoteColumnIdentifier(String identifier);

    public String quoteColumnIdentifier(Column column);

    public String quoteTableIdentifier(String identifier);

    public String quoteTableIdentifier(Table table);

    public String quoteSchemaIdentifier(String identifier);

    public String quoteSchemaIdentifier(Schema schema);

    public String quoteLiteral(String literal);

	public String quoteEndOfStatement(String statement);

    public String getToken(int token) throws RenderingException;

    public String fullyQualified(Table table);

    /**
     * @param value
     */
    public String quoteConstant(Object value, IDomain domain);
    
    public OperatorDefinition overrideOperatorDefinition(OperatorDefinition op, ExtendedType[] args);
    
    /**
     * allow to override template ID
     * @param templateID
     * @return
     */
    public String overrideTemplateID(String templateID);
    
    /**
     * the skin prefix for getting template
     * @return
     */
    public String getSkinPrefix();
    
    /**
     * experimental: supporting sampling decorator...
     * @param size
     * @return
     * @throws RenderingException 
     */
    public ISamplingDecorator createSamplingDecorator(DelegateSamplingDecorator sampling) throws RenderingException;
    
    /**
     * invert the IPiece.render implementation to allow skin depedendant implementation
     * Should return NULL if the Skin does not actually support that IPiece rendering; that way we can built a simple overriding mechanism.
     * @throws ScopeException 
     */
    public String render(SQLSkin skin, IPiece piece) throws RenderingException;

	
	public String render(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef, String args[]) throws RenderingException;

	public String render(SQLSkin skin, IGroupByElementPiece piece) throws RenderingException;

    /**
     * @param id : the operator 
     * @return whether the operator can be rendered 
     */
    public boolean canRender (String id);

	/**
	 * @return all the functions that a skin can render
	 */
	public List<String> canRender ();

	public ExtendedType createExtendedType(IDomain domain);
	
	public ExtendedType createExtendedType(IDomain domain, int dataType,
			String format, int size, int precision);
	
	/**
	 * return the name of the column type for a column, fully formatted -- it is called directly from the template!
	 * @param column
	 * @return
	 */
	public String getTypeDefinition(Column column);
	
	/**
	 * return the name of the column type for a column, fully formatted -- it is called directly from the template!
	 * @param column
	 * @return
	 */
	public String getTypeDefinition(ExtendedType type);

	/**
	 * return the name of the column type for a column, fully formatted -- it is called directly from the template!
	 * @param column
	 * @return
	 */
	public String getTypeName(int SQLType);	
	
	// experimental
	/**
	 * return information regarding the feature support; if the feature is unknown to the system, return null;
	 * The resulting object may be interpreted depending on the feature.
	 */
	public ISkinFeatureSupport getFeatureSupport(String featureID);
	
	/**
	 * return information regarding the preferences for a feature ; by default return none;
	 * The resulting object may be interpreted depending on the feature.
	 */
	public ISkinPref getPreferences(String featureID);

	/**
	 * return the Skin Handler used to create that skin
	 * @return
	 */
	public ISkinHandler getSkinHandler();
	
	/**
	 * Render the FROM clause when it is empty (no table)
	 */
	public String renderEmptyFromClause();

	/**
	 * The PrepareStatement() method allows the skin to apply so database specific transformation to a statement before the statement get rendered.
	 * If the skin does not want to modify the statement, just return it - else make a copy!
	 * @param statement
	 * @return
	 */
	public Statement prepareStatement(final SelectStatement statement);
	
	/**
	 * Share-nothing architecture: this implies to create PKs at table creation for maximum optimization
	 */
	public boolean isShareNothing();

	/**
	 * Allow the Skin to initialize the connection with special setting depending on the database...
	 * @param conn
	 */
	public void initializeConnection(Connection conn);
	
	/**
	 * Get product
	 * 
	 */
	public DatabaseProduct getProduct();

}