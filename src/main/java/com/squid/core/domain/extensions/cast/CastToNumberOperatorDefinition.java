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

import com.squid.core.domain.*;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.domain.vector.VectorDomain;

import java.sql.Types;
import java.util.ArrayList;
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
    public ListContentAssistEntry getListContentAssistEntry(){
        if(super.getListContentAssistEntry()==null){
            List <String> descriptions = new ArrayList<String>();
            descriptions.add("Cast the number to number using the format");
            descriptions.add("Cast the string to number using the format");
            descriptions.add("Cast the number to number using the size and the precision");
            descriptions.add("Cast the string to number using the size and the precision");
            descriptions.add("Cast the number to number using the size, the precision and the format");
            descriptions.add("Cast the string to number using the size, the precision and the format");

            ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
            setListContentAssistEntry(entry);
        }
        return super.getListContentAssistEntry();
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain num1 = new DomainNumeric();
        num1.setContentAssistLabel("Num");
        num1.setContentAssistProposal("${1:n}");
        IDomain string1 = new DomainString();
        string1.setContentAssistLabel("String");
        string1.setContentAssistProposal("${1:s}");
        IDomain string2 = new DomainNumeric();
        string2.setContentAssistLabel("format");
        string2.setContentAssistProposal("${2:format}");
        IDomain num2 = new DomainNumeric();
        num2.setContentAssistLabel("size");
        num2.setContentAssistProposal("${2:size}");
        IDomain num3 = new DomainNumeric();
        num3.setContentAssistLabel("precision");
        num3.setContentAssistProposal("${3:precision}");
        IDomain string4 = new DomainNumeric();
        string4.setContentAssistLabel("format");
        string4.setContentAssistProposal("${4:format}");


        type.add(num1);
        type.add(string2);

        poly.add(type);
        type = new ArrayList<IDomain>();

        type.add(string1);
        type.add(string2);


        poly.add(type);
        type = new ArrayList<IDomain>();

        type.add(num1);
        type.add(num2);
        type.add(num3);

        poly.add(type);
        type = new ArrayList<IDomain>();

        type.add(string1);
        type.add(num2);
        type.add(num3);

        poly.add(type);
        type = new ArrayList<IDomain>();


        type.add(num1);
        type.add(num2);
        type.add(num3);
        type.add(string4);

        poly.add(type);
        type = new ArrayList<IDomain>();

        type.add(string1);
        type.add(num2);
        type.add(num3);
        type.add(string4);

        poly.add(type);

        return poly;
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
