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
package com.squid.core.domain.operators;

/**
 * operator definition for AND & OR operators
 * @author Serge Fantino
 *
 */
public class OrAndConditionalOperatorDefinition extends
		BinaryLogicalOperatorDefinition {

	public OrAndConditionalOperatorDefinition(String name, int id, int position) {
		super(name, id, position);
		// TODO Auto-generated constructor stub
	}

	public OrAndConditionalOperatorDefinition(String name, int id, String symbol) {
		super(name, id, symbol);
		// TODO Auto-generated constructor stub
	}


	public OrAndConditionalOperatorDefinition(String name, int id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String prettyPrint(String symbol, int position, String[] args, boolean showBrackets) {
		// TODO Auto-generated method stub
		return super.prettyPrint(" "+symbol+" ", position, args, showBrackets);
	}

}
