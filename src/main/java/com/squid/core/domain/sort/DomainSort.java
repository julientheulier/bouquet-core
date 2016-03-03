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
package com.squid.core.domain.sort;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;

/**
 * Sort domain is a meta-domain that carries a sort direction. 
 * This domain is created by using SortOperators for instance.
 * If an ExpressionAST has this type it can be use to sort/orderBy
 * @author sergefantino
 *
 */
public interface DomainSort
extends IDomain, IDomainMetaDomain
{
	
	public enum SortDirection {
		ASC, DESC
	}
	
	public static final DomainSort DOMAIN = new DomainSortImp();
	
	public SortDirection getDirection();
	
	public void setDirection(SortDirection direction);
	
	public IDomain getSubdomain();

	/**
	 * custom constructor
	 */
	public IDomain createMetaDomain(IDomain baseDomain, SortDirection direction);

}
