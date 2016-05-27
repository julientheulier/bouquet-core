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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 03/05/16.
 */
public class CastToTimestampOperatorDefinition extends CastOperatorDefinition {
    public static final String ID = CastOperatorDefinition.CAST_BASE + "TO_TIMESTAMP";

    public CastToTimestampOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }

    public CastToTimestampOperatorDefinition(String name, IDomain domain,
                                        int categoryType) {
        super(name, ID, domain, categoryType);
    }

    public CastToTimestampOperatorDefinition(String name, String ID, IDomain domain) {
        super(name, ID, domain);
    }

    public CastToTimestampOperatorDefinition(String name, String ID, IDomain domain,
                                        int categoryType) {
        super(name, ID, domain, categoryType);
    }

    @Override
    public IDomain computeImageDomain(List<IDomain> imageDomains) {
        if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
        IDomain argumentToCast = imageDomains.get(0);
        boolean is_meta = argumentToCast.isInstanceOf(IDomain.META);
        IDomain computedDomain = IDomain.TIMESTAMP;
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
            descriptions.add("Cast the date to timestamp");
            descriptions.add("Cast the string to timestamp using the format");
            ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
            setListContentAssistEntry(entry);
        }
        return super.getListContentAssistEntry();
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain date1 = new DomainDate();
        date1.setContentAssistLabel("date");
        date1.setContentAssistProposal("${1:date}");
        IDomain string1 = new DomainString();
        string1.setContentAssistLabel("string");
        string1.setContentAssistProposal("${1:string}");
        IDomain string2 = new DomainString();
        string2.setContentAssistLabel("format");
        string2.setContentAssistProposal("${2:format}");
        type.add(date1);
        poly.add(type);

        type = new ArrayList<IDomain>();
        type.add(string1);
        type.add(string2);
        poly.add(type);

        return poly;
    }

    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
        return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
    }

    public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
        return ExtendedType.TIMESTAMP;
    }

}
