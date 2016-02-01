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
package com.squid.core.sql.model;

import java.util.Enumeration;
import java.util.Hashtable;

public class Scope implements IScope {
	
	private Scope parent;
	private Hashtable<Object, Object> mapping = new Hashtable<Object, Object>();
	private boolean opaque;
	
	public Scope() {
		//
	}
	
	public Scope(Scope parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.sql2.model.IScope#put(java.lang.Object, java.lang.Object)
	 */
	public void put(Object binding, Object value) throws SQLScopeException {
		if (mapping.containsKey(binding)) {
			if (!value.equals(mapping.get(binding))) {
				throw new SQLScopeException("Binding is already mapped to a different value");
			}
		} else {
			mapping.put(binding,value);
		}
	}

	/**
	 * force to put
	 * @param mexpr
	 * @param referencePiece
	 */
	public void override(Object binding, Object value) {
		mapping.put(binding,value);
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.sql2.model.IScope#contains(java.lang.Object)
	 */
	public boolean contains(Object binding) {
		if (mapping.containsKey(binding)) {
			return true;
		} else if (parent!=null && !opaque) {
			return parent.contains(binding);
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.squid.core.sql2.model.IScope#get(java.lang.Object)
	 */
	public Object get(Object binding) {
		if (binding==null) return null;
		Object value = mapping.get(binding);
		if (value==null && parent!=null && !opaque) {
			return parent.get(binding);
		} else {
			return value;
		}
	}
	
	public Enumeration<Object> enumerateKeys() {
		return mapping.keys();
	}

	/**
	 * return the scope where the binding is defined
	 * @param binding
	 * @return
	 */
	public Scope getDefiningScope(Object binding) {
		if (binding==null) return null;
		Object value = mapping.get(binding);
		if (value!=null) {
			return this;
		}
		else if (value==null&&parent!=null) {
			return parent.getDefiningScope(binding);
		} else {
			return null;
		}
	}

	/**
	 * if opaque is set to true, the scope will hide parent definitions
	 * @param b
	 */
	public void setOpaque(boolean flag) {
		this.opaque = flag;
	}

}
