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
package com.squid.core.domain.analytics;

import com.squid.core.domain.DomainBase;
import com.squid.core.domain.IDomain;

public class WindowingDomainImp 
extends DomainBase 
implements WindowingDomain
{
	
	private WindowingStaticExpression expression = WindowingStaticExpression.UNDEFINED;

	public static WindowingDomain createRowsDomain(WindowingDomain w) {
		WindowingStaticExpression e = w.getExpression();
		WindowingStaticExpression r = new WindowingStaticExpression(WindowingType.ROWS);
		r.setPreceding(e);
		return new WindowingDomainImp(r);
	}

	public static WindowingDomain createRowsDomain(WindowingDomain p, WindowingDomain f) {
		WindowingStaticExpression pe = p.getExpression();
		WindowingStaticExpression fe = f.getExpression();
		WindowingStaticExpression r = new WindowingStaticExpression(WindowingType.ROWS);
		r.setPreceding(pe);
		r.setFollowing(fe);
		return new WindowingDomainImp(r);
	}

	public static WindowingDomain createPrecedingDomain(int rowCount) {
		WindowingStaticExpression r = new WindowingStaticExpression(WindowingType.PRECEDING);
		r.setRowCount(rowCount);
		return new WindowingDomainImp(r);
	}

	public static WindowingDomain createFollowingDomain(int rowCount) {
		WindowingStaticExpression r = new WindowingStaticExpression(WindowingType.FOLLOWING);
		r.setRowCount(rowCount);
		return new WindowingDomainImp(r);
	}

	public static WindowingDomain createUnboundedDomain() {
		WindowingStaticExpression r = new WindowingStaticExpression(WindowingType.UNBOUNDED);
		return new WindowingDomainImp(r);
	}

	public static WindowingDomain createCurrentDomain() {
		WindowingStaticExpression r = new WindowingStaticExpression(WindowingType.CURRENT);
		return new WindowingDomainImp(r);
	}
	
	public WindowingDomainImp() {
		super(WindowingDomain.DOMAIN);
		setName("WindowingDomain");
		this.expression = WindowingStaticExpression.UNDEFINED;
	}
	
	protected WindowingDomainImp(WindowingStaticExpression expression) {
		super(WindowingDomain.DOMAIN);
		setName("WindowingDomain");
		this.expression = expression;
	}

    public IDomain getSingleton() {
        return DOMAIN;
    }

	@Override
	public WindowingStaticExpression getExpression() {
		return expression;
	}

}
