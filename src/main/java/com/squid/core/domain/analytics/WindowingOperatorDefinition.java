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

import java.sql.Types;
import java.util.ArrayList;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;

public class WindowingOperatorDefinition 
extends OperatorDefinition
{

	public WindowingOperatorDefinition(String name, String ID) {
		super(name,ID,OperatorDefinition.PREFIX_POSITION,name,WindowingDomain.DOMAIN);
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}
	
	/**
	 * The windowing operator family does NOT support extendedType calculation
	 */
	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		ArrayList<IDomain> imageDomains = new ArrayList<IDomain>();
		for (ExtendedType type : types) {
			imageDomains.add(type.getDomain());
		}
		IDomain domain = computeImageDomain(imageDomains);
		return new ExtendedType(domain,Types.OTHER,0,0);
	}
	
}
