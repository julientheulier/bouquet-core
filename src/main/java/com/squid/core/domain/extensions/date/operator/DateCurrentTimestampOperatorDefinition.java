/*******************************************************************************
 * Copyright © Squid Solutions, 2016
 * <p/>
 * This file is part of Open Bouquet software.
 * <p/>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 * <p/>
 * There is a special FOSS exception to the terms and conditions of the
 * licenses as they are applied to this program. See LICENSE.txt in
 * the directory of this program distribution.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * <p/>
 * Squid Solutions also offers commercial licenses with additional warranties,
 * professional functionalities or services. If you purchase a commercial
 * license, then it supersedes and replaces any other agreement between
 * you and Squid Solutions (above licenses and LICENSE.txt included).
 * See http://www.squidsolutions.com/EnterpriseBouquet/
 *******************************************************************************/
package com.squid.core.domain.extensions.date.operator;

import com.squid.core.domain.*;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 02/05/16.
 */
public class DateCurrentTimestampOperatorDefinition extends DateOperatorDefinition {
    public static final String ID = DateOperatorDefinition.DATE_BASE+"CURRENT_TIMESTAMP";

    public DateCurrentTimestampOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }

    public DateCurrentTimestampOperatorDefinition(String name, String ID, IDomain domain) {
        super(name,ID, domain);
    }

    @Override
    public List<String> getHint() {
        List<String> hint = new ArrayList<String>();
        hint.add("Return current timestamp");
        return hint;
    }


    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        poly.add(type);
        return poly;
    }
    
    @Override
    public List getSimplifiedParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        poly.add(type);
        return poly;
    }

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}
	
	@Override
    public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
        return ExtendedType.TIMESTAMP;
    }

    @Override
    public IDomain computeImageDomain(List<IDomain> imageDomains) {
        IDomain rawDomain = computeRawImageDomain(imageDomains);
        for (IDomain domain : imageDomains) {
            //Cannot cast ANY to META
            if(domain.isInstanceOf(IDomain.ANY)){
                return IDomain.DATE;
            }
            if (domain.isInstanceOf(DomainMetaDomain.META)) {
                return ((IDomainMetaDomain)domain).createMetaDomain(rawDomain);
            }
        }
        //
        return rawDomain;
    }

    public IDomain computeRawImageDomain(List<IDomain> imageDomains) {
        return IDomain.TIMESTAMP;
    }
}
