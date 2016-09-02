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
package com.squid.core.domain;

/**
 * This is the domain for a value that represent a Week of a year. That is, it's
 * a date where the day-of-week is always 1 (and meaningless). It is also a
 * Date, but this sub-type allows to perform some static type check.
 *
 * @author sergefantino
 *
 */
public class DomainDateWeekly extends DomainBase {

	/**
	 *
	 */
	public DomainDateWeekly() {
		this(IDomain.DATE);
	}

	/**
	 * @param parent
	 */
	protected DomainDateWeekly(IDomain parent) {
		super(parent);
		setName("Weekly");
	}

	@Override
	public IDomain getSingleton() {
		return IDomain.WEEKLY;
	}

}
