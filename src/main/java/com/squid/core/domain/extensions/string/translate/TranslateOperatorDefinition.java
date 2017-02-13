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
package com.squid.core.domain.extensions.string.translate;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainString;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.extensions.registry.StringFunctionsRegistry;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class TranslateOperatorDefinition extends OperatorDefinition {

	public static final String STRING_REPLACE= StringFunctionsRegistry.STRING_BASE+"REPLACE";
	public static final String STRING_TRANSLATE= StringFunctionsRegistry.STRING_BASE+"TRANSLATE";

	private String hint = "";

	public TranslateOperatorDefinition(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,IDomain.STRING);
		setDomain(domain);
		hint = name+"(string, string_from, string_to)";
		this.setCategoryType(OperatorDefinition.STRING_TYPE);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("Return the input string with all the occurences of from_string replaced by the to_string");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		IDomain string1 = new DomainString();
		string1.setContentAssistLabel("input_string");

		IDomain string2 = new DomainString();
		string2.setContentAssistLabel("from_string");

		IDomain string3 = new DomainString();
		string2.setContentAssistLabel("to_string");

		type.add(string1);
		type.add(string2);
		type.add(string3);

		poly.add(type);

		return poly;

	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length>0) {
			return types[0];
		} else {
			return ExtendedType.UNDEFINED;
		}
	}

}
