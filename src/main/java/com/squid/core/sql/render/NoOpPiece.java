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


/**
 * The NoOpPiece is a piece that should be ignore: it can be usefull in some situation where the general case will generate a IPiece but some special case can require performing side-effect...
 * @author serge fantino
 *
 */
public class NoOpPiece 
implements IPiece
{
	
	/**
	 * use the NOOP singleton: this is to avoid testing the IPiece type; you would rather use a MyPiece==NOOP simple test
	 */
	public static final NoOpPiece NOOP = new NoOpPiece();
	
	/**
	 * Use the NOOP singleton instead
	 */
	protected NoOpPiece() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.squid.core.sql2.render.IPiece#render(com.squid.core.sql2.render.SQLSkin)
	 */
	@Override
	public String render(SQLSkin skin) throws RenderingException {
		return "";
	}

}
