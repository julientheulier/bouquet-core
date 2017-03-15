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
package com.squid.core.domain.extensions.cast;

import java.sql.Types;
import java.util.List;

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.domain.vector.VectorDomain;

public class CastOperatorDefinition extends OperatorDefinition {

	public static final String CAST_BASE = "com.squid.domain.operator.cast.";
	public static final String TO_CHAR = CAST_BASE + "TO_CHAR";
	public static final String TO_DATE = CAST_BASE + "TO_DATE";
	public static final String TO_NUMBER = CAST_BASE + "TO_NUMBER";
	public static final String TO_TIMESTAMP = CAST_BASE + "TO_TIMESTAMP";
	public static final String TO_INTEGER = CAST_BASE + "TO_INTEGER";

	public static final int DEFAULT_LENGTH_FOR_STRING = 255;

	public CastOperatorDefinition(String name, String ID, IDomain domain) {
		super(name, ID, PREFIX_POSITION, name, domain);
	}


	public CastOperatorDefinition(String name, String ID, IDomain domain, int categoryName) {
		super(name, ID, PREFIX_POSITION, name, domain);
		this.setCategoryType(categoryName);
	}

	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return ALGEBRAIC_TYPE;
	}



	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
        return IDomain.UNKNOWN;
	}

	public int getPieceLength(ExtendedType[] types) {
		int length = DEFAULT_LENGTH_FOR_STRING;
		int scale = 0;
		int size = 0;
		if (types != null) {
			for (ExtendedType type : types) {
				if (type.getDomain() == IDomain.NUMERIC) {
					scale = type.getScale();
					size = type.getSize() + 1;
					if (scale > 0) {
						size++;
					}
					length = size;
				} else if (type.getDomain() == IDomain.STRING) {
					length = type.getSize();
				}
			}
		}
		return length;
	}
}
