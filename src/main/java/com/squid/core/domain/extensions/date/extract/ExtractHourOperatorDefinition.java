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

import com.squid.core.domain.DomainAny;
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
public class ExtractHourOperatorDefinition extends ExtractOperatorDefinition {

    public static final String ID = ExtractOperatorDefinition.EXTRACT_BASE+"HOUR";

    public ExtractHourOperatorDefinition(String name, int categoryType) {
        super(name, ID, categoryType);
    }

    public ExtractHourOperatorDefinition(String name, String ID, int categoryType) {
        super(name, ID, categoryType);
    }

    @Override
    public List<String> getHint() {
        List<String> hint = new ArrayList<String>();
        hint.add("Extract hour from the given time");
        hint.add("Extract hour from the given timestamp");
        return hint;
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain time1 = new DomainTime();
        IDomain timestamp1 = new DomainTimestamp();


        type.add(time1);
        poly.add(type);

        type = new ArrayList<IDomain>();
        type.add(timestamp1);
        poly.add(type);


        return poly;
    }

}
