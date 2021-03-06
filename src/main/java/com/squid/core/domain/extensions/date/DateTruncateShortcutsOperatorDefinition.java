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

import com.squid.core.domain.DomainDate;
import com.squid.core.domain.DomainTimestamp;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * This OperatorDefinition supports the following shortcuts: DAILY(), WEEKLY(),
 * MONTHLY(), YEARLY(), HOURLY().
 *
 * For example YEARLY(date) is equivalent to DATE_TRUNCATE(date,"year")
 *
 * Note that the rendering is supported by DateTruncateRenderer directly so we
 * can just extend the base class and don't have to edit every database specific
 * version
 *
 * @author sfantino
 */
public class DateTruncateShortcutsOperatorDefinition extends OperatorDefinition {

	// shortcuts
	public static final String SHORTCUT_BASE = DateTruncateOperatorDefinition.DATE_TRUNCATE_BASE + "shortcut.";
	public static final String HOURLY_ID = SHORTCUT_BASE + DateTruncateOperatorDefinition.HOUR;
	public static final String DAILY_ID = SHORTCUT_BASE + DateTruncateOperatorDefinition.DAY;
	public static final String WEEKLY_ID = SHORTCUT_BASE + DateTruncateOperatorDefinition.WEEK;
	public static final String MONTHLY_ID = SHORTCUT_BASE + DateTruncateOperatorDefinition.MONTH;
	public static final String QUARTERLY_ID = SHORTCUT_BASE + DateTruncateOperatorDefinition.QUARTER;
	public static final String YEARLY_ID = SHORTCUT_BASE + DateTruncateOperatorDefinition.YEAR;

	public static final String HOURLY = "HOURLY";
	public static final String DAILY = "DAILY";
	public static final String WEEKLY = "WEEKLY";
	public static final String MONTHLY = "MONTHLY";
	public static final String QUARTERLY = "QUARTERLY";
	public static final String YEARLY = "YEARLY";

	private String hint = "";

	public DateTruncateShortcutsOperatorDefinition(String name, String ID, IDomain domain) {
		super(name, ID, PREFIX_POSITION, name, domain);
		hint = name + "( date or timestamp)";
        this.setCategoryType(OperatorDefinition.DATE_TIME_TYPE);

	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("Shortcuts (hourly, daily, weekly, monthly, quaterly, yearly) to truncate a date");
		hint.add("Shortcuts (hourly, daily, weekly, monthly, quaterly, yearly) to truncate a timestamp");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		IDomain date = new DomainDate();
		IDomain timestamp = new DomainTimestamp();
		type.add(date);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(timestamp);
		poly.add(type);
		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size() != 1) {
			return new OperatorDiagnostic("Invalid number of parameters", hint);
		} else if (!(imageDomains.get(0).isInstanceOf(IDomain.DATE)
				|| imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP))) {
			return new OperatorDiagnostic("Invalid type for parameter #1, must be a date or a timestamp but it is a "
					+ imageDomains.get(0).getName(), hint);
		} else if (imageDomains.get(0).isInstanceOf(IDomain.ANY)) {
			return new OperatorDiagnostic("Invalid type for parameter #1, must be a date or a timestamp but it is a "
					+ imageDomains.get(0).getName(), hint);
		}
		return OperatorDiagnostic.IS_VALID;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		ExtendedType dateType = types[0];
		return fixExtendedTypeDomain(dateType, types);
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty()) {
			return IDomain.UNKNOWN;
		}
		IDomain argument0 = imageDomains.get(0);
		boolean is_aggregate = argument0.isInstanceOf(AggregateDomain.DOMAIN);
		IDomain domain = computeImageDomain(getExtendedID(), argument0);
		if (is_aggregate) {
			// compose with Aggregate
			domain = AggregateDomain.MANAGER.createMetaDomain(domain);
		}
		//
		return domain;
	}

	private IDomain computeImageDomain(String mode, IDomain argument) {
		if (mode.equals(YEARLY_ID) || argument.isInstanceOf(IDomain.YEARLY)) {
			if (argument.isInstanceOf(IDomain.YEARLY)) {
				return argument;
			} else {
				return IDomain.YEARLY;
			}
		}
		if (mode.equals(QUARTERLY_ID) || argument.isInstanceOf(IDomain.QUARTERLY)) {
			if (argument.isInstanceOf(IDomain.QUARTERLY)) {
				return argument;
			} else {
				return IDomain.QUARTERLY;
			}
		}
		if (mode.equals(MONTHLY_ID) || argument.isInstanceOf(IDomain.MONTHLY)) {
			if (argument.isInstanceOf(IDomain.MONTHLY)) {
				return argument;
			} else {
				return IDomain.MONTHLY;
			}
		}
		if (mode.equals(WEEKLY_ID) || argument.isInstanceOf(IDomain.WEEKLY)) {
			if (argument.isInstanceOf(IDomain.WEEKLY)) {
				return argument;
			} else {
				return IDomain.WEEKLY;
			}
		}
		if (mode.equals(DAILY_ID) || argument.isInstanceOf(IDomain.DATE)) {
			return IDomain.DATE;
		}
		// else
		return argument;
	}

}
