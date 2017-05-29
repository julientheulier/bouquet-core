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

public class ProxyAnalyticDomain 
extends DomainBase
implements AnalyticDomain
{
	
	private IDomain subdomain = IDomain.UNKNOWN;

	public ProxyAnalyticDomain() {
		super(AnalyticDomain.DOMAIN);
	}
	
	protected ProxyAnalyticDomain(IDomain subdomain) {
		this();
		this.subdomain = subdomain;
	}
	
	@Override
	public String getName() {
		if (subdomain!=null) {
			return "||"+subdomain.getName()+"||";
		} else {
			return "||???||";
		}
	}

	@Override
	public IDomain getSubdomain() {
		return subdomain;
	}
	
	@Override
	public IDomain getSingleton() {
		return AnalyticDomain.MANAGER.createMetaDomain(subdomain);
	}
	
	@Override
	public boolean isInstanceOf(IDomain domain) {
		// SetDomain(E) is instanceof E
		if (this.subdomain!=null && this.subdomain.isInstanceOf(domain)) {
			return true;
		} else 
			return super.isInstanceOf(domain);
	}
	
	@Override
	public IDomain compose(IDomain anotherDomain) {
		// if anotherDomain is a set, it is the result, else we return SET(anotherDomain)
		if (anotherDomain.isInstanceOf(AnalyticDomain.DOMAIN)) {
			return anotherDomain;
		} else {
			return AnalyticDomain.MANAGER.createMetaDomain(anotherDomain);
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
    	return AnalyticDomain.MANAGER.createMetaDomain(baseDomain);
    }

	@Override
	public IDomain getMetadomain() {
		return AnalyticDomain.DOMAIN;
	}

}
