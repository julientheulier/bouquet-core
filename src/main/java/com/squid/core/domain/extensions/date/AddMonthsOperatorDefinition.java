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
package com.squid.core.domain.extensions.date;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.*;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class AddMonthsOperatorDefinition
extends OperatorDefinition {

	public static final String ADD_MONTHS_BASE = "com.squid.domain.operator.date.";
	public static final String ADD_MONTHS = ADD_MONTHS_BASE+"ADD_MONTHS";

	private static final String HINT = "ADD_MONTHS(date or timestamp,integer)";

	public AddMonthsOperatorDefinition(String name, int id, IDomain domain, int categoryType) {
		super(name, id, domain, categoryType);
		setParamCount(2);
	}

	public AddMonthsOperatorDefinition(String name, String ID) {
		super(name,ID,PREFIX_POSITION,name,IDomain.NUMERIC);
	}

	public AddMonthsOperatorDefinition(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.NUMERIC,categoryType);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public ListContentAssistEntry getListContentAssistEntry(){
		if(super.getListContentAssistEntry()==null){
			List <String> descriptions = new ArrayList<String>();
			descriptions.add("Add n months to the date");
			descriptions.add("Add n months to the timestamp");
			ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
			setListContentAssistEntry(entry);
		}
		return super.getListContentAssistEntry();
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		IDomain date = new DomainDate();
		date.setContentAssistLabel("date");
		date.setContentAssistProposal("${1:date}");
		IDomain timestamp = new DomainTimestamp();
		timestamp.setContentAssistLabel("timestamp");
		timestamp.setContentAssistProposal("${1:timestamp}");
		IDomain num = new DomainNumeric();
		num.setContentAssistLabel("n");
		num.setContentAssistProposal("${2:n}");
		type.add(date);
		type.add(num);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(timestamp);
		type.add(num);
		poly.add(type);
		return poly;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length!=2) {
			return ExtendedType.UNDEFINED;
		} else {
			return types[0];
		}
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()>0) {
			//setDomain(types[0].getDomain());
			return imageDomains.get(0);
		} else {
			return IDomain.NUMERIC;
		}
	}

}
