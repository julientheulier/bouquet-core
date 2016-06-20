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
package com.squid.core.expression.scope;

/**
 * the result of an expression validation
 * @author serge fantino
 *
 */
public class ExpressionDiagnostic {
	
	public static final ExpressionDiagnostic IS_VALID = new ExpressionDiagnostic();
	
	private String errorMessage = null;
	private boolean is_valid = true;
	private int row = 0;
	private int column = 0;

	public ExpressionDiagnostic() {
		this.is_valid = true;
	}
	
	public ExpressionDiagnostic(String errorMessage) {
		this.errorMessage = errorMessage;
		this.is_valid = false;
	}

	public ExpressionDiagnostic(String errorMessage, int row, int column) {
		this.errorMessage = errorMessage;
		this.is_valid = false;
		this.row = row;
		this.column = column;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getRowError() {
		return row;
	}

	public int getColumnError() {
		return column;
	}

	public boolean isValid() {
		return is_valid;
	}

    public void setIsValid(boolean isValid) {
        is_valid = isValid;
    }
	
	

}
