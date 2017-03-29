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
package com.squid.core.domain.extensions.string;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainString;
import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/*
 * case
  when pos>1 and INSTR(string, delimiter, 1, pos - 1) = 0 then null
  else
    case when INSTR(string,delimiter,INSTR(string, delimiter, 1, pos - 1)+ length(delimiter),1)>0 then
    substr(string,INSTR(string, delimiter, 1, pos - 1)+ length(delimiter), INSTR(string,delimiter,INSTR(string, delimiter, 1, pos - 1)+ length(delimiter),1) - (INSTR(string, delimiter, 1, pos - 1)+ length(delimiter)))
    else
      substr(string,INSTR(string, delimiter, 1, pos - 1)+ length(delimiter))
    end
end
 */
public class SplitPartOperatorDefinition extends OperatorDefinition {

	public static final String STRING_BASE = "com.squid.domain.operator.string.";

	public static final String STRING_SPLIT_PART = STRING_BASE + "SPLIT_PART";

	private String hint = "";

	public SplitPartOperatorDefinition(String name, String ID, IDomain domain) {
		super(name, ID, PREFIX_POSITION, name, domain);
		hint = name + "(string, delimiter, position)";
		this.setCategoryType(OperatorDefinition.STRING_TYPE);
	}


	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}


	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("Split the input string using the string delimiter and takes the pos-th element.");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		IDomain string1 = new DomainString();
		string1.setContentAssistLabel("input_string");
		IDomain string2 = new DomainStringConstant("");
		string2.setContentAssistLabel("string_delim");
		IDomain num3 = new DomainNumericConstant();
		num3.setContentAssistLabel("position");

		type.add(string1);
		type.add(string2);
		type.add(num3);

		poly.add(type);

		return poly;

	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		if (types.length > 0) {
			return types[0];
		} else {
			return ExtendedType.UNDEFINED;
		}
	}

}
