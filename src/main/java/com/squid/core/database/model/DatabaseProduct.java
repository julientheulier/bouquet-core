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
package com.squid.core.database.model;

import com.squid.core.sql.render.SQLSkin;

/**
 * The DatabaseProduct object identifies a kind of database given a product name and version. It can be used to override some SQL features.
 * @author sfantino
 *
 */
public interface DatabaseProduct {

	public String getProductName();

	public void setProductName(String productName);

	public String getProductVersion();

	public void setProductVersion(String productVersion);

	public int getMajorVersion();

	public void setMajorVersion(int majorVersion);

	public int getMinorVersion();

	public void setMinorVersion(int minorVersion);
	
	/**
	 * get the SQLSkin for that database type
	 * @return
	 */
	public SQLSkin getSkin();

	public String getProductFullName();

}
