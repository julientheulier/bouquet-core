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
package com.squid.core.domain;

import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.sql.render.SQLSkin;

/**
 * The base implementation
 * @author serge fantino
 *
 */
public abstract class DomainBase 
extends AbstractSingletonDomain
{
	
	private String name = "";
	private IDomain parent = null;

	private String label = "";
	private String proposal = "";

    public DomainBase() {
        // on purpose
    }
    
    public DomainBase(IDomain parent) {
        this();
        setParentDomain(parent);
    }

	public ExtendedType computeType(SQLSkin skin) {
		return skin.createExtendedType(this);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IDomain getParentDomain() {
		return this.parent;
	}

	public void setParentDomain(IDomain parent) {
		this.parent = parent;
	}

	@Override
	public String getContentAssistLabel(){
		if(label==""){
			setContentAssistLabel(getName()+" "+getName().toLowerCase().charAt(0));
		}
		return label;
	}

	@Override
	public void setContentAssistLabel(String label){
		this.label=label;
	}

	@Override
	public String getContentAssistProposal(){
		if(proposal == ""){
			setContentAssistProposal(String.valueOf(getName().toLowerCase().charAt(0)));
		}
		return proposal;
	}

	@Override
	public void setContentAssistProposal(String proposal){
		this.proposal=proposal;
	}
}
