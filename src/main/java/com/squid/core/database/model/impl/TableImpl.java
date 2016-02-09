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
package com.squid.core.database.model.impl;

import java.util.ArrayList;
import java.util.List;

import com.squid.core.database.domain.ProxyTableDomain;
import com.squid.core.database.model.Column;
import com.squid.core.database.model.ForeignKey;
import com.squid.core.database.model.Index;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;
import com.squid.core.database.model.TableType;
import com.squid.core.domain.IDomain;

/**
 * A Table object; it contains a list of Columns; and it is owned by a single Schema.
 * @author sfantino
 *
 */
public class TableImpl implements Table {
	
	private String name = "";
	private String catalog = "";
	private String description = "";
	
	private TableType type;
	
	private Schema schema = null;
	
	private ProxyTableDomain domain = null;
	
	private ArrayList<Column> columns = null;
	private Index pk = null;
	private ArrayList<ForeignKey> fks = null;
	private ArrayList<Index> indexes = null;
	
	public TableImpl() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void refresh() {
		// just do nothing
	}
	
	@Override
	public boolean isStale() {
		return schema!=null?schema.isStale():true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TableType getType() {
        return type;
    }

    public void setType(TableType type) {
        this.type = type;
    }

    public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	
	public List<Column> getColumns() {
		if (this.columns==null) {
			initColumns();
		}
		return this.columns;
	}
	
	@Override
	public void addColumn(Column column) {
		getColumns().add(column);
	}

	protected void initColumns() {
		this.columns = new ArrayList<Column>();
	}

	public Column findColumnByName(String col_name) {
		if (col_name==null) col_name="";
		for (Column col : getColumns()) {
			if (col.getName().equals(col_name)) {
				return col;
			}
		}
		// not found
		return null;
	}
	
	public IDomain getDomain() {
		if (this.domain==null) {
			this.domain = new ProxyTableDomain(this);
		}
		return this.domain;
	}
	
	@Override
	public Index getPrimaryKey() {
		return pk;
	}
	
	@Override
	public void setPrimaryKey(Index pk) {
		this.pk = pk;
	}
	
	public List<ForeignKey> getForeignKeys() {
		if (this.fks==null) {
			initForeignKeys();
		}
		return this.fks;
	}

	protected void initForeignKeys() {
		this.fks = new ArrayList<ForeignKey>();
	}

	public ForeignKey findForeignKeyByName(String fk_name) {
		if (fk_name==null) fk_name="";
		for (ForeignKey fk : getForeignKeys()) {
			if (fk.getName().equals(fk_name)) {
				return fk;
			}
		}
		// not found
		return null;
	}
	
	@Override
	public ForeignKey addForeignKey(ForeignKey fk) {
		ForeignKey check = findForeignKeyByName(fk.getName());
		if (check==null) {
			getForeignKeys().add(fk);
			return fk;
		} else {
			return check;
		}
	}
	
	// indexes

	public List<Index> getIndexes() {
		if (this.indexes==null) {
			initIndexes();
		}
		return this.indexes;
	}
	
	public Index addIndex(Index index) {
		Index check = findIndexByName(index.getName());
		if (check==null) {
			getIndexes().add(index);
			return index;
		} else {
			return check;
		}
	}

	protected void initIndexes() {
		this.indexes = new ArrayList<Index>();
	}

	public Index findIndexByName(String name) {
		if (name==null) name="";
		for (Index index : getIndexes()) {
			if (index.getName().equals(name)) {
				return index;
			}
		}
		// not found
		return null;
	}
	
	@Override
	public String toString() {
		return (this.schema==null?"":this.schema.getName()+".")+getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
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
		TableImpl other = (TableImpl) obj;
		if (catalog == null) {
			if (other.catalog != null)
				return false;
		} else if (!catalog.equals(other.catalog))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (schema == null) {
			if (other.schema != null)
				return false;
		} else if (!schema.equals(other.schema))
			return false;
		return true;
	}
	

}
