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
package com.squid.core.domain.extensions.date.extract;

import com.squid.core.domain.DomainTime;
import com.squid.core.domain.DomainTimestamp;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDiagnostic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 02/05/16.
 */
public class ExtractMinuteOperatorDefinition extends ExtractOperatorDefinition {

    public static final String ID = ExtractOperatorDefinition.EXTRACT_BASE+"MINUTE";

    public ExtractMinuteOperatorDefinition(String name, int categoryType) {
        super(name, ID, categoryType);
    }

    public ExtractMinuteOperatorDefinition(String name, String ID, int categoryType) {
        super(name, ID, categoryType);
    }

    @Override
    public ListContentAssistEntry getListContentAssistEntry(){
        if(super.getListContentAssistEntry()==null){
            List <String> descriptions = new ArrayList<String>();
            descriptions.add("Extract minutes from the given time");
            descriptions.add("Extract minutes from the given timestamp");

            ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
            setListContentAssistEntry(entry);
        }
        return super.getListContentAssistEntry();
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain time1 = new DomainTime();
        time1.setContentAssistLabel("time");
        time1.setContentAssistProposal("${1:time}");

        IDomain timestamp1 = new DomainTimestamp();
        timestamp1.setContentAssistLabel("timestamp");
        timestamp1.setContentAssistProposal("${1:timestamp}");


        type.add(time1);
        poly.add(type);

        type = new ArrayList<IDomain>();
        type.add(timestamp1);
        poly.add(type);


        return poly;
    }

    @Override
    public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
        if (imageDomains.size()!=1) {
            return new OperatorDiagnostic("Invalid number of parameters", getName() + "(temporal)");
        }
        if (!imageDomains.get(0).isInstanceOf(IDomain.TIME)
                && !imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP)
                ) {
            return new OperatorDiagnostic("Parameter #1 must be a time or timestamp but it is a " + imageDomains.get(0).getName(), 1, getName() + "(time or timestamp)");
        }
        return OperatorDiagnostic.IS_VALID;
    }
}