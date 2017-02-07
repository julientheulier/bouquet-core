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
package com.squid.core.domain.operators;

import java.util.List;

import com.squid.core.domain.Domains;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.associative.AssociativeDomainInformation;

public class ArithmeticOperatorDefinition extends AlgebraicOperatorDefinition {

	protected ExtendedType extendedType = null;
	public ArithmeticOperatorDefinition(String name, String extendedID, int position, String symbol, IDomain domain, int categoryType) {
		super(name, extendedID, position, symbol, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	public ArithmeticOperatorDefinition(String name, String extendedID, int position, String symbol, IDomain domain, int categoryType, ExtendedType extendedType) {
		super(name, extendedID, position, symbol, domain, categoryType);
		this.extendedType = extendedType;
	}

	public ArithmeticOperatorDefinition(String name, String extendedID, int position, String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public ArithmeticOperatorDefinition(String name, int id, IDomain domain) {
		super(name, id, domain);
		// TODO Auto-generated constructor stub
	}

	public ArithmeticOperatorDefinition(String name, int id, IDomain domain, int categoryType) {
		super(name, id, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	public ArithmeticOperatorDefinition(String name, int id, int position, IDomain domain) {
		super(name, id, position, domain);
		// TODO Auto-generated constructor stub
	}
	public ArithmeticOperatorDefinition(String name, int id, int position, IDomain domain, int categoricalType) {
		super(name, id, position, domain, categoricalType);
		// TODO Auto-generated constructor stub
	}
	
	public ArithmeticOperatorDefinition(String name, int id, String symbol, IDomain domain) {
		super(name, id, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public ArithmeticOperatorDefinition(String name, int id, String symbol, IDomain domain, int categoryType) {
		super(name, id, symbol, domain, categoryType);
		// TODO Auto-generated constructor stub
	}
	
	public ArithmeticOperatorDefinition(String name, int id, int position, String symbol, IDomain domain) {
		super(name, id, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public ArithmeticOperatorDefinition(String name, int id, int position, String symbol, IDomain domain, int categoryType) {
		super(name, id, position, symbol, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		IDomain lessGeneric = Domains.computeLessGenericDomain(imageDomains);
		boolean is_aggregate = false;
		boolean is_not_sum_associative = false;
		OperatorDefinition asociative_operator = null;
		for (IDomain argDomain:imageDomains) {
			if (argDomain.isInstanceOf(AggregateDomain.DOMAIN)) {
				is_aggregate = true;
				// check if it is SUM associative
				OperatorDefinition check = AssociativeDomainInformation.getAssociativeOperator(argDomain);
				if (!is_not_sum_associative && (check==Operators.SUM || check==Operators.COUNT || check==Operators.COUNT_DISTINCT)) {
					asociative_operator = check;
				} else {
					is_not_sum_associative = true;
				}
			}
		}
		if (lessGeneric.isInstanceOf(IDomain.NUMERIC)) {
			for (IDomain image : imageDomains) {
				if (image.isInstanceOf(IDomainMetaDomain.META)) {
					lessGeneric= image;
					break;
				}
				if (IDomain.CONTINUOUS.isInstanceOf(image)) {// if image is truly continuous, the result is also continuous
					lessGeneric = image;
					break;
				}
			}
		}
        if (is_aggregate) {
        	if (lessGeneric.isInstanceOf(AggregateDomain.DOMAIN)) {
        		if(!lessGeneric.isInstanceOf(IDomain.ANY)) {
					IDomain subdomain = ((IDomainMetaDomain) lessGeneric).getSubdomain();
					lessGeneric = subdomain != null ? subdomain : lessGeneric;
				}else{
					lessGeneric = lessGeneric;
				}
        	}
        	// compose with Aggregate 
        	if (!lessGeneric.isInstanceOf(AggregateDomain.DOMAIN) && !is_not_sum_associative && isAssociative()) {
        		// the result is still sum associative
        		lessGeneric = AggregateDomain.MANAGER.createMetaDomain(lessGeneric, asociative_operator);
        	} else {
        		lessGeneric = AggregateDomain.MANAGER.createMetaDomain(lessGeneric);
        	}
        }
        return lessGeneric;
	}
	
	protected boolean isAssociative() {
		switch (getId()) {
		case IntrinsicOperators.PLUS:
		case IntrinsicOperators.COALESCE:
			return true;
		default:
			return false;
		}
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		 // if concatenating strings, compute the maximum size for the result
		 if (getId()==IntrinsicOperators.CONCAT 
				 // check also (string + ...)
			|| (getId()==IntrinsicOperators.PLUS && types.length>0 && types[0].getDomain().isInstanceOf(IDomain.STRING))) {
			 int totalSize = 0;
			 for (int i=0; i <types.length; i++) {
				 totalSize += types[i].getSize();
			 }
			 return types[0].size(totalSize);
		 }
		 return super.computeExtendedType(types);
	 }

}
