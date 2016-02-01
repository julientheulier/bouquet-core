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

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainManager;

public class ProxyAnalyticDomainManager 
implements IDomainManager
{
	
	private Hashtable<IDomain, ProxyAnalyticDomain> domains = new Hashtable<IDomain, ProxyAnalyticDomain>();

	public ProxyAnalyticDomainManager() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Collection<IDomain> getDomainScope() {
		return Collections.EMPTY_LIST;
	}
	
	public IDomain createMetaDomain(IDomain subdomain) {
		if (subdomain==null) return AnalyticDomain.DOMAIN;
		if (subdomain.isInstanceOf(AnalyticDomain.DOMAIN)) {
			return subdomain;
		}
		ProxyAnalyticDomain result = domains.get(subdomain);
		if (result==null) {
			result = new ProxyAnalyticDomain(subdomain);
			domains.put(subdomain,result);
		}
		return result;
	}

}
