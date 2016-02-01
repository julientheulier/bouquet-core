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
package com.squid.core.domain.operators;

/**
 * Defining intrinsic order of precedence
 * @author serge fantino
 *
 */
public interface OperatorPrecedence {
	
	public final static int GROUPING_PRECEDENCE = 1;
	public final static int LOGICAL_NEGATION_PRECEDENCE = 2;
	public final static int PRODUCT_RING_PRECEDENCE = 3;
	public final static int ARITHMETIC_GROUP_PRECEDENCE = 4;
	public final static int INEQUALITY_PRECEDENCE = 5;
	public final static int EQUALITY_PRECEDENCE = 6;
	public final static int LOGICAL_AND_PRECEDENCE = 7;
	public final static int LOGICAL_OR_PRECEDENCE = 8;
	public final static int ASSIGMENT_OR_PRECEDENCE = 9;
	public final static int FUNCTION_PRECEDENCE = 10;
	

}
