/*******************************************************************************
 * Copyright © Squid Solutions, 2016
 *
 * This file is part of Open Bouquet software.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 *
 * There is a special FOSS exception to the terms and conditions of the
 * licenses as they are applied to this program. See LICENSE.txt in
 * the directory of this program distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * Squid Solutions also offers commercial licenses with additional warranties,
 * professional functionalities or services. If you purchase a commercial
 * license, then it supersedes and replaces any other agreement between
 * you and Squid Solutions (above licenses and LICENSE.txt included).
 * See http://www.squidsolutions.com/EnterpriseBouquet/
 *******************************************************************************/
package com.squid.core.domain.sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.domain.operators.OperatorRegistry;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.OperatorScopeException;

/**
 * support SORT operators (ASC & DESC)
 * @author Serge Fantino
 *
 */
public class SortOperatorRegistry
implements OperatorRegistry
{

	private final static Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);

	public SortOperatorRegistry(OperatorScope scope) {
		try {
			apply(scope);
			logger.info("init SortOperatorRegistry");
		} catch (OperatorScopeException e) {
			logger.error("unable to init the SortOperatorRegistry", e);
		}
	}

	@Override
	public void apply(OperatorScope scope) throws OperatorScopeException {
		scope.registerExtension(new SortOperatorDefinition("ASC",SortOperatorDefinition.ASC_ID,DomainSort.SortDirection.ASC, DomainSort.NullsPosition.UNDEFINED));
		scope.registerExtension(new SortOperatorDefinition("DESC",SortOperatorDefinition.DESC_ID,DomainSort.SortDirection.DESC, DomainSort.NullsPosition.UNDEFINED));
		scope.registerExtension(new SortOperatorDefinition("ASC_NULLS_FIRST",SortOperatorDefinition.ASC_FIRST_ID,DomainSort.SortDirection.ASC, DomainSort.NullsPosition.NULLS_FIRST));
		scope.registerExtension(new SortOperatorDefinition("DESC_NULLS_FIRST",SortOperatorDefinition.DESC_FIRST_ID,DomainSort.SortDirection.DESC, DomainSort.NullsPosition.NULLS_FIRST));
		scope.registerExtension(new SortOperatorDefinition("ASC_NULLS_LAST",SortOperatorDefinition.ASC_LAST_ID,DomainSort.SortDirection.ASC, DomainSort.NullsPosition.NULLS_LAST));
		scope.registerExtension(new SortOperatorDefinition("DESC_NULLS_LAST",SortOperatorDefinition.DESC_FIRST_ID,DomainSort.SortDirection.DESC, DomainSort.NullsPosition.NULLS_LAST));
	}

}
