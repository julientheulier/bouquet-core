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
package com.squid.core.domain.extensions.string.regex;

import com.squid.core.domain.DomainString;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ListContentAssistEntry;
import com.squid.core.domain.operators.OperatorDiagnostic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 06/05/16.
 */
public class RegexpReplaceOperatorDefinition extends RegexpOperatorDefinition {
    public static final String ID = JSON_BASE + "REGEXP_REPLACE";

    public RegexpReplaceOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }

    public RegexpReplaceOperatorDefinition(String name, IDomain domain,
                                         int categoryType) {
        super(name, ID, domain, categoryType);
    }

    public RegexpReplaceOperatorDefinition(String name, String ID, IDomain domain) {
        super(name, ID, domain);
    }

    public RegexpReplaceOperatorDefinition(String name, String ID, IDomain domain,
                                         int categoryType) {
        super(name, ID, domain, categoryType);
    }

    @Override
    public ListContentAssistEntry getListContentAssistEntry(){
        if(super.getListContentAssistEntry()==null){
            List <String> descriptions = new ArrayList<String>();
            descriptions.add("Replace all the occurence regex_pattern by replace_string in the input_string");
            ListContentAssistEntry entry = new ListContentAssistEntry(descriptions,getParametersTypes());
            setListContentAssistEntry(entry);
        }
        return super.getListContentAssistEntry();
    }

    @Override
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();

        IDomain string1 = new DomainString();
        string1.setContentAssistLabel("input_string");
        string1.setContentAssistProposal("${1:input_string}");


        IDomain string2 = new DomainString();
        string2.setContentAssistLabel("regex_pattern");
        string2.setContentAssistProposal("${2:regex_pattern}");

        IDomain string3 = new DomainString();
        string2.setContentAssistLabel("replace_string");
        string2.setContentAssistProposal("${3:replace_string}");

        type.add(string1);
        type.add(string2);
        type.add(string3);

        poly.add(type);

        return poly;

    }
}
