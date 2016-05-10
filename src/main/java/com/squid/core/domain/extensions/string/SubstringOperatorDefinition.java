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
package com.squid.core.domain.extensions.string;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class SubstringOperatorDefinition extends OperatorDefinition {

	public static final String STRING_BASE = "com.squid.domain.operator.string.";

	public static final String STRING_SUBSTRING = STRING_BASE+"SUBSTRING";
	
	private String hint = "";
	
	public SubstringOperatorDefinition(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
		hint = name+"(string,begin) or "+name+"(string,-being) or "+name+"(string begin,end)";
	}
	
	public SubstringOperatorDefinition(String name, String ID, IDomain domain, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,domain, categoryType);
		hint = name+"(string,begin) or "+name+"(string,-being) or "+name+"(string begin,end)";
	}
	
	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		type.add(IDomain.STRING);
		type.add(DomainNumericConstant.DOMAIN);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.STRING);
		type.add(IDomain.NUMERIC);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.STRING);
		type.add(DomainNumericConstant.DOMAIN);
		type.add(IDomain.NUMERIC);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.STRING);
		type.add(IDomain.NUMERIC);
		type.add(IDomain.NUMERIC);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.STRING);
		type.add(DomainNumericConstant.DOMAIN);
		type.add(DomainNumericConstant.DOMAIN);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.STRING);
		type.add(IDomain.NUMERIC);
		type.add(DomainNumericConstant.DOMAIN);
		poly.add(type);
		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (!(imageDomains.size()==2 || imageDomains.size()==3)) {
			return new OperatorDiagnostic("Invalid number of parameters",hint);
		}
		if (!imageDomains.get(0).isInstanceOf(IDomain.STRING)) {
			return new OperatorDiagnostic("Parameter must be a string",0,hint);
		}
		if(!(imageDomains.get(1).isInstanceOf(IDomain.NUMERIC)) || ((imageDomains.get(1) instanceof DomainNumericConstant) && ((DomainNumericConstant)imageDomains.get(1)).getValue()!=Math.floor(((DomainNumericConstant)imageDomains.get(1)).getValue()))) {
			return new OperatorDiagnostic("First parameter must be an integer",1,hint);
		}
		if (imageDomains.size()==3 && (!(imageDomains.get(2).isInstanceOf(IDomain.NUMERIC)) || ((imageDomains.get(2) instanceof DomainNumericConstant) && ((DomainNumericConstant)imageDomains.get(2)).getValue()!=Math.floor(((DomainNumericConstant)imageDomains.get(2)).getValue())))) {
			return new OperatorDiagnostic("Second parameter must be an integer",2,hint);
		}
		return OperatorDiagnostic.IS_VALID;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length>0) {
			if (types.length==2) {
				if (types[1].getDomain() instanceof DomainNumericConstant) {
					return new ExtendedType(IDomain.STRING,Types.VARCHAR,0,(types[0].getSize() + 1 - new Double(((DomainNumericConstant)types[types.length-1].getDomain()).getValue()).intValue()));
				} else {
					return types[0];
				}
			} else {
				if (types[1].getDomain() instanceof DomainNumericConstant && types[2].getDomain() instanceof DomainNumericConstant) {
					return new ExtendedType(IDomain.STRING,Types.VARCHAR,0,(new Double(((DomainNumericConstant)types[2].getDomain()).getValue()).intValue() + 1 - new Double(((DomainNumericConstant)types[1].getDomain()).getValue()).intValue()));
				} else {
					return types[0];
				}
			}
		} else {
			return ExtendedType.UNDEFINED;
		}
	}

}
