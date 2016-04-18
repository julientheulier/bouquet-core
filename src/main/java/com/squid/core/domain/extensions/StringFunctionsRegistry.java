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
    scope.registerExtension(new SubstringOperatorDefinition("SUBSTRING", SubstringOperatorDefinition.STRING_SUBSTRING, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new PosStringOperatorDefinition("POSITION", PosStringOperatorDefinition.STRING_POSITION, IDomain.NUMERIC, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new OneArgStringOperatorDefinition("UPPER", OneArgStringOperatorDefinition.STRING_UPPER, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new OneArgStringOperatorDefinition("LOWER", OneArgStringOperatorDefinition.STRING_LOWER, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new OneArgStringOperatorDefinition("REVERSE", OneArgStringOperatorDefinition.STRING_REVERSE, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new OneArgStringOperatorDefinition("MD5", OneArgStringOperatorDefinition.STRING_MD5, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new OneArgStringOperatorDefinition("SPLIT_PART", SplitPartOperatorDefinition.STRING_SPLIT_PART, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new StringLengthOperatorsDefinition("LENGTH", StringLengthOperatorsDefinition.STRING_LENGTH, IDomain.NUMERIC, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new TrimOperatorDefinition("TRIM", TrimOperatorDefinition.STRING_TRIM, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new TrimOperatorDefinition("LTRIM", TrimOperatorDefinition.STRING_LTRIM, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new TrimOperatorDefinition("RTRIM", TrimOperatorDefinition.STRING_RTRIM, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new PadOperatorDefinition("LPAD", PadOperatorDefinition.STRING_LPAD, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new PadOperatorDefinition("RPAD", PadOperatorDefinition.STRING_RPAD, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new TranslateOperatorDefinition("REPLACE", TranslateOperatorDefinition.STRING_REPLACE, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new TranslateOperatorDefinition("TRANSLATE", TranslateOperatorDefinition.STRING_TRANSLATE, IDomain.STRING, OperatorDefinition.STRING_TYPE));

    scope.registerExtension(new RegexpOperatorDefinition("REGEXP_COUNT", RegexpOperatorDefinition.REGEXP_COUNT, IDomain.NUMERIC, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new RegexpOperatorDefinition("REGEXP_INSTR", RegexpOperatorDefinition.REGEXP_INSTR, IDomain.NUMERIC, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new RegexpOperatorDefinition("REGEXP_REPLACE", RegexpOperatorDefinition.REGEXP_REPLACE, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    scope.registerExtension(new RegexpOperatorDefinition("REGEXP_SUBSTRING", RegexpOperatorDefinition.REGEXP_SUBSTR, IDomain.STRING, OperatorDefinition.STRING_TYPE));

  }

}
