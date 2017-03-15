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
		scope.registerExtension(new CeilOperatorDefinition("CEIL", CeilOperatorDefinition.CEIL));
		scope.registerExtension(new FloorOperatorDefinition("FLOOR",FloorOperatorDefinition.FLOOR));
		scope.registerExtension(new SignOperatorDefinition("SIGN",SignOperatorDefinition.SIGN));		
		scope.registerExtension(new TruncateOperatorDefinition("TRUNCATE",TruncateOperatorDefinition.TRUNCATE));
		scope.registerExtension(new RoundOperatorDefinition("ROUND",RoundOperatorDefinition.ROUND));
		scope.registerExtension(new RandOperatorDefinition("RAND",RandOperatorDefinition.RAND));
		scope.registerExtension(new PowerOperatorDefinition("POWER",PowerOperatorDefinition.POWER));
		scope.registerExtension(new PiOperatorDefinition("PI",PiOperatorDefinition.PI));
		scope.registerExtension(new DegreesOperatorDefintion("DEGREES",DegreesOperatorDefintion.DEGREES));
		scope.registerExtension(new RadiansOperatorDefintion("RADIANS",RadiansOperatorDefintion.RADIANS));
		scope.registerExtension(new SinhCoshTanhOperatorDefinition("SINH",SinhCoshTanhOperatorDefinition.SINH));
		scope.registerExtension(new SinhCoshTanhOperatorDefinition("COSH",SinhCoshTanhOperatorDefinition.COSH));
		scope.registerExtension(new SinhCoshTanhOperatorDefinition("TANH",SinhCoshTanhOperatorDefinition.TANH));
		
		scope.registerExtension(new GreatestLeastOperatorDefinition("GREATEST",GreatestLeastOperatorDefinition.GREATEST));
		scope.registerExtension(new GreatestLeastOperatorDefinition("LEAST",GreatestLeastOperatorDefinition.LEAST));
	}

}
