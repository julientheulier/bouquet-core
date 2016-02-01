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
package com.squid.core.domain.set;

import com.squid.core.domain.DomainBase;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;

public class ProxySetDomain 
extends DomainBase
implements SetDomain
{
	
	private IDomain subdomain = IDomain.UNKNOWN;

	public ProxySetDomain() {
		super(SetDomain.SET);
	}
	
	protected ProxySetDomain(IDomain subdomain) {
		this();
		this.subdomain = subdomain;
	}
	
	@Override
	public String getName() {
		if (subdomain!=null) {
			return "{"+subdomain.getName()+"}";
		} else {
			return "{}";
		}
	}

	@Override
	public IDomain getSubdomain() {
		return subdomain;
	}
	
	@Override
	public IDomain getSingleton() {
		return SetDomain.MANAGER.getDomainSet(subdomain);
	}
	
	@Override
	public boolean isInstanceOf(IDomain domain) {
		// SetDomain(E) is instanceof E
		if (domain.equals(SetDomain.SET)) {
			return true;
		} else
		if (this.subdomain!=null && domain instanceof SetDomain) {
			IDomain sub = ((SetDomain)domain).getSubdomain();
			return this.subdomain.isInstanceOf(sub);
		} else
		if (this.subdomain!=null && this.subdomain.isInstanceOf(domain)) {
			return true;
		} else 
			return super.isInstanceOf(domain);
	}
	
	@Override
	public IDomain compose(IDomain anotherDomain) {
		// if anotherDomain is a set-domain, it is the result, 
		if (anotherDomain.isInstanceOf(SetDomain.SET)) {
			return anotherDomain;
		}
		// if anotherDomain is a aggr-domain, it is the result, 
		if (anotherDomain.isInstanceOf(AggregateDomain.AGGREGATE)) {
			return anotherDomain;
		} else { // else we return SET(anotherDomain)
			return SetDomain.MANAGER.getDomainSet(anotherDomain);
		}
	}
	
	@Override
	public Object getAdapter(Class<?> adapter) {
		Object ancestorSays = super.getAdapter(adapter);
		if (ancestorSays==null && getSubdomain()!=null) {
			return getSubdomain().getAdapter(adapter);
		} else
			return ancestorSays;
	}
    
    @Override
    public IDomain createMetaDomain(IDomain baseDomain) {
    	return SetDomain.MANAGER.getDomainSet(baseDomain);
    }

	@Override
	public IDomain getMetadomain() {
		return SetDomain.SET;
	}

}
