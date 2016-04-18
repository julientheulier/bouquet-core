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
 * Static definition of intrinsic operators
 * @author serge fantino
 */
public interface IntrinsicOperators {

	public static final String INTRINSIC_EXTENDED_ID = "com.squid.domain.intrinsic";

	// note: always use incrementing ID !!!

	public static final int EXTENDED_ID = -1;
	public static final int UNDEFINED_ID = 0;
	public static final int START_ID = UNDEFINED_ID;
	public static final int FIRST_ID = UNDEFINED_ID+1;
	public static final int CONCAT 		= FIRST_ID;
	public static final int PLUS 		= FIRST_ID+1;
	public static final int MINUS 		= FIRST_ID+2;
	public static final int MULTIPLY 	= FIRST_ID+3;
	public static final int DIVIDE 		= FIRST_ID+4;
	public static final int MAX 		= FIRST_ID+5;
	public static final int MIN 		= FIRST_ID+6;
	public static final int AVG 		= FIRST_ID+7;
	public static final int SUM 		= FIRST_ID+8;
	public static final int MEDIAN 		= FIRST_ID+9;
	public static final int ABS 		= FIRST_ID+10;
	public static final int VARIANCE 	= FIRST_ID+11;
	public static final int COUNT       = FIRST_ID+12;
	public static final int LESS        = FIRST_ID+13;
	public static final int LESS_OR_EQUAL= FIRST_ID+14;
	public static final int GREATER     = FIRST_ID+15;
	public static final int GREATER_OR_EQUAL= FIRST_ID+16;
	public static final int EQUAL       = FIRST_ID+17;
	public static final int NOT_EQUAL   = FIRST_ID+18;
	public static final int NOT         = FIRST_ID+19;
	public static final int AND         = FIRST_ID+20;
	public static final int OR          = FIRST_ID+21;
	public static final int IDENTITY    = FIRST_ID+22;
	public static final int MODULO 		= FIRST_ID+23;
	public static final int EXPONENTIATE= FIRST_ID+24;
	public static final int STDDEV = FIRST_ID+25;
	public static final int LIKE       = FIRST_ID+26;
	public static final int VAR_SAMP       = FIRST_ID+27;
	public static final int STDDEV_POP = FIRST_ID+28;
	public static final int STDDEV_SAMP = FIRST_ID+29;
	public static final int COVAR_POP       = FIRST_ID+30;
	public static final int PERCENTILE = FIRST_ID+31;
	public static final int COUNT_DISTINCT = FIRST_ID+32;
  public static final int RLIKE = FIRST_ID+33;

	public static final int ADD_MONTHS  = FIRST_ID+100;
	public static final int ISNULL		= FIRST_ID+101;
	public static final int RANK		= FIRST_ID+102;
	public static final int IS_NOTNULL	= FIRST_ID+103;
	public static final int IN			= FIRST_ID+104;
	
	public static final int CASE		= FIRST_ID+120;

	public static final int SUBTRACTION = FIRST_ID+200;// added to make the distinction with MINUS...

	public static final int EXISTS		= FIRST_ID+300;

	public static final int COALESCE		= FIRST_ID+400;

	public static final int START_PUBLIC_ID    = FIRST_ID+1000;

}
