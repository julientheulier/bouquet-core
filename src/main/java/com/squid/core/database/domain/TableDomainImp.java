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
package com.squid.core.database.domain;

import com.squid.core.database.model.Table;
import com.squid.core.domain.DomainObject;
import com.squid.core.domain.IDomain;

/**
 * The generic TableDomain implementation, using a singleton pattern; this is the parent of all specific TableDomain
 * @author sfantino
 *
 */
public class TableDomainImp 
extends DomainObject 
implements TableDomain
{

    /**
     * 
     */
    public TableDomainImp() {
        this(IDomain.OBJECT);
    }

    /**
     * @param parent
     */
    protected TableDomainImp(IDomain parent) {
        super(parent);
        setName("Table");
    }

    /*
    public Image getIcon() {
        return getParentDomain().getIcon();
    }
    */

    public IDomain getSingleton() {
        return TableDomainImp.DOMAIN;
    }
    
    @Override
    public Table getTable() {
    	return null;
    }

}
