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

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 02/05/16.
 */
public class DateMonthsBetweenOperatorDefinition extends DateOperatorDefinition{
    public static final String ID = DateOperatorDefinition.DATE_BASE+"MONTHS_BETWEEN";

    public DateMonthsBetweenOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }

    public DateMonthsBetweenOperatorDefinition(String name, String ID, IDomain domain) {
        super(name,ID, domain);
    }


    @Override
    public List<String> getHint() {
        List<String> hint = new ArrayList<String>();
        hint.add("Compute the number of months between the two temporals");
        hint.add("");
        return hint;
    }


    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain temporal1 = new DomainTemporal();
        temporal1.setContentAssistLabel("temporal");
        IDomain temporal2 = new DomainTemporal();
        temporal2.setContentAssistLabel("temporal");
        IDomain num2 = new DomainNumeric();
        num2.setContentAssistLabel("num");



        type = new ArrayList<IDomain>();
        type.add(temporal1);
        type.add(temporal2);
        poly.add(type);

        type = new ArrayList<IDomain>();
        type.add(temporal1);
        type.add(num2);
        poly.add(type);


        return poly;
    }

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
    public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
        return new ExtendedType(IDomain.NUMERIC, Types.FLOAT,0,0);
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
        return IDomain.NUMERIC;

    }
}
