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
package com.squid.core.domain.extensions.registry;

import com.squid.core.domain.extensions.date.AddMonthsOperatorDefinition;
import com.squid.core.domain.extensions.date.DateTruncateOperatorDefinition;
import com.squid.core.domain.extensions.date.DateTruncateShortcutsOperatorDefinition;
import com.squid.core.domain.extensions.date.extract.*;
import com.squid.core.domain.extensions.date.operator.*;
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
		DateOperatorDefinition months_between = new DateMonthsBetweenOperatorDefinition("MONTHS_BETWEEN",IDomain.NUMERIC, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(months_between);
		DateOperatorDefinition date_add = new DateAddOperatorDefinition("DATE_ADD",IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(date_add);
		DateOperatorDefinition date_sub = new DateSubOperatorDefinition("DATE_SUB",IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(date_sub);
		DateOperatorDefinition date_interval = new DateIntervalOperatorDefinition("DATE_INTERVAL",IDomain.NUMERIC, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(date_interval);
		DateOperatorDefinition current_date = new DateCurrentDateOperatorDefinition("CURRENT_DATE",IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(current_date);
		DateOperatorDefinition current_timestamp = new DateCurrentTimestampOperatorDefinition("CURRENT_TIMESTAMP",IDomain.TIMESTAMP, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(current_timestamp);
		//
		DateOperatorDefinition from_unixtime = new DateFromEpochOperatorDefinition("FROM_EPOCH",IDomain.TIMESTAMP, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(from_unixtime);
		DateOperatorDefinition to_unixtime = new DateToEpochOperatorDefinition("TO_EPOCH",IDomain.NUMERIC, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(to_unixtime);
		//
		AddMonthsOperatorDefinition addMonths = new AddMonthsOperatorDefinition("ADD_MONTHS",AddMonthsOperatorDefinition.ADD_MONTHS, OperatorDefinition.DATE_TIME_TYPE);
		scope.registerExtension(addMonths);
		scope.registerLegacy(addMonths,IntrinsicOperators.ADD_MONTHS);
		//
		scope.registerExtension(new ExtractDayOperatorDefinition("DAY", OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractDayOfWeekOperatorDefinition("DAY_OF_WEEK", OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractDayOfYearOperatorDefinition("DAY_OF_YEAR", OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractMonthOperatorDefinition("MONTH", OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractYearOperatorDefinition("YEAR", OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractHourOperatorDefinition("HOUR", OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractMinuteOperatorDefinition("MINUTE", OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new ExtractSecondOperatorDefinition("SECOND", OperatorDefinition.DATE_TIME_TYPE));
		//
		scope.registerExtension(new DateTruncateOperatorDefinition("DATE_TRUNCATE",DateTruncateOperatorDefinition.DATE_TRUNCATE,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE));
		// For this ones, I dont really need to separate.
		scope.registerExtension(new DateTruncateShortcutsOperatorDefinition("HOURLY",DateTruncateShortcutsOperatorDefinition.HOURLY_ID,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new DateTruncateShortcutsOperatorDefinition("DAILY",DateTruncateShortcutsOperatorDefinition.DAILY_ID,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new DateTruncateShortcutsOperatorDefinition("WEEKLY",DateTruncateShortcutsOperatorDefinition.WEEKLY_ID,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new DateTruncateShortcutsOperatorDefinition("MONTHLY",DateTruncateShortcutsOperatorDefinition.MONTHLY_ID,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new DateTruncateShortcutsOperatorDefinition("YEARLY",DateTruncateShortcutsOperatorDefinition.YEARLY_ID,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE));
		scope.registerExtension(new DateTruncateShortcutsOperatorDefinition("QUARTERLY",DateTruncateShortcutsOperatorDefinition.QUARTERLY_ID,IDomain.DATE, OperatorDefinition.DATE_TIME_TYPE));
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
