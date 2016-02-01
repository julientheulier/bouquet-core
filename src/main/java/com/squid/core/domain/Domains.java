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

import java.util.List;

/**
 * utility class
 * @author serge fantino
 *
 */
public class Domains {
    
    public static IDomain checkNotNull(IDomain domain) {
        return domain!=null?domain:IDomain.NULL;
    }

    public static boolean isWellDefined(IDomain domain) {
        if (domain.equals(IDomain.NULL)) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * compute the domain which generalise all the domains provided - and for which there is no less generic domain complying. 
     * @param domains
     * @return the less generic domain - UNKNOWN means that the domains have nothing to share
     */
    public static IDomain computeLessGenericDomain(List<IDomain> domains) {
        if (domains.size()==0) return IDomain.UNKNOWN;
        IDomain lessGeneric = domains.get(0);
        for (int i=1;i<domains.size();i++) {
            lessGeneric = computeLessGenericDomain(lessGeneric,domains.get(i));
        }
        return lessGeneric;
    }
    
    public static IDomain combineDomains(IDomain first, IDomain second) {
        if (first.isInstanceOf(DomainStringConstant.DOMAIN) 
                && second.isInstanceOf(DomainStringConstant.DOMAIN)) {
            return DomainStringConstant.DOMAIN;
        } else if (first.isInstanceOf(IDomain.META)) {
            return first;
        } else {
            return second;
        }
    }
    
    public static IDomain computeLessGenericDomain(IDomain first, IDomain second) {
        if (first.isInstanceOf(second)) {
            return combineDomains(first, second);
        }
        else if (second.isInstanceOf(first)) {
            return combineDomains(second, first);
        }
        else
        {
        	if (first.isInstanceOf(IDomain.STRING)) {
        		return first;
        	}
        	if (first.isInstanceOf(IDomain.NUMERIC)) {
        		if (second.isInstanceOf(IDomain.STRING)) {
        			return second;
        		}
        		if (second.isInstanceOf(IDomain.TEMPORAL)) {
        			return first;
        		}
        		if (second.isInstanceOf(IDomain.NUMERIC)) {
        			return IDomain.NUMERIC;
        		}
        	}
    		return IDomain.UNKNOWN;
         }
    }

}
