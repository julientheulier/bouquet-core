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
package com.squid.core.sql.render;

import java.util.ArrayList;

import javax.management.OperationsException;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.sql.db.templates.SkinFactory;

public class OperatorPiece 
implements IPiece, ITypedPiece {

    private OperatorDefinition opDef;
    private IPiece[] params = null;
    private ExtendedType[] types = null;
    private IDomain[] domains = null;
	private ExtendedType type;
	
	public OperatorPiece(OperatorDefinition opDef, IPiece[] params) {
		this.opDef = opDef;
		try {
			this.params = filterNoOp(params);
		} catch (OperationsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initType();
	}
	
	/**
	 * @deprecated
	 * @param opDef
	 * @param params
	 * @param domains
	 */
	public OperatorPiece(OperatorDefinition opDef, IPiece[] params, IDomain[] domains) {
		this.opDef = opDef;
		try {
			this.params = filterNoOp(params);
		} catch (OperationsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.domains = domains;
		initType();
	}
	
	public OperatorPiece(OperatorDefinition opDef, IPiece[] params, ExtendedType[] types) {
		this.opDef = opDef;
		try {
			this.params = filterNoOp(params);
		} catch (OperationsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.types = types;
		this.type = opDef.computeExtendedType(types);// init the resulting type
		// compatibility mode
		this.domains = new IDomain[this.types.length];
		for (int i=0;i<this.types.length;i++) {
			this.domains[i] = this.types[i].getDomain();
		}
	}
	
	@Override
	public ExtendedType getType() {
		return this.type;
	}
	
	/**
	 * @deprecated
	 */
	protected void initType() {
		types = new ExtendedType[params.length];
		for (int i=0;i<types.length;i++) {
			if (params[i] instanceof ITypedPiece) {
				types[i] = ((ITypedPiece)params[i]).getType();
			} else {
				types[i] = ExtendedType.UNDEFINED;
			}
		}
		this.type = opDef.computeExtendedType(types);
	}
	
	private IPiece[] filterNoOp(IPiece[] input) throws OperationsException {
		if(input==null){
			throw new OperationsException("parameters not defined");
		}
		ArrayList<IPiece> temp = new ArrayList<IPiece>();
		for (int i=0;i<input.length;i++) {
			if (input[i]!=NoOpPiece.NOOP) temp.add(input[i]);
		}
		if (temp.size()==input.length) return input;
		else return temp.toArray(new IPiece[temp.size()]);
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		return skin.render(skin,this);
	}
	
	public ExtendedType[] getParamTypes() {
		return types;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public IDomain[] getDomains() {
		return domains;
	}

	public OperatorDefinition getOpDef() {
		return opDef;
	}

	public IPiece[] getParams() {
		return params;
	}
	
	@Override
	public String toString() {
		try {
			return render(SkinFactory.INSTANCE.getDefaultSkin());
		} catch (RenderingException e) {
			return super.toString();
		}
	}

}
