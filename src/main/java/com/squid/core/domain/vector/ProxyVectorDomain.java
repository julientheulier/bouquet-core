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
package com.squid.core.domain.vector;


import com.squid.core.domain.DomainBase;
import com.squid.core.domain.IDomain;

public class ProxyVectorDomain 
extends DomainBase
implements VectorDomain
{
	
	private IDomain subdomain = IDomain.UNKNOWN;
	private int size = 0;

	public ProxyVectorDomain() {
		super(VectorDomain.DOMAIN);
	}
	
	protected ProxyVectorDomain(IDomain subdomain, int size) {
		this();
		this.subdomain = subdomain;
		this.size = size;
	}

	@Override
	public String getName() {
		if (subdomain!=null) {
			return "VECTOR{"+subdomain.getName()+"}";
		} else {
			return "VECTOR{}";
		}
	}

	@Override
	public IDomain getSubdomain() {
		return subdomain;
	}
	
	public int getSize() {
		return size;
	}
	
	@Override
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public boolean isInstanceOf(IDomain domain) {
		// SetDomain(E) is instanceof E
		if (domain instanceof ProxyVectorDomain) {
			ProxyVectorDomain v = (ProxyVectorDomain)domain;
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
		if (ancestorSays==null && getSubdomain()!=null) {
			return getSubdomain().getAdapter(adapter);
		} else
			return ancestorSays;
	}
    
    @Override
    public IDomain createMetaDomain(IDomain subdomain) {
    	return new ProxyVectorDomain(subdomain,-1);
    }

	@Override
	public IDomain getMetadomain() {
		return VectorDomain.DOMAIN;
	}

}
