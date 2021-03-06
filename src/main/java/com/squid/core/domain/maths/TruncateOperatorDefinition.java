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
package com.squid.core.domain.maths;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainNumeric;
import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd 
 * Truncate definition
 */
public class TruncateOperatorDefinition extends OperatorDefinition {

	public static final String TRUNCATE = MathsOperatorRegistry.MATHS_BASE
			+ "TRUNCATE";
	public TruncateOperatorDefinition(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
		this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}
	
	public TruncateOperatorDefinition(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
		this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}
	
	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("TRUNCATE returns n rounded to 0 places to the right of the decimal point");
		hint.add("Takes two arguments to compute TRUNCATE(column_name,decimals)");
		hint.add("Takes two arguments to compute TRUNCATE(column_name,decimals)");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		IDomain number = new DomainNumeric();
		type.add(number);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(number);
		IDomain decimals = new DomainNumeric();
		decimals.setContentAssistLabel("decimals");
		type.add(decimals);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(number);
		IDomain num = new DomainNumericConstant();
		num.setContentAssistLabel("constant");
		type.add(num);
		poly.add(type);
		return poly;
	}
	@Override
	public List getSimplifiedParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		IDomain number = new DomainNumeric();
		type.add(number);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(number);
		IDomain num = new DomainNumericConstant();
		num.setContentAssistLabel("constant");
		type.add(num);
		poly.add(type);
		return poly;
	}
	
	

	// We need  to add integer idomain and interger constant idomain to have the notion needed for the validate.
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size() != 1 && imageDomains.size() != 2) {
			return new OperatorDiagnostic("Invalid number of parameters",
					getName());
		}
		// check if parameter is valid?
		if (!imageDomains.get(0).isInstanceOf(IDomain.NUMERIC)) {
			return new OperatorDiagnostic(
					"The first Parameter must be a number", getName());
		}
		if (imageDomains.get(0).isInstanceOf(IDomain.ANY)) {
			return new OperatorDiagnostic("Parameters must be numbers (Any given)",
					getName());
		}
		if (imageDomains.size() == 2
				&& !imageDomains.get(1).isInstanceOf(IDomain.NUMERIC)) {
			return new OperatorDiagnostic(
					"the second Parameter must be an integer", getName());
		}
		if (imageDomains.size() == 2 && !(imageDomains.get(1) instanceof DomainNumericConstant)) {
			return new OperatorDiagnostic(
					"the second Parameter must be an constant", getName());
		} else if (imageDomains.size() == 2) {
			if (imageDomains.get(1).isInstanceOf(IDomain.ANY)) {
				return new OperatorDiagnostic("Parameters must be numbers (Any given)",
						getName());
			}
			Double d = ((DomainNumericConstant) imageDomains.get(1)).getValue();
			if (Math.floor(d)!=d || Math.abs(d)!=d) {
				return new OperatorDiagnostic(
						"the second Parameter must be a positive integer", getName());

			}
		}
		return OperatorDiagnostic.IS_VALID;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		if (types.length >1) {
			ExtendedType currentType = types[0];
			if (types[0].getDomain() instanceof DomainNumericConstant) {
				if (currentType.getDataType()!= Types.FLOAT && currentType.getDataType()!= Types.REAL) {
					int scale = new Double(((DomainNumericConstant) types[0].getDomain()).getValue()).intValue();
					int size = currentType.getSize();
					return new ExtendedType(IDomain.CONTINUOUS,Types.DECIMAL,scale, size-scale);
				}
				
			}
			return currentType;
		}		
		return ExtendedType.INTEGER;
	}
	
	@Override
	public IDomain  computeImageDomain(List<IDomain> imageDomains) {

		if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argument0 = imageDomains.get(0);
		boolean is_aggregate = argument0.isInstanceOf(AggregateDomain.DOMAIN);
		IDomain domain = IDomain.UNKNOWN;
		if (imageDomains.size()==1) {
			domain = IDomain.NUMERIC;
		} else {
			domain = IDomain.CONTINUOUS;
		}
        if (is_aggregate) {
        	// compose with Aggregate
        	domain = AggregateDomain.MANAGER.createMetaDomain(domain);
        }
        //
        return domain;
	}

}
