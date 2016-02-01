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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;

/**
 * A Column object: it belongs to a Table object and define a name, description, and ColumnType.
 * @author sfantino
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Column {
	
	private String name = "";
	private String description = "";
	private boolean isNotNullFlag = false;
	
	private ExtendedType type = null;

	@JsonBackReference("table")
	private Table table = null;

	public Column() {
		// TODO Auto-generated constructor stub
	}

	public boolean isPrimaryKey() {
		if (table!=null && table.getPrimaryKey()!=null) {
			return table.getPrimaryKey().contains(this);
		}
		// else
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isNotNullFlag() {
		return isNotNullFlag;
	}

	public void setNotNullFlag(boolean isNotNullFlag) {
		this.isNotNullFlag = isNotNullFlag;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Table getTable() {
		return table;
	}

	/**
	 * Set the owning Table. DO NOT add the column to the table
	 * @param table
	 */
	public void setTable(Table table) {
		this.table = table;
	}

	public ExtendedType getType() {
		return type;
	}

	public void setType(ExtendedType type) {
		this.type = type;
	}
	
	/**
	 * return the IDomain for this Column based on the ColumnType (SQL) definition.
	 * @return
	 */
	@JsonIgnore
	public IDomain getTypeDomain() {
		return getType() != null ? getType().getDomain() : IDomain.UNKNOWN;
	}

	@Override
	public String toString() {
		return "Column [name=" + name + ", table=" + table + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		//result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Column other = (Column) obj;
		if (table!=null && !table.equals(other.table)) {
		    return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		/*
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		*/
		return true;
	}

}
