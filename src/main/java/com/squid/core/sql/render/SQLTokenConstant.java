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

public class SQLTokenConstant {
	
	public static final int SELECT = 0;
	public static final int FROM = 1;
	public static final int WHERE = 2;
	public static final int UPDATE = 3;
	public static final int INSERT = 4;
	public static final int AND = 5;
	public static final int OR = 6;
	public static final int NULL = 7;
	public static final int IN = 8;
	public static final int NOT = 9;
	public static final int MERGE = 15;
	public static final int NULLS_FIRST = 16;
	public static final int NULLS_LAST = 17;
	
	// join
	public static final int LEFT_OUTER_JOIN = 10;
	public static final int RIGHT_OUTER_JOIN = 11;
	public static final int FULL_OUTER_JOIN = 12;
	public static final int INNER_JOIN = 13;
	public static final int CROSS_JOIN = 14;

}
