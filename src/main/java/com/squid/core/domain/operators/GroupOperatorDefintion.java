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

public class GroupOperatorDefintion extends AlgebraicOperatorDefinition {

    /**
     * @param name
     * @param id
     * @param position
     */
    public GroupOperatorDefintion() {
        super("GROUP", IntrinsicOperators.IDENTITY, WRAPPER_POSITION, IDomain.UNKNOWN);
        setParamCount(1);
    }
    
    @Override
    public IDomain computeImageDomain(List<IDomain> sourceDomain) {
        // source domain should be one dimension
        if (sourceDomain.size()!=1) {
            return IDomain.UNKNOWN;
        } else {
            return sourceDomain.get(0);
        }
    }

    public String prettyPrint(String[] args) {
        return "("+super.prettyPrint(args,true)+")";
    }
    
    @Override
    public String prettyPrint(String[] args, boolean showBrackets) {
        return "("+super.prettyPrint(args,true)+")";
    }
    
    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
    	if (types.length==1) {
    		return types[0];
    	} else {
    		return ExtendedType.UNDEFINED;
    	}
    }

}
