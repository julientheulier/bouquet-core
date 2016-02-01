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
package com.squid.core.expression;

import com.squid.core.expression.scope.IdentifierType;

/**
 * define a token position in a formula
 * @author sergefantino
 *
 */
public class TokenPosition {
	
	private int line = 0;
	private int start = 0;
	private int end = 0;
	
	private IdentifierType type = null;
	
	public TokenPosition(int line, int start, int end, IdentifierType type) {
		super();
		this.line = line;
		this.start = start;
		this.end = end;
		this.type = type;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}

	public int length() {
		return end-start+1;
	}
	
	public IdentifierType getType() {
		return type;
	}

}
