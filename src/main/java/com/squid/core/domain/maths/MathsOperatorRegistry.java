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
package com.squid.core.domain.maths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorRegistry;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.OperatorScopeException;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd 
 * Registry ANSI functions
 */

public class MathsOperatorRegistry 
implements OperatorRegistry
{
	public static final String MATHS_BASE = "com.squid.domain.operator.maths.";
	
	private final static Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);
	
	public MathsOperatorRegistry(OperatorScope scope) {
		try {
			apply(scope);
			logger.info("init MathsOperatorRegistry");
		} catch (OperatorScopeException e) {
			logger.error("unable to init the MathsOperatorRegistry", e);
		}
	}
	
	@Override
	public void apply(OperatorScope scope) throws OperatorScopeException {
		scope.registerExtension(new CeilOperatorDefinition("CEIL", CeilOperatorDefinition.CEIL,OperatorDefinition.MATHS_TYPE));
		scope.registerExtension(new FloorOperatorDefintion("FLOOR",FloorOperatorDefintion.FLOOR,OperatorDefinition.MATHS_TYPE));
		scope.registerExtension(new SignOperatorDefintion("SIGN",SignOperatorDefintion.SIGN,OperatorDefinition.MATHS_TYPE));		
		scope.registerExtension(new TruncateOperatorDefintion("TRUNCATE",TruncateOperatorDefintion.TRUNCATE,OperatorDefinition.MATHS_TYPE));
		scope.registerExtension(new RoundOperatorDefintion("ROUND",RoundOperatorDefintion.ROUND,OperatorDefinition.MATHS_TYPE));
		scope.registerExtension(new RandOperatorDefinition("RAND",RandOperatorDefinition.RAND,OperatorDefinition.MATHS_TYPE));
		scope.registerExtension(new PowerOperatorDefintion("POWER",PowerOperatorDefintion.POWER,OperatorDefinition.MATHS_TYPE));
		scope.registerExtension(new PiOperatorDefintion("PI",PiOperatorDefintion.PI,OperatorDefinition.TRIGO_TYPE));
		scope.registerExtension(new DegreesOperatorDefintion("DEGREES",DegreesOperatorDefintion.DEGREES,OperatorDefinition.TRIGO_TYPE));
		scope.registerExtension(new RadiansOperatorDefintion("RADIANS",RadiansOperatorDefintion.RADIANS,OperatorDefinition.TRIGO_TYPE));
		scope.registerExtension(new SinhCoshTanhOperatorDefintion("SINH",SinhCoshTanhOperatorDefintion.SINH,OperatorDefinition.TRIGO_TYPE));
		scope.registerExtension(new SinhCoshTanhOperatorDefintion("COSH",SinhCoshTanhOperatorDefintion.COSH,OperatorDefinition.TRIGO_TYPE));
		scope.registerExtension(new SinhCoshTanhOperatorDefintion("TANH",SinhCoshTanhOperatorDefintion.TANH,OperatorDefinition.TRIGO_TYPE));
		
		scope.registerExtension(new GreatestLeastOperatorDefinition("GREATEST",GreatestLeastOperatorDefinition.GREATEST,OperatorDefinition.MATHS_TYPE));
		scope.registerExtension(new GreatestLeastOperatorDefinition("LEAST",GreatestLeastOperatorDefinition.LEAST,OperatorDefinition.MATHS_TYPE));
	}

}
