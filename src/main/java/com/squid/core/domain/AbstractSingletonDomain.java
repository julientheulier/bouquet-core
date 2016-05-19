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

import java.util.Collections;
import java.util.List;

/**
 * the minimal logical implementation; no storage involved
 * @author serge fantino
 *
 */
public abstract class AbstractSingletonDomain 
extends BaseDomainManager
implements ISingletonDomain
{

	public IDomain getSingleton() {
		return this;
	}
    
	/*
    public Image getIcon() {
        IDomain parent = getParentDomain();
        if (parent!=null) {
            return parent.getIcon();
        } else {
            return null;
        }
    }
    */

    public List<IDomain> flatten() {
        return Collections.singletonList((IDomain)this);
    }

    public boolean isInstanceOf(IDomain domain) {
        if (this.equals(domain)) {
            return true;
        }
        else if (this.getParentDomain()!=null&&this.getParentDomain().isInstanceOf(domain)) {
            return true;
        }
        else return false;
    }
    
    @Override
    public IDomain compose(IDomain anotherDomain) {
    	return IDomain.UNKNOWN;// never return NULL here!!! we should not end here except if there is some UNKNOWN domain in the composition
    }

    @Override
    public Object getAdapter(Class<?> adapter) {
        if (adapter.equals(ISingletonDomain.class)) {
            return getSingleton();
        } else return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (obj instanceof ISingletonDomain) {
            return this.getSingleton()==((ISingletonDomain)obj).getSingleton();
        }
        //
        return false;
    }

    @Override
    public String toString() {
    	return getName();
    }

    @Override
    public IDomainManager getDomainManager() {
    	return this;
    }

    @Override
    public String getContentAssistLabel(){
        return getName()+" "+getName().toLowerCase().charAt(0);
    }

    @Override
    public String getContentAssistProposal(){
        return String.valueOf(getName().toLowerCase().charAt(0));
    }

}
