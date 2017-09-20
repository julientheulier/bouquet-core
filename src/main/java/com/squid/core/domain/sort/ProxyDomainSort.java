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
package com.squid.core.domain.sort;


import com.squid.core.domain.DomainBase;
import com.squid.core.domain.IDomain;

public class ProxyDomainSort
extends DomainBase
implements DomainSort
{

	private IDomain subdomain = IDomain.UNKNOWN;
	private SortDirection direction;
	private NullsPosition nullsPosition;

	public ProxyDomainSort() {
		super(DomainSort.DOMAIN);
	}

	protected ProxyDomainSort(IDomain subdomain) {
		this();
		this.subdomain = subdomain;
	}

	protected ProxyDomainSort(IDomain subdomain, SortDirection direction, NullsPosition nullsPosition) {
		this();
		this.subdomain = subdomain;
		this.direction = direction;
		this.nullsPosition = nullsPosition;
	}

	@Override
	public String getName() {
		if (subdomain!=null) {
			return direction.name()+"{"+subdomain.getName()+"}" + (nullsPosition != NullsPosition.UNDEFINED?" "+nullsPosition.toString():"");
		} else {
			return direction.name()+"{}" + (nullsPosition != NullsPosition.UNDEFINED?" "+nullsPosition.toString():"");
		}
	}

	@Override
	public IDomain getSubdomain() {
		return subdomain;
	}

	@Override
	public SortDirection getDirection() {
		return direction;
	}

	@Override
	public void setDirection(SortDirection direction) {
		this.direction = direction;
	}

	@Override
	public boolean isInstanceOf(IDomain domain) {
		if (domain==DomainSort.DOMAIN) {
			return true;
		}
		if (domain instanceof ProxyDomainSort) {
			ProxyDomainSort v = (ProxyDomainSort)domain;
			if (this.subdomain!=null && v.subdomain!=null && subdomain.isInstanceOf(v.subdomain)) {
				return true;
			}
		}
		if (this.subdomain!=null && this.subdomain.isInstanceOf(domain)) {
			return true;
		} else {
			return super.isInstanceOf(domain);
		}
	}

	@Override
	public IDomain compose(IDomain anotherDomain) {
		return null;// unable to compose
	}

	@Override
	public Object getAdapter(Class<?> adapter) {
		Object ancestorSays = super.getAdapter(adapter);
		if (ancestorSays!=null) return ancestorSays;
		if (adapter.equals(DomainSort.class)) {
			return this;// this one may be called by a containing metaModel
		}
		if (getSubdomain()!=null) {
			return getSubdomain().getAdapter(adapter);
		}
		// don't know
		return null;
	}

	@Override
	public IDomain createMetaDomain(IDomain subdomain) {
		return new ProxyDomainSort(subdomain);
	}

	@Override
	public IDomain createMetaDomain(IDomain baseDomain, SortDirection direction, NullsPosition nullsPosition) {
		return new ProxyDomainSort(subdomain, direction, nullsPosition);
	}

	@Override
	public IDomain getMetadomain() {
		return DomainSort.DOMAIN;
	}

	@Override
	public NullsPosition getNullsPosition() {
		return nullsPosition;
	}

	@Override
	public void setNullsPosition(NullsPosition nullsPosition) {
		this.nullsPosition = nullsPosition;
	}

}
