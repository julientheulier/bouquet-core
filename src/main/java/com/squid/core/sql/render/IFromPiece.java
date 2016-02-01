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

import java.util.Collection;

import com.squid.core.sql.model.IAlias;
import com.squid.core.sql.statements.IStatement;

public interface IFromPiece 
extends IBoundPiece, IAlias
{
	
	public void setSamplingDecorator(ISamplingDecorator sampling);
	
	public ISamplingDecorator getSamplingDecorator();
	
	public void addJoinDecorator(IJoinDecorator join);
	
	public Collection<IJoinDecorator> getJoinDecorators();
	
	public boolean isDense();
	
	/**
	 * identify the defining statement; 
	 * in case of a select with sub-selects, this can be useful to figure out if two pieces are from the same defining statement...
	 * @return
	 */
	public IStatement getStatement();

	/**
	 * for inner, left, right, full... set the defining join decorator for this piece
	 * @param joinDecorator
	 */
	public void setDefiningJoinDecorator(IJoinDecorator joinDecorator);
	
	/**
	 * return the defining join decorator for this piece if exists or null otherwise
	 * @return
	 */
	public IJoinDecorator getDefiningJoinDecorator();

}
