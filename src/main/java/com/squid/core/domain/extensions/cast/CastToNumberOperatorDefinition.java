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

import com.squid.core.domain.DomainNumericConstant;
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
public class CastToNumberOperatorDefinition extends CastOperatorDefinition {
    public static final String ID = CastOperatorDefinition.CAST_BASE + "TO_NUMBER";

    public CastToNumberOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }

    public CastToNumberOperatorDefinition(String name, IDomain domain,
                                        int categoryType) {
        super(name, ID, domain, categoryType);
    }

    public CastToNumberOperatorDefinition(String name, String ID, IDomain domain) {
        super(name, ID, domain);
    }

    public CastToNumberOperatorDefinition(String name, String ID, IDomain domain,
                                        int categoryType) {
        super(name, ID, domain, categoryType);
    }

    @Override
    public IDomain computeImageDomain(List<IDomain> imageDomains) {
        if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argumentToCast = imageDomains.get(0);
        boolean is_meta = argumentToCast.isInstanceOf(IDomain.META);
        IDomain computedDomain = IDomain.CONTINUOUS;
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
            if (imageDomains.get(0).isInstanceOf(IDomain.STRING) == false
                    && imageDomains.get(0).isInstanceOf(IDomain.NUMERIC) == false) {
                return new OperatorDiagnostic(
                        "Invalid type of parameters",
                        getName()
                                + ": first parameter must be a numeric or a string");
            }
            if (imageDomains.size() == 2
                    && imageDomains.get(1) != IDomain.STRING) {
                return new OperatorDiagnostic(
                        "Invalid number of parameters", getName()
                        + "(any,format)");
            } else if (imageDomains.size() == 3) {
                if (!(imageDomains.get(1) instanceof DomainNumericConstant)
                        || !(imageDomains.get(2) instanceof DomainNumericConstant)) {
                    return new OperatorDiagnostic(
                            "Invalid number of parameters", getName()
                            + "(any,size,precision)");
                } else {
                    double d1 = ((DomainNumericConstant) imageDomains
                            .get(1)).getValue();
                    double d2 = ((DomainNumericConstant) imageDomains
                            .get(2)).getValue();
                    if (Math.floor(d1) != d1 || Math.floor(d2) != d2) {
                        return new OperatorDiagnostic(
                                "Invalid parameters size and/or precision, they must be integer",
                                getName() + "(any,size,precision)");
                    }
                }
            } else if (imageDomains.size() == 4
                    && (imageDomains.get(1) != IDomain.NUMERIC
                    || imageDomains.get(2) != IDomain.NUMERIC || imageDomains
                    .get(3) != IDomain.STRING)) {
                return new OperatorDiagnostic(
                        "Invalid number of parameters", getName()
                        + "(any,size,precision, format)");
            } else if (imageDomains.size() > 4) {
                return new OperatorDiagnostic(
                        "Invalid number of parameters", getName()
                        + "(any,size,precision, format)");
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
        ExtendedType castExtendedType = ExtendedType.FLOAT;
        if (types.length == 3) {
            int size = 15;
            int precision = 0;
            if (types[1].getDomain() instanceof DomainNumericConstant) {
                Double d = ((DomainNumericConstant) types[1].getDomain())
                        .getValue();
                if (d != Double.NaN) {
                    size = d.intValue();
                }
            }
            if (types[2].getDomain() instanceof DomainNumericConstant) {
                Double d = ((DomainNumericConstant) types[2].getDomain())
                        .getValue();
                if (d != Double.NaN) {
                    precision = d.intValue();
                }
            }
            castExtendedType = new ExtendedType(IDomain.NUMERIC,
                    Types.DECIMAL, precision, size);
        }

        return castExtendedType;
    }

}
