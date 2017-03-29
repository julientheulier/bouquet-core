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
import com.squid.core.domain.operators.OperatorDiagnostic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 03/05/16.
 */
public class DateFromEpochOperatorDefinition extends DateOperatorDefinition {

    public static final String ID = DateOperatorDefinition.DATE_BASE+"FROM_UNIXTIME";

    public DateFromEpochOperatorDefinition(String name, IDomain domain) {
        super(name,ID,domain);
    }


    public DateFromEpochOperatorDefinition(String name, String ID, IDomain domain) {
        super(name,ID, domain);
    }

    @Override
    public List<String> getHint() {
        List<String> hint = new ArrayList<String>();
        hint.add("Convert the given timestamp to unix timestamp");
        hint.add("Convert the given temporal to unix timestamp using the given format");
        hint.add("Convert the given temporal to unix timestamp using the given format");
        return hint;
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain temporal1 = new DomainTemporal();
        IDomain temporal2 = new DomainTemporal();
        IDomain num2 = new DomainNumeric();
        IDomain num1 = new DomainNumeric();

        type.add(num1);
        poly.add(type);
        type = new ArrayList<IDomain>();
        type.add(temporal1);
        type.add(num2);
        poly.add(type);
        type = new ArrayList<IDomain>();
        type.add(temporal1);
        type.add(temporal2);
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
