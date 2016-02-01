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

import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.set.SetDomain;

public class DomainStringConstant extends DomainString implements IConstantValueDomain<String> {
    
    public static final IDomain DOMAIN = new DomainStringConstant();

	private String value = null;

    protected DomainStringConstant() {
        this(STRING);
        this.value = null;
    }

	public DomainStringConstant(String value) {
		this(STRING);
		this.value = value;
	}

	public DomainStringConstant(IDomain parent) {
		super(parent);
	}

	public String getValue() {
		return value;
	}

    /**
     * because this domain is not a singleton we must change the equality test
     */
    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof DomainStringConstant) {
    		return true;
    	} else {
    		return false;
    	}
    }

    @Override
    public boolean isInstanceOf(IDomain domain) {
		if (domain==DomainConstant.DOMAIN || domain==DomainStringConstant.DOMAIN) {
			return true;
		} else if (domain instanceof AggregateDomain) {
			AggregateDomain pDomain = (AggregateDomain) domain;
			if (pDomain.getSubdomain() != null && (pDomain.getSubdomain()!=IDomain.INTRINSIC)) {
				return isInstanceOf(pDomain.getSubdomain());
			} else {
				return false;
			}
		} else if (domain instanceof SetDomain) {
			SetDomain pDomain = (SetDomain) domain;
			if (pDomain.getSubdomain() != null && (pDomain.getSubdomain()!=IDomain.INTRINSIC)) {
				return isInstanceOf(pDomain.getSubdomain());
			} else {
				return false;
			}
		} else {
			return super.isInstanceOf(domain);
		}
    }

}
