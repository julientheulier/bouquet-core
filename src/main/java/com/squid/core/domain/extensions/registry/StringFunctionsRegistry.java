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
package com.squid.core.domain.extensions.registry;

import com.squid.core.domain.extensions.string.PosStringOperatorDefinition;
import com.squid.core.domain.extensions.string.SplitPartOperatorDefinition;
import com.squid.core.domain.extensions.string.StringLengthOperatorsDefinition;
import com.squid.core.domain.extensions.string.SubstringOperatorDefinition;
import com.squid.core.domain.extensions.string.oneArgStringOperator.*;
import com.squid.core.domain.extensions.string.pad.PadLPadOperatorDefinition;
import com.squid.core.domain.extensions.string.pad.PadOperatorDefinition;
import com.squid.core.domain.extensions.string.pad.PadRPadOperatorDefinition;
import com.squid.core.domain.extensions.string.regex.*;
import com.squid.core.domain.extensions.string.translate.TranslateOperatorDefinition;
import com.squid.core.domain.extensions.string.translate.TranslateReplaceOperatorDefinition;
import com.squid.core.domain.extensions.string.translate.TranslateTranslateOperatorDefinition;
import com.squid.core.domain.extensions.string.trim.TrimLTrimOperatorDefinition;
import com.squid.core.domain.extensions.string.trim.TrimOperatorDefinition;
import com.squid.core.domain.extensions.string.trim.TrimRTrimOperatorDefinition;
import com.squid.core.domain.extensions.string.trim.TrimTrimOperatorDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorRegistry;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.OperatorScopeException;

public class StringFunctionsRegistry implements OperatorRegistry {

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

	@Override
	public void apply(OperatorScope scope) throws OperatorScopeException {
		scope.registerExtension(new SubstringOperatorDefinition("SUBSTRING",
				SubstringOperatorDefinition.STRING_SUBSTRING, IDomain.STRING, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new PosStringOperatorDefinition("POSITION", PosStringOperatorDefinition.STRING_POSITION,
				IDomain.NUMERIC, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new OneArgStringUpperOperatorDefinition("UPPER", OneArgStringOperatorDefinition.STRING_UPPER,
				IDomain.STRING, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new OneArgStringLowerOperatorDefinition("LOWER", OneArgStringOperatorDefinition.STRING_LOWER,
				IDomain.STRING, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new OneArgStringReverseOperatorDefinition("REVERSE",
				OneArgStringOperatorDefinition.STRING_REVERSE, IDomain.STRING, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new OneArgStringMD5OperatorDefinition("MD5", OneArgStringOperatorDefinition.STRING_MD5,
				IDomain.STRING, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new SplitPartOperatorDefinition("SPLIT_PART",
				SplitPartOperatorDefinition.STRING_SPLIT_PART, IDomain.STRING, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new StringLengthOperatorsDefinition("LENGTH",
				StringLengthOperatorsDefinition.STRING_LENGTH, IDomain.NUMERIC, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new TrimTrimOperatorDefinition("TRIM", TrimOperatorDefinition.STRING_TRIM, IDomain.STRING,
				OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new TrimLTrimOperatorDefinition("LTRIM", TrimOperatorDefinition.STRING_LTRIM, IDomain.STRING,
				OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new TrimRTrimOperatorDefinition("RTRIM", TrimOperatorDefinition.STRING_RTRIM, IDomain.STRING,
				OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new PadLPadOperatorDefinition("LPAD", PadOperatorDefinition.STRING_LPAD, IDomain.STRING,
				OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new PadRPadOperatorDefinition("RPAD", PadOperatorDefinition.STRING_RPAD, IDomain.STRING,
				OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new TranslateReplaceOperatorDefinition("REPLACE", TranslateOperatorDefinition.STRING_REPLACE,
				IDomain.STRING, OperatorDefinition.STRING_TYPE));
		scope.registerExtension(new TranslateTranslateOperatorDefinition("TRANSLATE",
				TranslateOperatorDefinition.STRING_TRANSLATE, IDomain.STRING, OperatorDefinition.STRING_TYPE));

		scope.registerExtension(new RegexpCountOperatorDefinition("REGEXP_COUNT", RegexpOperatorDefinition.REGEXP_COUNT,
				IDomain.NUMERIC, OperatorDefinition.REGEXP_TYPE));
		scope.registerExtension(new RegexpInstrOperatorDefinition("REGEXP_INSTR", RegexpOperatorDefinition.REGEXP_INSTR,
				IDomain.NUMERIC, OperatorDefinition.REGEXP_TYPE));
		scope.registerExtension(new RegexpReplaceOperatorDefinition("REGEXP_REPLACE", RegexpOperatorDefinition.REGEXP_REPLACE,
				IDomain.STRING, OperatorDefinition.REGEXP_TYPE));
		scope.registerExtension(new RegexpSubstringOperatorDefinition("REGEXP_SUBSTRING", RegexpOperatorDefinition.REGEXP_SUBSTR,
				IDomain.STRING, OperatorDefinition.REGEXP_TYPE));

	}

}
