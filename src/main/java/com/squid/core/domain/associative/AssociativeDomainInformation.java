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
package com.squid.core.domain.associative;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.OperatorDefinition;

/**
 * if the expression is associative, provides information about how to compose intermediate results
 * @author sergefantino
 *
 */
public class AssociativeDomainInformation {
	
	/**
	 * check if the expression is associative based on its image domain
	 * @param domain
	 * @return
	 */
	public static boolean isAssociative(IDomain image) {
		Object check = image.getAdapter(AssociativeDomainInformation.class);
		return (check!=null && (check instanceof AssociativeDomainInformation));
	}
	
	/**
	 * get the associative operator to use to combine intermediate results
	 * @param image
	 * @return
	 */
	public static OperatorDefinition getAssociativeOperator(IDomain image) {
		Object check = image.getAdapter(AssociativeDomainInformation.class);
		if (check!=null && (check instanceof AssociativeDomainInformation)) {
			AssociativeDomainInformation info = (AssociativeDomainInformation)check;
			return info.getOperator();
		} else {
			return null;
		}
	}

	private OperatorDefinition operator;
	
	public AssociativeDomainInformation(OperatorDefinition operator) {
		this.operator = operator;
	}
	
	public OperatorDefinition getOperator() {
		return operator;
	}
	
}
