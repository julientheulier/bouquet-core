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

    public DateMonthsBetweenOperatorDefinition(String name, IDomain domain, int categoryType) {
        super(name, ID, domain, categoryType);
    }

    public DateMonthsBetweenOperatorDefinition(String name, String ID, IDomain domain) {
        super(name,ID, domain);
    }

    public DateMonthsBetweenOperatorDefinition(String name, String ID, IDomain domain, int categoryType) {
        super(name,ID, domain, categoryType);
    }

    @Override
    public ListContentAssistEntry getListContentAssistEntry(){
        if(super.getListContentAssistEntry()==null){
            List <String> descriptions = new ArrayList<String>();
            descriptions.add("Compute the number of months between the two temporals");
            descriptions.add("");

            ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
            setListContentAssistEntry(entry);
        }
        return super.getListContentAssistEntry();
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain temporal1 = new DomainTemporal();
        temporal1.setContentAssistLabel("temporal");
        temporal1.setContentAssistProposal("${1:temporal}");
        IDomain temporal2 = new DomainTemporal();
        temporal2.setContentAssistLabel("temporal");
        temporal2.setContentAssistProposal("${2:format}");
        IDomain num2 = new DomainNumeric();
        num2.setContentAssistLabel("num");
        num2.setContentAssistProposal("${2:n}");



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
    public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
        if (imageDomains.size()>0 && imageDomains.size()<=2) {
            int cpt = 0;
            for (IDomain domain : imageDomains) {
                cpt++;
                if (!domain.isInstanceOf(IDomain.TEMPORAL) && cpt==1 || cpt==2 && !domain.isInstanceOf(IDomain.TEMPORAL) && !domain.isInstanceOf(IDomain.NUMERIC)) {
                    return new OperatorDiagnostic("Invalid type of parameters",getName()+"(temporal, temporal or integer)");
                }
            }
        } else if (imageDomains.size()==3) {
                    return new OperatorDiagnostic("Invalid function",getName());
        }
        if (imageDomains.size()==2) {
                return OperatorDiagnostic.IS_VALID;
        } else {
            return new OperatorDiagnostic("Invalid number of parameters",getName());
        }
    }

    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
        return fixExtendedTypeDomain(computeRawExtendedType(types), types);
    }

    public ExtendedType computeRawExtendedType(ExtendedType[] types) {
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
