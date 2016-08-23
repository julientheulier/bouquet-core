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
package com.squid.core.domain.extensions.JSON;

import java.sql.Types;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class JSONOperatorDefinition extends OperatorDefinition {

  public static final String JSON_BASE = "com.squid.domain.operator.json.";
  public static final String JSON_ARRAY_LENGTH = JSON_BASE + "JSON_ARRAY_LENGTH";
  public static final String JSON_EXTRACT_FROM_ARRAY = JSON_BASE + "JSON_EXTRACT_ARRAY_ELEMENT_TEXT";
  public static final String JSON_EXTRACT_PATH_TEXT = JSON_BASE + "JSON_EXTRACT_PATH_TEXT";

  public JSONOperatorDefinition(String name, String ID, IDomain domain) {
    super(name, ID, PREFIX_POSITION, name, domain);
  }

  public JSONOperatorDefinition(String name, String ID, IDomain domain, int categoryType) {
    super(name, ID, PREFIX_POSITION, name, domain, categoryType);
  }

  @Override
  public int getType() {
    return ALGEBRAIC_TYPE;
  }

  @Override
  public ExtendedType computeExtendedType(ExtendedType[] types) {
    return new ExtendedType(IDomain.NUMERIC, Types.INTEGER, 0, 0);
  }

  @Override
  public IDomain computeImageDomain(List<IDomain> imageDomains) {
    return IDomain.NUMERIC;
  }

}
