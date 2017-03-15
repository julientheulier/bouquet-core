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

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.DomainConditional;
import com.squid.core.domain.IDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author serge fantino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AlgebraicOperatorDefinition extends OperatorDefinition {
	
    public AlgebraicOperatorDefinition(String name, String extendedID, int position, String symbol, IDomain domain) {
		super(name, extendedID, position, symbol, domain);
		this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}    

	public AlgebraicOperatorDefinition(String name, int id, IDomain domain) {
		super(name, id, domain);
		this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}

	public AlgebraicOperatorDefinition(String name, int id, int position, IDomain domain) {
		super(name, id, position, domain);
		this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}

	public AlgebraicOperatorDefinition(String name, int id, String symbol, IDomain domain) {
		super(name, id, symbol, domain);
		this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	} 

	public AlgebraicOperatorDefinition(String name, int id, int position, String symbol, IDomain domain) {
		super(name, id, position, symbol, domain);
		this.setCategoryType(OperatorDefinition.MATHS_TYPE);
	}
	

	/* (non-Javadoc)
     * @see com.squid.ldm.model.expressions.OperatorDefinition#getType()
     */
    @Override
	public int getType() {
        return ALGEBRAIC_TYPE;
    }
    
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
    	ExtendedType result = ExtendedType.UNDEFINED;
		for (int i=0;i<types.length;i++) {
			ExtendedType challenger = types[i];
    		if (ExtendedType.UNDEFINED.equals(challenger)) {
    			// ignore
    		} else if (ExtendedType.UNDEFINED.equals(result)) {
    			result = new ExtendedType(challenger);
    		} else {
    			result = reconcileTypes(result, challenger);
    		}
		}
		if (ExtendedType.UNDEFINED.equals(result) && types.length==1) {
			result = types[0];
		}
		return fixExtendedTypeDomain(result,types);
	}

	/**
	 * Reconcile the two types using the typeOrder
	 * @param first
	 * @param second
	 * @return
	 */
	protected ExtendedType reconcileTypes(ExtendedType first, ExtendedType second) {
		int[] firstOrder = first.computeTypeOrder();
		int[] secondOrder = second.computeTypeOrder();
		if (firstOrder[0]>secondOrder[0]) {
			return first;
		} else if (firstOrder[0]<secondOrder[0]) {
			return second;
		} else if (firstOrder[1]>secondOrder[1]) {
			return mergeTypes(firstOrder,first,second);
		} else if (firstOrder[1]<secondOrder[1]) {
			return mergeTypes(secondOrder,second,first);
		} else {
			return mergeTypes(firstOrder,first,second);
		}
	}

	/**
	 * If two types have the same order, merge the definition (size, scale, ...)
	 * @param typeOrder
	 * @param master
	 * @param slave
	 * @return
	 */
	protected ExtendedType mergeTypes(int[] typeOrder, ExtendedType master, ExtendedType slave) {
		ExtendedType copy = master;
		if (slave.getScale() > master.getScale()) {
			copy = copy.scale(slave.getScale());
		}
		if (getId() == IntrinsicOperators.PLUS && typeOrder[0] == ExtendedType.STRING_ORDER) {
			copy = copy.size(master.getSize() + slave.getSize());
		} else if (slave.getSize() > master.getSize()) {
			copy = copy.size(slave.getSize());
		}
		return copy;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List<IDomain>>();
		List type = new ArrayList<IDomain>();
		IDomain any = new DomainAny();
		any.setContentAssistLabel("any");
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		poly.add(type);
		type = new ArrayList<IDomain>();
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		type.add(any);
		poly.add(type);
		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		return OperatorDiagnostic.IS_VALID;
	}
}
