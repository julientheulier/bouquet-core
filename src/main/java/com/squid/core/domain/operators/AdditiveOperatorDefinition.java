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

import com.squid.core.domain.IDomain;

public class AdditiveOperatorDefinition 
extends ArithmeticOperatorDefintion
{

	public AdditiveOperatorDefinition(String name, int id, IDomain domain,
			int categoryType) {
		super(name, id, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	public AdditiveOperatorDefinition(String name, int id, IDomain domain) {
		super(name, id, domain);
		// TODO Auto-generated constructor stub
	}

	public AdditiveOperatorDefinition(String name, int id, int position,
			IDomain domain) {
		super(name, id, position, domain);
		// TODO Auto-generated constructor stub
	}

	public AdditiveOperatorDefinition(String name, int id, int position,
			String symbol, IDomain domain, int categoryType) {
		super(name, id, position, symbol, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	public AdditiveOperatorDefinition(String name, int id, int position,
			String symbol, IDomain domain) {
		super(name, id, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public AdditiveOperatorDefinition(String name, int id, String symbol,
			IDomain domain) {
		super(name, id, symbol, domain);
		// TODO Auto-generated constructor stub
	}

	public AdditiveOperatorDefinition(String name, String extendedID,
			int position, String symbol, IDomain domain, int categoryType,
			ExtendedType extendedType) {
		super(name, extendedID, position, symbol, domain, categoryType, extendedType);
		// TODO Auto-generated constructor stub
	}

	public AdditiveOperatorDefinition(String name, String extendedID,
			int position, String symbol, IDomain domain, int categoryType) {
		super(name, extendedID, position, symbol, domain, categoryType);
		// TODO Auto-generated constructor stub
	}

	public AdditiveOperatorDefinition(String name, String extendedID,
			int position, String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()==2) {
			IDomain first = imageDomains.get(0);
			IDomain second = imageDomains.get(1);
			if (first.isInstanceOf(IDomain.TEMPORAL) 
			 || second.isInstanceOf(IDomain.TEMPORAL)) {
				// in case of calculus involving temporal, delegate to DateOperatorDefinition
				if (getId()==IntrinsicOperators.PLUS) {
					return Operators.DATE_ADD.validateParameters(imageDomains);
				} else if (getId()==IntrinsicOperators.SUBTRACTION) {
					return Operators.DATE_SUB.validateParameters(imageDomains);
				}
			}
		}
		// only works for intrinsic types
		for (IDomain d : imageDomains) {
			if (!d.isInstanceOf(IDomain.INTRINSIC)) {
				return new OperatorDiagnostic("invalid arithmetic operation","arithmetic operator only applies to intrinsic values");
			}
		}
		//
		return OperatorDiagnostic.IS_VALID;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length==2) {
			if (types[0].getDomain().isInstanceOf(IDomain.TEMPORAL) 
			 || types[1].getDomain().isInstanceOf(IDomain.TEMPORAL)) {
				// in case of calculus involving temporal, delegate to DateOperatorDefinition
				if (getId()==IntrinsicOperators.PLUS) {
					return Operators.DATE_ADD.computeExtendedType(types);
				} else if (getId()==IntrinsicOperators.SUBTRACTION) {
					return Operators.DATE_SUB.computeExtendedType(types);
				}
			}
		}
		// else
		return super.computeExtendedType(types);
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()==2) {
			IDomain first = imageDomains.get(0);
			IDomain second = imageDomains.get(1);
			if (first.isInstanceOf(IDomain.TEMPORAL) 
			 || second.isInstanceOf(IDomain.TEMPORAL)) {
				// in case of calculus involving temporal, delegate to DateOperatorDefinition
				if (getId()==IntrinsicOperators.PLUS) {
					return Operators.DATE_ADD.computeImageDomain(imageDomains);
				} else if (getId()==IntrinsicOperators.SUBTRACTION) {
					return Operators.DATE_SUB.computeImageDomain(imageDomains);
				}
			}
		}
		// else
		return super.computeImageDomain(imageDomains);
	}

}
