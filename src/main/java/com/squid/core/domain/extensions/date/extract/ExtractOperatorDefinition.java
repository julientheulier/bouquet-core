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

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainDate;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.aggregate.AggregateDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;

public class ExtractOperatorDefinition extends OperatorDefinition {

    public static final String EXTRACT_BASE = "com.squid.domain.operator.extract.";
    public static final String EXTRACT_DAY = EXTRACT_BASE+"DAY";
    public static final String EXTRACT_MONTH = EXTRACT_BASE+"MONTH";
    public static final String EXTRACT_YEAR = EXTRACT_BASE+"YEAR";
    public static final String EXTRACT_HOUR = EXTRACT_BASE+"HOUR";
    public static final String EXTRACT_MINUTE = EXTRACT_BASE+"MINUTE";
    public static final String EXTRACT_SECOND = EXTRACT_BASE+"SECOND";
    public static final String EXTRACT_DAY_OF_WEEK = EXTRACT_BASE+"DAY_OF_WEEK";
    public static final String EXTRACT_DAY_OF_YEAR = EXTRACT_BASE+"DAY_OF_YEAR";

    public ExtractOperatorDefinition(String name, int categoryType) {
        super(name, EXTRACT_BASE + name, PREFIX_POSITION, name, IDomain.NUMERIC, categoryType);
    }

    public ExtractOperatorDefinition(String name, String ID, int categoryType) {
        super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC, categoryType);
    }

    @Override
    public int getType() {
        return ALGEBRAIC_TYPE;
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();
        IDomain date1 = new DomainDate();
        type.add(date1);
        poly.add(type);
        return poly;
    }

    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
        return new ExtendedType(IDomain.NUMERIC, Types.NUMERIC, 0, 2);
    }

    @Override
    public IDomain computeImageDomain(List<IDomain> imageDomains) {
        if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argument0 = imageDomains.get(0);
        boolean is_aggregate = argument0.isInstanceOf(AggregateDomain.DOMAIN);
        IDomain domain = IDomain.NUMERIC;
        if (is_aggregate) {
            // compose with Aggregate
            domain = AggregateDomain.MANAGER.createMetaDomain(domain);
        }
        //
        return domain;
    }

}
