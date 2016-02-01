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
package com.squid.core.database.model;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.squid.core.domain.DomainProduct;
import com.squid.core.domain.IDomain;

public class ForeignKey {
	
	private String name;
	
	private Table pkTable;
	private Table fkTable;
	private List<KeyPair> keys = new LinkedList<KeyPair>();

	@JsonIgnore
	private IDomain domain ;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonBackReference("primary")
	public Table getPrimaryTable() {
		return pkTable;
	}
	
	public Table getForeignTable() {
		return fkTable;
	}

	public List<KeyPair> getKeys() {
		return keys;
	}
	
	public boolean add(Column primary, Column exported, int pos) {
		if (pkTable==null) {
			pkTable = primary.getTable();
			domain = null;
		} else if (!pkTable.equals(primary.getTable())) {
			return false;
		}
		if (fkTable==null) {
			fkTable = exported.getTable();
			domain = null;
		} else if (!fkTable.equals(exported.getTable())) {
			return false;
		}
		KeyPair pair = new KeyPair(primary, exported, pos);
		return keys.add(pair);
	}

	public IDomain getDomain() {
		if (domain==null) {
			domain = pkTable==null?IDomain.UNKNOWN:fkTable==null?IDomain.UNKNOWN:
				DomainProduct.createDomain(
				getPrimaryTable().getDomain(),
				getForeignTable().getDomain());
		}
		// else
		return domain;
	}

	@Override
	public String toString() {
		return "ForeignKey [name=" + name + ", keys=" + keys + ", domain="
				+ getDomain() + "]";
	}
	
}
