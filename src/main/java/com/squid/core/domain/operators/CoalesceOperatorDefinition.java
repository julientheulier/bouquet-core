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
import com.squid.core.domain.DomainNumeric;
import com.squid.core.domain.DomainString;
import com.squid.core.domain.IDomain;

public class CoalesceOperatorDefinition
extends ArithmeticOperatorDefintion {

	private static final String HINT = "COALESCE(expr,value)";

	public CoalesceOperatorDefinition(String name, int id) {
		super(name, id, OperatorDefinition.PREFIX_POSITION, IDomain.UNKNOWN);
		//

	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length>=2) {
			ExtendedType type = types[0];
			for (int i=1;i<types.length;i++) {
				if (types[i].getDomain().isInstanceOf(type.getDomain())) {
					int currentScale = type.getScale();
					int newScale = Math.max(type.getScale(), types[i].getScale());
					int newSize = type.getSize();
					if (currentScale<type.getScale()) {
						newSize = type.getSize()+type.getScale()-currentScale;
					}
					newSize = Math.max(newSize, types[i].getSize());
					type = type.scaleAndsize(newScale, newSize);
				} else if (type.getDomain().isInstanceOf(types[i].getDomain())) {
					// OK
				} else {
					return type;// first type is better...
				}
			}
			//
			return type;
		} else {
			return ExtendedType.FLOAT;
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
	public ListContentAssistEntry getListContentAssistEntry() {
		if (super.getListContentAssistEntry() == null) {

			List<String> descriptions = new ArrayList<String>();
			List types = getParametersTypes();
			for(int i = 0; i<types.size();i++){
				descriptions.add("Returns the first non null value");
			}
			ListContentAssistEntry entry = new ListContentAssistEntry(descriptions, types);
			setListContentAssistEntry(entry);

		}
		return super.getListContentAssistEntry();
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain any1 = new DomainAny();
		any1.setContentAssistLabel("any");
		any1.setContentAssistProposal("${1:any}");
		IDomain any2 = new DomainAny();
		any2.setContentAssistLabel("any");
		any2.setContentAssistProposal("${2:any}");
		type.add(any1);
		type.add(any2);
		poly.add(type);
		type = new ArrayList<IDomain>();
		IDomain any3 = new DomainAny();
		any3.setContentAssistLabel("any");
		any3.setContentAssistProposal("${3:any}");
		type.add(any1);
		type.add(any2);
		type.add(any3);
		poly.add(type);
		type = new ArrayList<IDomain>();
		IDomain any4 = new DomainAny();
		any4.setContentAssistLabel("any");
		any4.setContentAssistProposal("${4:any}");
		type.add(any1);
		type.add(any2);
		type.add(any3);
		type.add(any4);
		poly.add(type);
		type = new ArrayList<IDomain>();
		IDomain any5 = new DomainAny();
		any5.setContentAssistLabel("any");
		any5.setContentAssistProposal("${5:any}");
		type.add(any1);
		type.add(any2);
		type.add(any3);
		type.add(any4);
		type.add(any5);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		IDomain any6 = new DomainAny();
		any6.setContentAssistLabel("any");
		any6.setContentAssistProposal("${6:any}");
		type.add(any1);
		type.add(any2);
		type.add(any3);
		type.add(any4);
		type.add(any5);
		type.add(any6);
		poly.add(type);
		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty() || imageDomains.size()<=1) {
			return new OperatorDiagnostic("invalid COALESCE expression",HINT);
		}
		IDomain domain = imageDomains.get(0);
		for (int i=1;i<imageDomains.size();i++) {
			if (imageDomains.get(i).isInstanceOf(domain)) {
				domain = imageDomains.get(i);
			} else if (domain.isInstanceOf(imageDomains.get(i))) {
				// ok
			} else {
				return new OperatorDiagnostic("mismatching arguments",HINT);
			}
		}
		return OperatorDiagnostic.IS_VALID;
	}

}
