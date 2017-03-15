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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.squid.core.domain.Domains;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;

/**
 * Special support for CASE operator
 * @author serge fantino
 *
 */
public class CaseOperatorDefinition 
extends ArithmeticOperatorDefinition {

	private static final String HINT = "CASE(condition1,then1,...,[else])";

	public CaseOperatorDefinition(String name, int id) {
		super(name, id, OperatorDefinition.PREFIX_POSITION, IDomain.UNKNOWN);
		this.setCategoryType(OperatorDefinition.LOGICAL_TYPE);
	}
	
	
	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public List getParametersTypes() { // TODO handle infinite number of arguments.
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		type.add(IDomain.CONDITIONAL);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		type.add(IDomain.CONDITIONAL);
		poly.add(type);
		type = new ArrayList<IDomain>(); ;
		return poly;
	}

	
	
	@Override
	public List getSimplifiedParametersTypes() { // TODO handle infinite number of arguments.
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		type.add(IDomain.CONDITIONAL);
		poly.add(type);

		return poly;
	}
	
	
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty()) {
			return new OperatorDiagnostic("invalid CASE expression",HINT);
		}
		Iterator<IDomain> iter = imageDomains.iterator();
		IDomain main = null;
		int i=0;
		while (iter.hasNext()) {
			IDomain first = iter.next();
			i++;
			if (iter.hasNext()) {
				if (!first.isInstanceOf(IDomain.CONDITIONAL)) {
					return new OperatorDiagnostic("invalid CASE expression #"+i+", not a condition",i,HINT);
				}
				// get second
				IDomain second = iter.next();
				i++;
				if (second!=IDomain.NULL) {
					if (main==null) {
						main = second;
					}
					else if (main.isInstanceOf(second)) {
						main = second;
					}
					else if (!second.isInstanceOf(main)) {
						return new OperatorDiagnostic("invalid CASE expression, ELSE clause type mismatch",i,HINT);
					}
				}
			} else {
				if (main!=null&&!(first.isInstanceOf(main)||main.isInstanceOf(first))&&first!=IDomain.NULL) {
					return new OperatorDiagnostic("invalid CASE expression, ELSE clause type mismatch",HINT);
				} else if (main==null && first!=IDomain.NULL) {
					main=first;
				}
			}
		}
		if (main==null) {
			return new OperatorDiagnostic("invalid CASE expression, unable to define expression type",HINT);
		}
		return OperatorDiagnostic.IS_VALID;
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		//
		// skip the tests ? but check if the test is an Aggregate !
		//
		ArrayList<IDomain> copy = new ArrayList<IDomain>(imageDomains);
		boolean is_aggregate = false;
		for (int i=0,j=0;i+1<imageDomains.size();) {
			if (copy.get(j).isInstanceOf(AggregateDomain.DOMAIN)) {
				is_aggregate = true;
			}
			copy.remove(j++);
			i+=2;
		}
		// skip null 
		Iterator<IDomain> iter = copy.iterator();
		for (;iter.hasNext();) {
			IDomain domain = iter.next();
			if (domain==IDomain.NULL) {
				iter.remove();
			}
		}
        IDomain lessGeneric = Domains.computeLessGenericDomain(copy);
        if (is_aggregate && !lessGeneric.isInstanceOf(AggregateDomain.DOMAIN)) {
        	// compose with Aggregate
        	lessGeneric = AggregateDomain.MANAGER.createMetaDomain(lessGeneric);
        }
        //
        return lessGeneric;
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length<1) {
			return ExtendedType.FLOAT;
		} else {
			ExtendedType[] values = new ExtendedType[types.length/2+types.length%2];
			for (int i=0,j=0;i<types.length;i+=2,j++) {
				if (i+1<types.length) {
					values[j]=types[i+1];
				} else {
					values[j]=types[i];//default==else
				}
			}
			return fixExtendedTypeDomain(super.computeExtendedType(values),types);
		}
	}

}
