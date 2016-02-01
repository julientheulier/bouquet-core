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
package com.squid.core.domain.maths;

import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * Ticket #1190 implements some ANSI functions
 * @author loivd
 * Rand function definition
 */
public class RandOperatorDefinition extends OperatorDefinition {

	public static final String RAND = MathsOperatorRegistry.MATHS_BASE + "RAND";

	public RandOperatorDefinition(String name, String ID) {
		super(name, ID, PREFIX_POSITION, name, IDomain.NUMERIC);
	}

	public RandOperatorDefinition(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
	}

	public RandOperatorDefinition(String name, String ID, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,IDomain.NUMERIC, categoryType);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size() != 0 && imageDomains.size() != 2) {
			return new OperatorDiagnostic("RAND take 0 or 2 parameters only",
					getName());
		}
		return OperatorDiagnostic.IS_VALID;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return ExtendedType.FLOAT;
	}

}
