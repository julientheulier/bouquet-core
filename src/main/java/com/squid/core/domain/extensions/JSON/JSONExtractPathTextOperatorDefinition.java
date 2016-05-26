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
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDiagnostic;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 03/05/16.
 * http://docs.aws.amazon.com/redshift/latest/dg/JSON_EXTRACT_PATH_TEXT.html
 */
public class JSONExtractPathTextOperatorDefinition extends JSONOperatorDefinition {
    public static final String ID = JSONOperatorDefinition.JSON_BASE + "JSON_EXTRACT_PATH_TEXT";

    public JSONExtractPathTextOperatorDefinition(String name, IDomain domain) {
        super(name, JSONExtractPathTextOperatorDefinition.ID, domain);
    }

    public JSONExtractPathTextOperatorDefinition(String name, IDomain domain, int categoryType) {
        super(name, JSONExtractPathTextOperatorDefinition.ID, domain, categoryType);
    }

    public JSONExtractPathTextOperatorDefinition(String name, String ID, IDomain domain) {
        super(name, ID, domain);
    }

    public JSONExtractPathTextOperatorDefinition(String name, String ID, IDomain domain, int categoryType) {
        super(name, ID, domain, categoryType);
    }

    @Override
    public ListContentAssistEntry getListContentAssistEntry(){
        if(super.getListContentAssistEntry()==null){
            List <String> descriptions = new ArrayList<String>();
            descriptions.add("Return the element corresponding to the key in the json element");
            descriptions.add("Return the elements corresponding to the keys in the json element");
            descriptions.add("Return the elements corresponding to the keys in the json element");
            descriptions.add("Return the elements corresponding to the keys in the json element");
            descriptions.add("Return the elements corresponding to the keys in the json element");
            descriptions.add("Return the elements corresponding to the keys in the json element");
            ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
            setListContentAssistEntry(entry);
        }
        return super.getListContentAssistEntry();
    }


    public List getParametersTypes() { //Up to five level of path_elements.
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain json = new DomainString();
        json.setContentAssistLabel("json");
        json.setContentAssistProposal("${1:json}");
        type.add(json);
        poly.add(type);
        type = new ArrayList<IDomain>(); ;
        IDomain key1 = new DomainString();
        key1.setContentAssistLabel("key1");
        key1.setContentAssistProposal("${2:key1}");
        type.add(json);
        type.add(key1);
        poly.add(type);
        type = new ArrayList<IDomain>(); ;
        IDomain key2 = new DomainString();
        key2.setContentAssistLabel("key2");
        key2.setContentAssistProposal("${3:key2}");
        type.add(json);
        type.add(key1);
        type.add(key2);
        poly.add(type);
        type = new ArrayList<IDomain>(); ;
        IDomain key3 = new DomainString();
        key3.setContentAssistLabel("key3");
        key3.setContentAssistProposal("${4:key3}");
        type.add(json);
        type.add(key1);
        type.add(key2);
        type.add(key3);
        poly.add(type);
        type = new ArrayList<IDomain>(); ;
        IDomain key4 = new DomainString();
        key4.setContentAssistLabel("key4");
        key4.setContentAssistProposal("${5:key4}");
        type.add(json);
        type.add(key1);
        type.add(key2);
        type.add(key3);
        type.add(key4);
        poly.add(type);
        type = new ArrayList<IDomain>(); ;
        IDomain key5 = new DomainString();
        key5.setContentAssistLabel("key5");
        key5.setContentAssistProposal("${6:key5}");
        type.add(json);
        type.add(key1);
        type.add(key2);
        type.add(key3);
        type.add(key4);
        type.add(key5);
        poly.add(type);
        return poly;
    }

    @Override
    public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
        if (imageDomains.size() >= 1) {
            if (!imageDomains.get(0).isInstanceOf(IDomain.STRING)) {
                return new OperatorDiagnostic("1st parameter of function must be the json field as text", getName());
            }
            if (imageDomains.size() >= 2) {
                for (int i = 1; i < imageDomains.size(); i++) {
                    if (!imageDomains.get(i).isInstanceOf(IDomain.STRING)) {
                        return new OperatorDiagnostic("A path is a list of keys", getName() + "(json, key1, ...)");
                    }
                }
            } else {
                return new OperatorDiagnostic("Invalid number of parameters, please check function definition", getName());
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