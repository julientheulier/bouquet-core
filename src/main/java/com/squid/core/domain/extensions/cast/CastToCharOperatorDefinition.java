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

import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.domain.vector.VectorDomain;

import java.sql.Types;
import java.util.List;

/**
 * Created by lrabiet on 03/05/16.
 */
public class CastToCharOperatorDefinition extends CastOperatorDefinition {
    public static final String ID = CastOperatorDefinition.CAST_BASE + "TO_CHAR";

    public CastToCharOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }

    public CastToCharOperatorDefinition(String name, IDomain domain,
                                        int categoryType) {
        super(name, ID, domain, categoryType);
    }

    public CastToCharOperatorDefinition(String name, String ID, IDomain domain) {
        super(name, ID, domain);
    }

    public CastToCharOperatorDefinition(String name, String ID, IDomain domain,
                                        int categoryType) {
        super(name, ID, domain, categoryType);
    }

    @Override
    public IDomain computeImageDomain(List<IDomain> imageDomains) {
        if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argumentToCast = imageDomains.get(0);
        boolean is_meta = argumentToCast.isInstanceOf(IDomain.META);
        IDomain computedDomain = argumentToCast;
        computedDomain = IDomain.STRING;
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
    public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
        if (imageDomains.size() > 0 && imageDomains.size() <= 3) {
            if (imageDomains.size() <= 2) {
                if (imageDomains.size() == 2
                        && !imageDomains.get(1)
                        .isInstanceOf(IDomain.STRING)) {
                    return new OperatorDiagnostic(
                            "Invalid type of parameters", getName()
                            + "(any,format)");
                }
            } else {
                return new OperatorDiagnostic("Invalid type of parameters",
                        getName() + "(any,format)");
            }

            return OperatorDiagnostic.IS_VALID;
        }else{
                return new OperatorDiagnostic("Invalid number of parameters",
                        getName());
        }
    }

    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
        return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
    }

    public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
        ExtendedType castExtendedType = null;
        int size = getPieceLength(types);
        if (types.length == 2
                && types[1].getDomain() instanceof DomainStringConstant) {
            size = ((DomainStringConstant) types[1].getDomain()).getValue()
                    .length();
        }
        castExtendedType = new ExtendedType(IDomain.STRING, Types.VARCHAR,
                0, size);

        return castExtendedType;
    }

}
