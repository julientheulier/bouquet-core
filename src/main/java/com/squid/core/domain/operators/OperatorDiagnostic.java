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

import com.squid.core.domain.IDomain;

/**
 * the result of an Operator validation
 * @author serge fantino
 *
 */
public class OperatorDiagnostic {
	
	public static final OperatorDiagnostic IS_VALID = new OperatorDiagnostic();
	
	private String errorMessage = null;
	private String hint = null;
	private int position = -1;
	private boolean is_valid = true;
	
	public static final OperatorDiagnostic invalidType(int pos, IDomain domain, String expectedType) {
        return new OperatorDiagnostic("argument #"+pos+": Invalid type: "+domain.getName()+" expecting "+expectedType,
                pos);
	}
    
    public static final OperatorDiagnostic invalidType(int pos, IDomain domain, String expectedType, String hint) {
        return new OperatorDiagnostic("argument #"+pos+": Invalid type: "+domain.getName()+" expecting "+expectedType,
                pos, hint);
    }

    public static OperatorDiagnostic unexpectedArgument(int pos, String hint) {
        return new OperatorDiagnostic("argument #"+pos+": Unexpected at that position in expression",
                pos, hint);
    }
	
	public OperatorDiagnostic() {
		this.is_valid = true;
	}
	
	public OperatorDiagnostic(String errorMessage, String hint) {
		this.errorMessage = errorMessage;
		this.hint = hint;
		this.is_valid = false;
	}
	
	public OperatorDiagnostic(String errorMessage, int pos, String hint) {
		this.errorMessage = errorMessage;
		this.hint = hint;
		this.position = pos;
		this.is_valid = false;
	}
	
	public OperatorDiagnostic(String errorMessage, int pos) {
		this.errorMessage = errorMessage;
		this.position = pos;
		this.is_valid = false;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getHint() {
		return hint;
	}
	
	public String getMessage() {
		return errorMessage+(hint!=null?"\n"+hint:"");
	}
	
	public int getPosition() {
		return position;
	}

	public boolean isValid() {
		return is_valid;
	}

}
