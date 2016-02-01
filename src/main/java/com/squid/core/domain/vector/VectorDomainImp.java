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

public class VectorDomainImp 
extends DomainBase 
implements VectorDomain
{
	
    /**
     * 
     */
    public VectorDomainImp() {
        this(IDomain.META);
    }

    /**
     * @param parent
     */
    protected VectorDomainImp(IDomain parent) {
        super(parent);
        setName("Vector");
    }
    
    @Override
    public IDomain getSubdomain() {
    	return IDomain.UNKNOWN;
    }
    
    @Override
    public IDomain createMetaDomain(IDomain subdomain) {
    	return new ProxyVectorDomain(subdomain,-1);
    }

	@Override
	public IDomain getMetadomain() {
		return this;
	}
	
	@Override
	public void setSize(int size) {
		// TODO Auto-generated method stub
		
	}

}
