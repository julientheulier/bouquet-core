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
package com.squid.core.domain.extensions.JSON;

import com.squid.core.domain.DomainNumeric;
import com.squid.core.domain.DomainString;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDiagnostic;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 03/05/16.
 */
public class JSONExtractArrayElementTextOperatorDefinition extends JSONOperatorDefinition {
    public static final String ID = JSONOperatorDefinition.JSON_BASE + "JSON_EXTRACT_ARRAY_ELEMENT_TEXT";

    public JSONExtractArrayElementTextOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }

    public JSONExtractArrayElementTextOperatorDefinition(String name, IDomain domain, int categoryType) {
        super(name, ID, domain, categoryType);
    }

    public JSONExtractArrayElementTextOperatorDefinition(String name, String ID, IDomain domain) {
        super(name, ID, domain);
    }

    public JSONExtractArrayElementTextOperatorDefinition(String name, String ID, IDomain domain, int categoryType) {
        super(name, ID, domain, categoryType);
    }


    @Override
    public ListContentAssistEntry getListContentAssistEntry(){
        if(super.getListContentAssistEntry()==null){
            List <String> descriptions = new ArrayList<String>();
            descriptions.add("");
            descriptions.add("Return the pos_th element in the json element");
            ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
            setListContentAssistEntry(entry);
        }
        return super.getListContentAssistEntry();
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type1 = new ArrayList<IDomain>();
        List type2 = new ArrayList<IDomain>();

        IDomain json = new DomainString();
        json.setContentAssistLabel("json");
        json.setContentAssistProposal("${1:json}");
        type1.add(json);
        poly.add(type1);

        type2 = new ArrayList<IDomain>();
        type2.add(json);
        IDomain index = new DomainNumeric();
        index.setContentAssistLabel("index");
        index.setContentAssistProposal("${2:index}");
        type2.add(index);
        poly.add(type2);

        return poly;
    }

    @Override
    public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
        if (imageDomains.size() >= 1) {
            if (!imageDomains.get(0).isInstanceOf(IDomain.STRING)) {
                return new OperatorDiagnostic("1st parameter of function must be the json field as text", getName());
            }
                if (imageDomains.size() == 2) {
                    if (!imageDomains.get(1).isInstanceOf(IDomain.NUMERIC)) {
                        return new OperatorDiagnostic("Please specify the index number", getName() + "(json, index)");
                    }
                } else {
                    return new OperatorDiagnostic("Invalid number of parameters, please check function definition", getName() + "(json, index)");
                }
            return OperatorDiagnostic.IS_VALID;
        } else {
            return new OperatorDiagnostic("Invalid number of parameters, please check function definition", getName());
        }

    }

    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
            return new ExtendedType(IDomain.STRING, Types.VARCHAR, 0, (types[0].getSize()));
    }

    @Override
    public IDomain computeImageDomain(List<IDomain> imageDomains) {
            return IDomain.STRING;
    }

}