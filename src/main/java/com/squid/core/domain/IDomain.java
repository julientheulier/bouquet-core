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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.sql.render.SQLSkin;

/**
 * The base interface for Domain definition
 *
 * @author serge fantino
 *
 */
public interface IDomain {

	public static final IDomain UNKNOWN = new DomainUnknown();
	public static final IDomain INTRINSIC = new DomainIntrinsic();
	public static final IDomain STRING = new DomainString();
	public static final IDomain INT = new DomainInteger();
	// NUMERIC is Double and INT is Integer
	public static final IDomain NUMERIC = new DomainNumeric();
	public static final IDomain TEMPORAL = new DomainTemporal();
	public static final IDomain DATE = new DomainDate();
	public static final IDomain WEEKLY = new DomainDateWeekly();
	public static final IDomain MONTHLY = new DomainDateMonthly();
	public static final IDomain QUARTERLY = new DomainDateQuarterly();
	public static final IDomain YEARLY = new DomainDateYearly();
	public static final IDomain TIME = new DomainTime();
	public static final IDomain OBJECT = new DomainObject();
	public static final IDomain NULL = new DomainNull();
	public static final IDomain ANY = new DomainAny();
	public static final IDomain TIMESTAMP = new DomainTimestamp();
	public static final IDomain INTERVAL = new DomainInterval();

	public static final IDomain CONDITIONAL = new DomainConditional();
	public static final IDomain BOOLEAN = new DomainBoolean();// ticket:3133
	public static final IDomain CONTINUOUS = new DomainContinuous();

	public static final IDomain META = new DomainMetaDomain();
	public static final IDomain AGGREGATE = AggregateDomain.DOMAIN;// must be
																	// declared
																	// after
																	// META...

	static final Collection<IDomain> BASE_SCOPE = Collections
			.unmodifiableCollection(Arrays.asList(new IDomain[] { STRING, NUMERIC, DATE, CONDITIONAL }));

	public String getName();

	public void setName(String name);

	public IDomain getParentDomain();

	public void setParentDomain(IDomain parent);

	// public Image getIcon();//deprecated, avoid dependency to SWT in model
	// code

	public boolean isInstanceOf(IDomain domain);

	/**
	 * flatten domain as a list of simple domain wrapped within an
	 * complex-domain
	 *
	 * @return a list composed of only simple IDomain objects
	 */
	public List<IDomain> flatten();

	/**
	 * get the domain manager
	 *
	 * @return
	 */
	public IDomainManager getDomainManager();

	/**
	 * compose this domain with another domain
	 *
	 * @param domain
	 * @return null if the two domain cannot be composed, or the correct domain
	 */
	public IDomain compose(IDomain anotherDomain);

	public ExtendedType computeType(SQLSkin skin);

	/**
	 * Returns an object which is an instance of the given class associated with
	 * this object. Returns <code>null</code> if no such object can be found.
	 *
	 * @param adapter
	 *            the adapter class to look up
	 * @return a object castable to the given class, or <code>null</code> if
	 *         this object does not have an adapter for the given class
	 */
	public Object getAdapter(Class<?> adapter);

	/**
	 * Returns the label seen by the user for completion. Correspond to the list
	 * seen in eclipse when using autocompletion this is the text
	 * "function(String s, Int i)"
	 *
	 * @return
	 */
	public String getContentAssistLabel();

	public void setContentAssistLabel(String label);

	/**
	 * Returns the proposal seen by the user for completion. Correspond to the
	 * actual text replacing the code Most of the time, one letter character,
	 * for example "i" for integer type
	 *
	 * @return
	 */
	public String getContentAssistProposal();

	/**
	 * Returns the proposal seen by the user for completion. Correspond to the
	 * actual text replacing the code Most of the time, one letter character,
	 * for example "i" for integer type
	 *
	 * @parameter position correspond to the position inside the template (1 for
	 *            ${1:something})
	 * @return
	 */
	public String getContentAssistProposal(int position);

	/**
	 * Returns the proposal seen by the user for completion. Correspond to the
	 * actual text replacing the code Most of the time, one letter character,
	 * for example "i" for integer type
	 *
	 * @parameter position correspond to the position inside the template 1 for
	 *            ${1:name}
	 * @parameter skin will be used to compute the type of the parameter example
	 *            {$1:Numerical:name}
	 * @return
	 */
	public String getContentAssistProposal(int position, SQLSkin skin);

	public void setContentAssistProposal(String name);

	public void setContentAssistProposal(String name, int position);

}
