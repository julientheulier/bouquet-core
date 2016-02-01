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
package com.squid.core.domain.operators.special;

import java.util.List;

import com.squid.core.domain.DomainMetaDomain;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ArithmeticOperatorDefintion;
import com.squid.core.domain.operators.ExtendedType;

/**
 * Support Divide operator:
 *  * special rule to compute the result type: we prefer to return a floating result
 *  * take care of int/int situation
 *  
 * @author Serge Fantino
 *
 */
public class DivideOperatorDefinition 
extends ArithmeticOperatorDefintion {

	public DivideOperatorDefinition(String name, int id, String symbol,
			IDomain domain) {
		super(name,id,symbol,domain);
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()!=2) {
			return IDomain.UNKNOWN;
		}
		IDomain arg0 = imageDomains.get(0);
		IDomain arg1 = imageDomains.get(1);
		if (arg0.isInstanceOf(IDomain.NUMERIC) && arg1.isInstanceOf(IDomain.NUMERIC)) {
			if (arg0.isInstanceOf(DomainMetaDomain.META)) {
				return ((IDomainMetaDomain)arg0).createMetaDomain(IDomain.CONTINUOUS);
			}
			if (arg1.isInstanceOf(DomainMetaDomain.META)) {
				return ((IDomainMetaDomain)arg1).createMetaDomain(IDomain.CONTINUOUS);
			}
			return IDomain.CONTINUOUS;
		}
		// else
		return IDomain.UNKNOWN;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		ExtendedType result = null;
		if (types.length!=2) {
			return ExtendedType.UNDEFINED;
		} else {
			if (types[0].isInteger()) {
				if (types[1].isInteger()) {
					result = ExtendedType.FLOAT;
				} else {
					result = types[1];
				}
			} else {
				result = types[0];
			}
		}
		//
		IDomainMetaDomain meta = null;
		if (types[0].getDomain().isInstanceOf(IDomain.META)) {
			meta = (IDomainMetaDomain)types[0].getDomain();
		} else if (types[1].getDomain().isInstanceOf(IDomain.META)) {
			meta = (IDomainMetaDomain)types[1].getDomain();
		}
        if (meta!=null) {
        	if (meta.equals(result.getDomain())) {
        		return result;
        	} else {
            	IDomain proxy = meta.createMetaDomain(result.getDomain());
            	result = new ExtendedType(proxy, result);
        	}
        	return result;
        } else {
        	return result;
        }
	}

}
