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
package com.squid.core.domain.extensions.string.translate;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.extensions.registry.StringFunctionsRegistry;

/**
 * Created by lrabiet on 06/05/16.
 */
public class TranslateReplaceOperatorDefinition extends TranslateOperatorDefinition {

    public static final String ID = StringFunctionsRegistry.STRING_BASE + "REPLACE";

    public TranslateReplaceOperatorDefinition(String name, IDomain domain) {
        super(name, ID, domain);
    }

    public TranslateReplaceOperatorDefinition(String name, IDomain domain,
                                         int categoryType) {
        super(name, ID, domain, categoryType);
    }

    public TranslateReplaceOperatorDefinition(String name, String ID, IDomain domain) {
        super(name, ID, domain);
    }

    public TranslateReplaceOperatorDefinition(String name, String ID, IDomain domain,
                                         int categoryType) {
        super(name, ID, domain, categoryType);
    }
}
