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
 * Created by lrabiet on 03/05/16.
 */
public class DateIntervalOperatorDefinition extends DateOperatorDefinition {

    public static final String ID = DateOperatorDefinition.DATE_BASE+"DATE_INTERVAL";

    public DateIntervalOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }


    public DateIntervalOperatorDefinition(String name, String ID, IDomain domain) {
        super(name,ID, domain);
    }


    @Override
    public List<String> getHint() {
        List<String> hint = new ArrayList<String>();
        hint.add("Compute the interval between two timestamps with a unit");
        return hint;
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain temporal1 = new DomainTemporal();
        IDomain temporal2 = new DomainTemporal();
        IDomain timestamp1 = new DomainTimestamp();
        IDomain timestamp2 = new DomainTimestamp();
        IDomain num2 = new DomainNumeric();
        IDomain date1 = new DomainDate();
        IDomain interval2 = new DomainInterval();
        IDomain numConst2 = new DomainNumericConstant();
        IDomain stringConst3 = new DomainStringConstant("");
        stringConst3.setContentAssistLabel("unit");
        IDomain string3 = new DomainString();
        string3.setContentAssistLabel("unit");


        type.add(timestamp1);
        type.add(timestamp2);
        type.add(stringConst3);
        poly.add(type);
        type = new ArrayList<IDomain>();
        /*type.add(timestamp1);
        type.add(timestamp2);
        type.add(string3);
        poly.add(type);*/

        return poly;
    }

    @Override
    public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
        if(imageDomains.size()>2){
            if(imageDomains.get(2).isInstanceOf(DomainStringConstant.DOMAIN) && !imageDomains.get(2).isInstanceOf(IDomain.ANY)) {
                String unit = ((DomainStringConstant) imageDomains.get(2)).getValue();
                if (!"SECOND".equals(unit) && !"MINUTE".equals(unit) && !"HOUR".equals(unit) && !"DAY".equals(unit)) {
                    if ("MONTH".equals(unit) || "YEAR".equals(unit)) {
                        return new OperatorDiagnostic("Invalid unit", "Please use function MONTH_BETWEEN instead");
                    }
                    return new OperatorDiagnostic("Invalid unit", getName() + "timestamp, timestamp, unit (SECOND,MINUTE,HOUR,DAY)");
                }
            }
        }
        return super.validateParameters(imageDomains);

    }

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
	    return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
    public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
        return new ExtendedType(IDomain.NUMERIC,Types.INTEGER,0,0);
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
