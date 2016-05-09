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

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd 
 * Truncate definition
 */
public class TruncateOperatorDefintion extends OperatorDefinition {

	public static final String TRUNCATE = MathsOperatorRegistry.MATHS_BASE
			+ "TRUNCATE";
	public TruncateOperatorDefintion(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
	}
	
	public TruncateOperatorDefintion(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
	}
	
	public TruncateOperatorDefintion(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.NUMERIC, categoryType);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		type.add(IDomain.NUMERIC);
		poly.add(type);
		type.clear();
		type.add(IDomain.NUMERIC);
		type.add(IDomain.NUMERIC);
		poly.add(type);
		type.clear();
		type.add(IDomain.NUMERIC);
		type.add(DomainNumericConstant.DOMAIN);
		poly.add(type);
		return poly;
	}

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
		if (imageDomains.size() == 2
				&& !imageDomains.get(1).isInstanceOf(IDomain.NUMERIC)) {
			return new OperatorDiagnostic(
					"the second Parameter must be an integer", getName());
		}
		if (imageDomains.size() == 2 && !(imageDomains.get(1) instanceof DomainNumericConstant)) {
			return new OperatorDiagnostic(
					"the second Parameter must be an constant", getName());
		} else if (imageDomains.size() == 2) {
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
		return IDomain.NUMERIC;
		
	}

}
