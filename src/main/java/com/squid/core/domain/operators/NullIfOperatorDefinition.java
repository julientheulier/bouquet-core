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

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.IDomain;

public class NullIfOperatorDefinition extends ArithmeticOperatorDefinition {

	private static final String HINT = "NULLIF(expr,value)";

	public NullIfOperatorDefinition(String name, int id) {
		super(name, id, OperatorDefinition.PREFIX_POSITION, IDomain.UNKNOWN);
		this.setCategoryType(OperatorDefinition.LOGICAL_TYPE);
	}
	
	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length == 2) {
			ExtendedType type = types[0];
			return type;
		} else {
			return ExtendedType.UNDEFINED;
		}
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty()) {
			return IDomain.UNKNOWN;
		} else {
			return imageDomains.get(0);
		}
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("Returns a null value if the value equals the expression");
		return hint;
	}

	@Override
	public List<List> getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain any = new DomainAny();
		type.add(any);
		type.add(any);
		poly.add(type);
		/*
		 * type = new ArrayList<IDomain>(); type.add(any); type.add(any);
		 * type.add(any); poly.add(type); type = new ArrayList<IDomain>();
		 * type.add(any); type.add(any); type.add(any); type.add(any);
		 * poly.add(type); type = new ArrayList<IDomain>(); type.add(any);
		 * type.add(any); type.add(any); type.add(any); type.add(any);
		 * poly.add(type); type = new ArrayList<IDomain>(); ; type.add(any);
		 * type.add(any); type.add(any); type.add(any); type.add(any);
		 * type.add(any); poly.add(type);
		 */
		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty() || imageDomains.size() <= 1) {
			return new OperatorDiagnostic("invalid NULLIF expression", HINT);
		}
		IDomain domain = imageDomains.get(0);
		for (int i = 1; i < imageDomains.size(); i++) {
			if (imageDomains.get(i).isInstanceOf(domain)) {
				domain = imageDomains.get(i);
			} else if (domain.isInstanceOf(imageDomains.get(i))) {
				// ok
			} else {
				return new OperatorDiagnostic("mismatching NULLIF arguments", HINT);
			}
		}
		return OperatorDiagnostic.IS_VALID;
	}

}
