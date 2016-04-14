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

import java.sql.Types;
import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class JSONOperatorDefinition extends OperatorDefinition {

  public static final String JSON_BASE = "com.squid.domain.operator.j.";
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
  public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
    if (imageDomains.size() >= 1) {
      if (!imageDomains.get(0).isInstanceOf(IDomain.STRING)) {
        return new OperatorDiagnostic("1st parameter of function must be the json field as text", getName());
      }
      if (JSON_EXTRACT_FROM_ARRAY.equals(this.getExtendedID())) {
        if (imageDomains.size() == 2) {
          if (!imageDomains.get(1).isInstanceOf(IDomain.NUMERIC)) {
            return new OperatorDiagnostic("Please specify the index number", getName() + "(json, index)");
          }
        } else {
          return new OperatorDiagnostic("Invalid number of parameters, please check function definition", getName() + "(json, index)");
        }
      } else if (JSON_EXTRACT_PATH_TEXT.equals(this.getExtendedID())) {
        if (imageDomains.size() >= 2) {
          for (int i = 1; i < imageDomains.size(); i++) {
            if (!imageDomains.get(i).isInstanceOf(IDomain.STRING)) {
              return new OperatorDiagnostic("A path is a list of keys", getName() + "(json, key1, ...)");
            }
          }
        } else {
          return new OperatorDiagnostic("Invalid number of parameters, please check function definition", getName());
        }
      }
      return OperatorDiagnostic.IS_VALID;
    } else {
      return new OperatorDiagnostic("Invalid number of parameters, please check function definition", getName());
    }

  }

  @Override
  public ExtendedType computeExtendedType(ExtendedType[] types) {
    if (JSON_EXTRACT_FROM_ARRAY.equals(this.getExtendedID())) {
      return new ExtendedType(IDomain.STRING, Types.VARCHAR, 0, (types[0].getSize()));
    } else if (JSON_EXTRACT_PATH_TEXT.equals(this.getExtendedID())) {
      return new ExtendedType(IDomain.STRING, Types.VARCHAR, 0, (types[0].getSize()));
    }
    return new ExtendedType(IDomain.NUMERIC, Types.INTEGER, 0, 0);
  }

  @Override
  public IDomain computeImageDomain(List<IDomain> imageDomains) {
    if (JSON_EXTRACT_FROM_ARRAY.equals(this.getExtendedID())) {
      return IDomain.STRING;
    } else if (JSON_EXTRACT_PATH_TEXT.equals(this.getExtendedID())) {
      return IDomain.STRING;
    }
    return IDomain.NUMERIC;
  }

}
