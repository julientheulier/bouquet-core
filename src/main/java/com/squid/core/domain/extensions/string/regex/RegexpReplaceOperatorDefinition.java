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

import com.squid.core.domain.IDomain;
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
    public List getParametersTypes() {
        List poly = new ArrayList<List>();
        List type = new ArrayList<IDomain>();
        type.add(IDomain.STRING);
        type.add(IDomain.STRING);
        poly.add(type);
        type = new ArrayList<IDomain>(); ;
        type.add(IDomain.STRING);
        type.add(IDomain.STRING);
        type.add(IDomain.STRING);
        poly.add(type);
        return poly;
    }

    @Override
    public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
        String hint = "Invalid number of parameters for " + getName() + "(string, regexp, ...)";
        if (imageDomains.size() < 2) {
            return new OperatorDiagnostic("Invalid number of parameters", hint);
        }
        if (!imageDomains.get(0).isInstanceOf(IDomain.STRING)) {
            return new OperatorDiagnostic("1st parameter must be a string", 0, hint);
        }
        if (!imageDomains.get(1).isInstanceOf(IDomain.STRING)) {
            return new OperatorDiagnostic("2nd parameter must be a string", 1, hint);
        }
        if (imageDomains.size() != 3 || !imageDomains.get(2).isInstanceOf(IDomain.STRING)) {
                return new OperatorDiagnostic("3rd parameter must be a string", 2, hint);
        } else if (imageDomains.size() > 2) {
            return new OperatorDiagnostic("Invalid number of parameters", 2,
                    "Invalid number of parameters for " + getName() + "(string, regexp)");
        }
        return OperatorDiagnostic.IS_VALID;
    }
}
