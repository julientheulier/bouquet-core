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
package com.squid.core.domain.extensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorRegistry;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.OperatorScopeException;

public class StringFunctionsRegistry implements OperatorRegistry{

	public static final String STRING_BASE = "com.squid.domain.operator.string.";
	
	private final static Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);
	
	public StringFunctionsRegistry(OperatorScope scope) {
		try {
			apply(scope);
			logger.info("init StringFunctionsRegistry");
		} catch (OperatorScopeException e) {
			logger.error("unable to init the StringFunctionsRegistry", e);
		}
	}

	public void apply(OperatorScope scope) throws OperatorScopeException {
		SubstringOperatorDefinition substring = new SubstringOperatorDefinition("SUBSTRING",SubstringOperatorDefinition.STRING_SUBSTRING,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(substring);
		PosStringOperatorDefinition position = new PosStringOperatorDefinition("POSITION",PosStringOperatorDefinition.STRING_POSITION,IDomain.NUMERIC, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(position);
		UpperLowerOperatorsDefinition upper = new UpperLowerOperatorsDefinition("UPPER",UpperLowerOperatorsDefinition.STRING_UPPER,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(upper);
		UpperLowerOperatorsDefinition lower = new UpperLowerOperatorsDefinition("LOWER",UpperLowerOperatorsDefinition.STRING_LOWER,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(lower);
		StringLengthOperatorsDefinition length = new StringLengthOperatorsDefinition("LENGTH",StringLengthOperatorsDefinition.STRING_LENGTH,IDomain.NUMERIC, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(length);
		TrimOperatorDefinition trim = new TrimOperatorDefinition("TRIM",TrimOperatorDefinition.STRING_TRIM,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(trim);
		TrimOperatorDefinition ltrim = new TrimOperatorDefinition("LTRIM",TrimOperatorDefinition.STRING_LTRIM,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(ltrim);
		TrimOperatorDefinition rtrim = new TrimOperatorDefinition("RTRIM",TrimOperatorDefinition.STRING_RTRIM,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(rtrim);
		PadOperatorDefinition lpad = new PadOperatorDefinition("LPAD",PadOperatorDefinition.STRING_LPAD,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(lpad);
		PadOperatorDefinition rpad = new PadOperatorDefinition("RPAD",PadOperatorDefinition.STRING_RPAD,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(rpad);
		TranslateOperatorDefinition replace = new TranslateOperatorDefinition("REPLACE",TranslateOperatorDefinition.STRING_REPLACE,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(replace);
		TranslateOperatorDefinition translate = new TranslateOperatorDefinition("TRANSLATE",TranslateOperatorDefinition.STRING_TRANSLATE,IDomain.STRING, OperatorDefinition.STRING_TYPE);
		scope.registerExtension(translate);
	}

}
