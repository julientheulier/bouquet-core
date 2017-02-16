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
package com.squid.core.domain.extensions.cast;

import com.squid.core.domain.DomainAny;
import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.domain.vector.VectorDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 03/05/16.
 */
public class CastToIntegerOperatorDefinition extends CastOperatorDefinition {
    public static final String ID = CastOperatorDefinition.CAST_BASE + "TO_INTEGER";

    public CastToIntegerOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
        this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);
    }

    public CastToIntegerOperatorDefinition(String name, String ID, IDomain domain) {
        super(name, ID, domain);
        this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);
    }

    @Override
    public IDomain computeImageDomain(List<IDomain> imageDomains) {
        if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argumentToCast = imageDomains.get(0);
        if(argumentToCast.isInstanceOf(IDomain.ANY)){
            return IDomain.NUMERIC;
        }
        boolean is_meta = argumentToCast.isInstanceOf(IDomain.META);
        IDomain computedDomain = IDomain.NUMERIC;
        if (is_meta) {
            IDomainMetaDomain meta = (IDomainMetaDomain) argumentToCast;
            IDomain proxy = meta.createMetaDomain(computedDomain);
            if (proxy instanceof VectorDomain) {
                ((VectorDomain) proxy).setSize(imageDomains.size());
            }
            return proxy;
        } else {
            return computedDomain;
        }
    }

    @Override
    public List<String> getHint() {
        List<String> hint = new ArrayList<String>();
        hint.add("Cast first argument to integer");
        return hint;
    }


    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();
        IDomain any = new DomainAny();
        type.add(any);
        poly.add(type);

        return poly;
    }

    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
        return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
    }

    public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
        return ExtendedType.INTEGER;
    }

}
