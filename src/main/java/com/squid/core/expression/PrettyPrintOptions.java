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

import com.squid.core.domain.IDomain;

/**
 * This class allows to customize the ExpressionAST.prettyPrint() output.
 * @version 4.2.24
 * @author sergefantino
 *
 */
public class PrettyPrintOptions {
	
	public enum ReferenceStyle {
		LEGACY,// compatible with prettyPrint() prior to 4.2.24 - this is the default
		NAME,// just use the name
		IDENTIFIER// use canonical ID
	}
	
	// constant option for HUMAN display
	public static final PrettyPrintOptions HUMAN_GLOBAL = new PrettyPrintOptions(ReferenceStyle.NAME, null);
	// constant option for ROBOT reference
	public static final PrettyPrintOptions ROBOT_GLOBAL = new PrettyPrintOptions(ReferenceStyle.IDENTIFIER, null);
	
	private ReferenceStyle style = ReferenceStyle.LEGACY;
	
	private boolean explicitType = false;
	
	private IDomain scope = null; // default is global scope == null
	
	public PrettyPrintOptions() {
	}
	
	public PrettyPrintOptions(PrettyPrintOptions copy) {
		super();
		this.style = copy.style;
		this.explicitType = copy.explicitType;
		this.scope = copy.scope;
	}

	public PrettyPrintOptions(ReferenceStyle style, IDomain scope) {
		super();
		this.style = style;
		this.scope = scope;
	}

	public ReferenceStyle getStyle() {
		return style;
	}

	public IDomain getScope() {
		return scope;
	}

	/**
	 * if true the prettyPrinter must explicitly type the identifier (e.g; @explicitType:'identifier')
	 * @return
	 */
	public boolean isExplicitType() {
		return explicitType;
	}

	public PrettyPrintOptions setExplicitType(boolean explicitType) {
		this.explicitType = explicitType;
		return this;
	}

}
