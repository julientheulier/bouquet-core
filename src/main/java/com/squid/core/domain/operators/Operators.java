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

import com.squid.core.domain.extensions.CastOperatorDefinition;
import com.squid.core.domain.extensions.DateOperatorDefinition;
import com.squid.core.domain.extensions.DateTruncateOperatorDefinition;
import com.squid.core.domain.vector.VectorOperatorDefinition;

/**
 * Provides quick access to most usefull operators
 * @author sfantino
 *
 */
public class Operators {

	public static final OperatorDefinition IDENTITY = OperatorScope.getDefault().lookupByID(IntrinsicOperators.IDENTITY);
	public static final OperatorDefinition COUNT = OperatorScope.getDefault().lookupByID(IntrinsicOperators.COUNT);
	public static final OperatorDefinition COUNT_DISTINCT = OperatorScope.getDefault().lookupByID(IntrinsicOperators.COUNT_DISTINCT);
	public static final OperatorDefinition ADD = OperatorScope.getDefault().lookupByID(IntrinsicOperators.PLUS);
	public static final OperatorDefinition SUM = OperatorScope.getDefault().lookupByID(IntrinsicOperators.SUM);
	public static final OperatorDefinition MULTIPLY = OperatorScope.getDefault().lookupByID(IntrinsicOperators.MULTIPLY);
	public static final OperatorDefinition DIVIDE = OperatorScope.getDefault().lookupByID(IntrinsicOperators.DIVIDE);
	public static final OperatorDefinition SUBTRACTION = OperatorScope.getDefault().lookupByID(IntrinsicOperators.SUBTRACTION);
	public static final OperatorDefinition AVG = OperatorScope.getDefault().lookupByID(IntrinsicOperators.AVG);
	public static final OperatorDefinition MAX = OperatorScope.getDefault().lookupByID(IntrinsicOperators.MAX);
	public static final OperatorDefinition MIN = OperatorScope.getDefault().lookupByID(IntrinsicOperators.MIN);
	public static final OperatorDefinition VAR_POP = OperatorScope.getDefault().lookupByID(IntrinsicOperators.VARIANCE);
	public static final OperatorDefinition VAR_SAMP = OperatorScope.getDefault().lookupByID(IntrinsicOperators.VAR_SAMP);
	public static final OperatorDefinition ISNULL = OperatorScope.getDefault().lookupByID(IntrinsicOperators.ISNULL);
	public static final OperatorDefinition IS_NOTNULL = OperatorScope.getDefault().lookupByID(IntrinsicOperators.IS_NOTNULL);
	public static final OperatorDefinition NOT = OperatorScope.getDefault().lookupByID(IntrinsicOperators.NOT);
	public static final OperatorDefinition EQUAL = OperatorScope.getDefault().lookupByID(IntrinsicOperators.EQUAL);
	public static final OperatorDefinition LESS_OR_EQUAL = OperatorScope.getDefault().lookupByID(IntrinsicOperators.LESS_OR_EQUAL);
	public static final OperatorDefinition LESS = OperatorScope.getDefault().lookupByID(IntrinsicOperators.LESS);
	public static final OperatorDefinition CASE = OperatorScope.getDefault().lookupByID(IntrinsicOperators.CASE);
	public static final OperatorDefinition GREATER_OR_EQUAL = OperatorScope.getDefault().lookupByID(IntrinsicOperators.GREATER_OR_EQUAL);
	public static final OperatorDefinition GREATER = OperatorScope.getDefault().lookupByID(IntrinsicOperators.GREATER);
	public static final OperatorDefinition AND = OperatorScope.getDefault().lookupByID(IntrinsicOperators.AND);
	public static final OperatorDefinition OR = OperatorScope.getDefault().lookupByID(IntrinsicOperators.OR);
	public static final OperatorDefinition CAST_TO_TIMESTAMP = OperatorScope.getDefault().lookupByExtendedID(CastOperatorDefinition.TO_TIMESTAMP);
	public static final OperatorDefinition CAST_TO_DATE = OperatorScope.getDefault().lookupByExtendedID(CastOperatorDefinition.TO_DATE);
	public static final OperatorDefinition CAST_TO_INTEGER = OperatorScope.getDefault().lookupByExtendedID(CastOperatorDefinition.TO_INTEGER);
	public static final OperatorDefinition CAST_TO_FLOAT = OperatorScope.getDefault().lookupByExtendedID(CastOperatorDefinition.TO_NUMBER);
	public static final OperatorDefinition TO_DATE = OperatorScope.getDefault().lookupByExtendedID(CastOperatorDefinition.TO_DATE);
	public static final OperatorDefinition TO_CHAR = OperatorScope.getDefault().lookupByExtendedID(CastOperatorDefinition.TO_CHAR);
	public static final OperatorDefinition DATE_TRUNCATE = OperatorScope.getDefault().lookupByExtendedID(DateTruncateOperatorDefinition.DATE_TRUNCATE);
	public static final OperatorDefinition PERCENTILE = OperatorScope.getDefault().lookupByID(IntrinsicOperators.PERCENTILE);
	public static final OperatorDefinition COALESCE = OperatorScope.getDefault().lookupByID(IntrinsicOperators.COALESCE);
	
	//
	
	public static final OperatorDefinition DATE_ADD = OperatorScope.getDefault().lookupByExtendedID(DateOperatorDefinition.DATE_ADD);
	public static final OperatorDefinition DATE_SUB = OperatorScope.getDefault().lookupByExtendedID(DateOperatorDefinition.DATE_SUB);
	
	public static final OperatorDefinition CONCAT = OperatorScope.getDefault().lookupByID(IntrinsicOperators.CONCAT);

	public static final OperatorDefinition VECTOR = OperatorScope.getDefault().lookupByExtendedID(VectorOperatorDefinition.ID);
	public static final OperatorDefinition IN = OperatorScope.getDefault().lookupByID(IntrinsicOperators.IN);
	public static final OperatorDefinition EXISTS = OperatorScope.getDefault().lookupByID(IntrinsicOperators.EXISTS);

}
