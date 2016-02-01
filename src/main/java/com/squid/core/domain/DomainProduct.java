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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DomainProduct 
extends DomainBase 
implements IComplexDomain
{
    
    private List<IDomain> m_domains = new ArrayList<IDomain>();
    
    private DomainProduct() {
        super(IDomain.NULL);
    }

    private DomainProduct(IDomain domain1, IDomain domain2) {
        this();
        add(domain1);
        add(domain2);
    }
    
    private DomainProduct(Collection<IDomain> domains) {
		addAll(domains);
	}
    
    public static IDomain createDomain(IDomain domain1, IDomain domain2) {
    	DomainProduct domain = new DomainProduct(domain1, domain2);
    	if (domain.size()==0) {
    		return IDomain.NULL;
    	} else if (domain.size()==1) {
    		return domain.get(0);
    	} else {
    		return domain;
    	}
    }
    
    public static IDomain createDomain(Collection<IDomain> domains) {
    	DomainProduct domain = new DomainProduct(domains);
    	if (domain.size()==0) {
    		return IDomain.NULL;
    	} else if (domain.size()==1) {
    		return domain.get(0);
    	} else {
    		return domain;
    	}
    }

	public void add(IDomain domain) {
		if (domain instanceof IComplexDomain) {
			addAll((IComplexDomain)domain);
		}
		else if (!m_domains.contains(domain)) {
	        m_domains.add(domain);
		}
    }
    
    public void addAll(IComplexDomain domain) {
        for (int i=0;i<domain.size();i++) {
            add(domain.get(i));
        }
    }
    
    public void addAll(Collection<IDomain> domains) {
        for (IDomain domain : domains) {
        	add(domain);
        }
    }
    
    @Override
    public IDomain compose(IDomain anotherDomain) {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    public int size() {
        return m_domains.size();
    }
    
    public IDomain get(int index) {
        return (IDomain) m_domains.get(index);
    }
    
    /**
     * recursively flatten the product
     */
    public List<IDomain> flatten() {
    	ArrayList<IDomain> flat = new ArrayList<IDomain>();
        Iterator<IDomain> iter = m_domains.iterator();
        while (iter.hasNext()) {
            IDomain domain = iter.next();
            flat.addAll(domain.flatten());
            /*
            if (domain instanceof IComplexDomain) {
                flat.addAll(domain.flatten());
            } else {
                flat.add(domain);
            }
            */
        }
        return flat;
    }


    /* (non-Javadoc)
     * @see com.squid.ldm.model.expressions.ExpressionType#getName()
     */
    public String getName() {
        String res = "[";
        Iterator<IDomain> iter = m_domains.iterator();
        boolean first = true;
        while (iter.hasNext()) {
            if (first) first = false; else res += " x ";
            IDomain type = iter.next();
            res += type.getName();
        }
        res += "]";
        return res;
    }

    public IDomain getSingleton() {
        return this;
    }

}
