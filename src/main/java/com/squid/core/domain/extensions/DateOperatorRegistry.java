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
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorRegistry;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.OperatorScopeException;

public class DateOperatorRegistry
implements OperatorRegistry
{
	
	private final static Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);
	
	public DateOperatorRegistry(OperatorScope scope) {
		try {
			apply(scope);
			logger.info("init DateOperatorRegistry");
		} catch (OperatorScopeException e) {
			logger.error("unable to init the DateOperatorRegistry", e);
		}
	}

	public void apply(OperatorScope scope) throws OperatorScopeException {
		DateOperatorDefinition months_between = new DateOperatorDefinition("MONTHS_BETWEEN",DateOperatorDefinition.DATE_MONTHS_BETWEEN,IDomain.NUMERIC, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(months_between);
		DateOperatorDefinition date_add = new DateOperatorDefinition("DATE_ADD",DateOperatorDefinition.DATE_ADD,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(date_add);
		DateOperatorDefinition date_sub = new DateOperatorDefinition("DATE_SUB",DateOperatorDefinition.DATE_SUB,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(date_sub);
		DateOperatorDefinition date_interval = new DateOperatorDefinition("DATE_INTERVAL",DateOperatorDefinition.DATE_INTERVAL,IDomain.NUMERIC, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(date_interval);
		DateOperatorDefinition current_date = new DateOperatorDefinition("CURRENT_DATE",DateOperatorDefinition.CURRENT_DATE,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(current_date);
		DateOperatorDefinition current_timestamp = new DateOperatorDefinition("CURRENT_TIMESTAMP",DateOperatorDefinition.CURRENT_TIMESTAMP,IDomain.TIMESTAMP, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(current_timestamp);
		//
		DateOperatorDefinition from_unixtime = new DateOperatorDefinition("FROM_EPOCH",DateOperatorDefinition.FROM_UNIXTIME,IDomain.TIMESTAMP, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(from_unixtime);
		DateOperatorDefinition to_unixtime = new DateOperatorDefinition("TO_EPOCH",DateOperatorDefinition.TO_UNIXTIME,IDomain.NUMERIC, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(to_unixtime);
		//
		AddMonthsOperatorDefinition addMonths = new AddMonthsOperatorDefinition("ADD_MONTHS",AddMonthsOperatorDefinition.ADD_MONTHS, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(addMonths);
		scope.registerLegacy(addMonths,IntrinsicOperators.ADD_MONTHS);
		//
		scope.registerExtension(new ExtractOperatorDefinition("DAY",ExtractOperatorDefinition.EXTRACT_DAY, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractOperatorDefinition("DAY_OF_WEEK",ExtractOperatorDefinition.EXTRACT_DAY_OF_WEEK, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractOperatorDefinition("DAY_OF_YEAR",ExtractOperatorDefinition.EXTRACT_DAY_OF_YEAR, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractOperatorDefinition("MONTH",ExtractOperatorDefinition.EXTRACT_MONTH, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractOperatorDefinition("YEAR",ExtractOperatorDefinition.EXTRACT_YEAR, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractOperatorDefinition("HOUR",ExtractOperatorDefinition.EXTRACT_HOUR, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractOperatorDefinition("MINUTE",ExtractOperatorDefinition.EXTRACT_MINUTE, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractOperatorDefinition("SECOND",ExtractOperatorDefinition.EXTRACT_SECOND, OperatorDefinition.DATE_TIME_TYPE));
		//
		scope.registerExtension(new DateTruncateOperatorDefinition("DATE_TRUNCATE",DateTruncateOperatorDefinition.DATE_TRUNCATE,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE));
		//
//See Ticket #1620
//		scope.registerExtension(new IntervalOperatorDefinition("INTERVAL_DAY",IntervalOperatorDefinition.INTERVAL_DAY, OperatorDefinition.DATE_TIME_TYPE));
//		scope.registerExtension(new IntervalOperatorDefinition("INTERVAL_MONTH",IntervalOperatorDefinition.INTERVAL_MONTH, OperatorDefinition.DATE_TIME_TYPE));
//		scope.registerExtension(new IntervalOperatorDefinition("INTERVAL_YEAR",IntervalOperatorDefinition.INTERVAL_YEAR, OperatorDefinition.DATE_TIME_TYPE));
//		scope.registerExtension(new IntervalOperatorDefinition("INTERVAL_HOUR",IntervalOperatorDefinition.INTERVAL_HOUR, OperatorDefinition.DATE_TIME_TYPE));
//		scope.registerExtension(new IntervalOperatorDefinition("INTERVAL_MINUTE",IntervalOperatorDefinition.INTERVAL_MINUTE, OperatorDefinition.DATE_TIME_TYPE));
//		scope.registerExtension(new IntervalOperatorDefinition("INTERVAL_SECOND",IntervalOperatorDefinition.INTERVAL_SECOND, OperatorDefinition.DATE_TIME_TYPE));

	}

}
