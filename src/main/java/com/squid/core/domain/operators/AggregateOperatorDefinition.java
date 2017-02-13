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
import java.util.List;

import com.squid.core.domain.DomainIntrinsic;
import com.squid.core.domain.DomainNumeric;
import com.squid.core.domain.DomainString;
import com.squid.core.domain.DomainTemporal;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.analytics.AnalyticDomain;

/**
 * @author serge fantino
 *
 * A generic operator definition for Aggregate operators (SUM, MIN, MAX...)
 *
 */
public class AggregateOperatorDefinition extends OperatorDefinition {
	
	private ArrayList<IDomain> validDomains = null;

    public AggregateOperatorDefinition(String name, int id) {
		super(name, id, IDomain.AGGREGATE);
		init();
		this.setCategoryType(OperatorDefinition.AGGR_TYPE);
	}

	public AggregateOperatorDefinition(String name, int id, int position) {
		super(name, id, position, IDomain.AGGREGATE);
		init();
		this.setCategoryType(OperatorDefinition.AGGR_TYPE);
	}

	public AggregateOperatorDefinition(String name, int id, String symbol) {
		super(name, id, symbol, IDomain.AGGREGATE);
		init();
		this.setCategoryType(OperatorDefinition.AGGR_TYPE);
	}
	
	public AggregateOperatorDefinition(String name, String extendedID,
			int position, String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		init();
		this.setCategoryType(OperatorDefinition.AGGR_TYPE);
	}


	protected void init() {
		setParamCount(1);
		validDomains = new ArrayList<IDomain>();
		switch (getId()) {
		case IntrinsicOperators.SUM:
			validDomains.add(IDomain.NUMERIC);
			break;
		case IntrinsicOperators.MEDIAN:
		case IntrinsicOperators.VARIANCE:
		case IntrinsicOperators.STDDEV:
		case IntrinsicOperators.AVG:
			validDomains.add(IDomain.NUMERIC);
			validDomains.add(IDomain.TEMPORAL);
			break;
		case IntrinsicOperators.MAX:
		case IntrinsicOperators.MIN:
			validDomains.add(IDomain.STRING);
			validDomains.add(IDomain.NUMERIC);
			validDomains.add(IDomain.TEMPORAL);
			break;
		case IntrinsicOperators.COUNT:
		case IntrinsicOperators.COUNT_DISTINCT:
			validDomains.add(IDomain.INTRINSIC);// count support any domain
			break;
		}
	}

	public ArrayList<IDomain> getValidDomains() {
		return validDomains;
	}

	/* (non-Javadoc)
     * @see com.squid.ldm.model.expressions.OperatorDefinition#getType()
     */
    @Override
	public int getType() {
        return AGGREGATE_TYPE;
    }
    
    @Override
    public IDomain computeImageDomain(List<IDomain> sourceDomain) {
        // source domain should be one dimension
        if (getParamCount()>=0&&sourceDomain.size()!=getParamCount()) {
            return IDomain.UNKNOWN;
        } else if (getParamCount()>=1) {
            IDomain source = sourceDomain.get(0);
            // by default this operator is not associative
            return AggregateDomain.MANAGER.createMetaDomain(source);
        } else {// no param
        	// it's the count...
        	return AggregateDomain.MANAGER.createMetaDomain(IDomain.NUMERIC);
        }
    }



	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();
		validDomains = new ArrayList<IDomain>();
		IDomain num1 = new DomainNumeric();
		IDomain string1 = new DomainString();
		IDomain temp1 = new DomainTemporal();
		IDomain intrinsic1 = new DomainIntrinsic();

		switch (getId()) {
			case IntrinsicOperators.SUM:
				type.add(num1);
				poly.add(type);
				type=new ArrayList<IDomain>();
				break;
			case IntrinsicOperators.MEDIAN:
			case IntrinsicOperators.VARIANCE:
			case IntrinsicOperators.STDDEV:
			case IntrinsicOperators.AVG:
				type.add(num1);
				poly.add(type);
				type=new ArrayList<IDomain>();
				type.add(temp1);
				poly.add(type);
				type=new ArrayList<IDomain>();
				break;
			case IntrinsicOperators.MAX:
			case IntrinsicOperators.MIN:
				type.add(num1);
				poly.add(type);
				type=new ArrayList<IDomain>();
				type.add(temp1);
				poly.add(type);
				type=new ArrayList<IDomain>();
				type.add(string1);
				poly.add(type);
				break;
			case IntrinsicOperators.COUNT:
			case IntrinsicOperators.COUNT_DISTINCT:
				// count support any domain
				type.add(intrinsic1);
				poly.add(type);
				type=new ArrayList<IDomain>();
				break;
		}
		return poly;
	}

    @Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		// kept just to get specific message, not necessary for type checking.
    	if (imageDomains.size()==1) {
	    	IDomain domain = imageDomains.get(0);
	    	//
			if(!domain.isInstanceOf(IDomain.ANY)) {
				// domain cannot be already aggregated
				if (domain.isInstanceOf(AggregateDomain.DOMAIN)) {
					return new OperatorDiagnostic("argument #1: Invalid type: cannot nest aggregate operator", 1);
				}
				//
				// domain cannot be already analytic
				if (domain.isInstanceOf(AnalyticDomain.DOMAIN)) {
					return new OperatorDiagnostic("argument #1: Invalid type: cannot nest analytic operator", 1);
				}
			}
    	}
    	return super.validateParameters(imageDomains);
    }
    
    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
    	//
		ExtendedType result = types.length>0?types[0]:ExtendedType.NUMERIC;
		//
		if (result.getDomain().isInstanceOf(IDomain.NUMERIC)) {
			switch (getId()) {
			case IntrinsicOperators.VARIANCE:
			case IntrinsicOperators.STDDEV:
				// force floating
				{
    				result = ExtendedType.FLOAT.size(result.getSize());
				}
				break;
			case IntrinsicOperators.AVG:
				if (result.getDomain().isInstanceOf(IDomain.NUMERIC)) {
    				result = ExtendedType.FLOAT.size(result.getSize());
				}
				break;
			case IntrinsicOperators.MAX:
			case IntrinsicOperators.MIN:
				break;
			case IntrinsicOperators.SUM:
				// increase size ?
				if (result.isExactNumber()) {
					result = result.size(Math.max(15,result.getSize()));
				}
				break;
			case IntrinsicOperators.COUNT:
			case IntrinsicOperators.COUNT_DISTINCT:
				result = ExtendedType.INTEGER;
				break;
			}
		}
		//
		return fixExtendedTypeDomain(result, types);
    }

}
