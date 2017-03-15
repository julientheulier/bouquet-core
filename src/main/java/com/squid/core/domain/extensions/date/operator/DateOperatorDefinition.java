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
package com.squid.core.domain.extensions.date.operator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.squid.core.domain.DomainMetaDomain;
import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class DateOperatorDefinition extends OperatorDefinition {

	public static final String DATE_BASE = "com.squid.domain.operator.date.";
	public static final String DATE_MONTHS_BETWEEN = DATE_BASE+"MONTHS_BETWEEN";
	public static final String DATE_SUB = DATE_BASE+"DATE_SUB";
	public static final String DATE_ADD = DATE_BASE+"DATE_ADD";
	public static final String DATE_INTERVAL = DATE_BASE+"DATE_INTERVAL";
	public static final String CURRENT_DATE = DATE_BASE+"CURRENT_DATE";
	public static final String CURRENT_TIMESTAMP = DATE_BASE+"CURRENT_TIMESTAMP";
	public static final String TO_UNIXTIME = DATE_BASE+"TO_UNIXTIME";
	public static final String FROM_UNIXTIME = DATE_BASE+"FROM_UNIXTIME";
	
	public static final Hashtable<String, IDomain> periods =  new Hashtable<String, IDomain>();
	

	public DateOperatorDefinition(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
        this.setCategoryType(OperatorDefinition.DATE_TIME_TYPE);
		init();
	}
	
	// for backward compatibily in plug ins
	public DateOperatorDefinition(String name, String ID, IDomain domain, int categoryName) {
		super(name,ID,PREFIX_POSITION,name,domain);
        this.setCategoryType(OperatorDefinition.DATE_TIME_TYPE);
		init();
	}

	
	public void init() {
		periods.put("SECOND", IDomain.TIMESTAMP);
		periods.put("MINUTE", IDomain.TIMESTAMP);
		periods.put("HOUR", IDomain.TIMESTAMP);
		periods.put("DAY", IDomain.DATE);
		periods.put("MONTH", IDomain.DATE);
		periods.put("YEAR", IDomain.DATE);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}



}
