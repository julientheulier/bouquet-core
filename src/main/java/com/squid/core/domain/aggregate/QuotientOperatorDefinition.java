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
package com.squid.core.domain.aggregate;

import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * The quotient operator provides quotient operation on aggregate, e.g. to compute:
 *  SUM(AMOUNT) ON VOICE_TRAFFIC
 *  COUNT() ON (BID AND SUCCESFULL)
 *  
 *  
 * @author sfantino
 *
 */
public class QuotientOperatorDefinition extends OperatorDefinition {
	
	public static final String ID = AggregateOperatorRegistry.REGISTRY_BASE + ".Quotient";
	
	public static final String ERROR_MESSAGE_ARG_1 = "ON operator applies only to aggregate expression";
	public static final String ERROR_MESSAGE_ARG_2 = "ON operator defines a filter";
	public static final String ERROR_MESSAGE_HINT = "SUM(quantity) ON (quarter=Q1)";

	public QuotientOperatorDefinition() {
		super("ON",ID,INFIX_POSITION," ON ",IDomain.AGGREGATE);
	}

	@Override
	public int getType() {
		return AGGREGATE_TYPE;
	}
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()==2) {
			if (imageDomains.get(0).isInstanceOf(AggregateDomain.DOMAIN) && imageDomains.get(0).isInstanceOf(AggregateDomain.NUMERIC)) {
				if (imageDomains.get(1).isInstanceOf(IDomain.CONDITIONAL)) {
					return OperatorDiagnostic.IS_VALID;
				} else {
					return new OperatorDiagnostic(ERROR_MESSAGE_ARG_2,ERROR_MESSAGE_HINT);
				}
			} else {
				return new OperatorDiagnostic(ERROR_MESSAGE_ARG_1,ERROR_MESSAGE_HINT);
			}
		} else {
			return new OperatorDiagnostic("Invalid number of arguments",ERROR_MESSAGE_HINT);
		}
	}
	
	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		if (types.length==2) {
			return types[0];
		} else {
			return ExtendedType.UNDEFINED;
		}
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()==2) {
			return imageDomains.get(0);
		} else {
			return IDomain.UNKNOWN;
		}
	}
	
}
