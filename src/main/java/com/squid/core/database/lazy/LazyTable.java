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
package com.squid.core.database.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.domain.ProxyTableDomain;
import com.squid.core.database.impl.DatabaseServiceException;
import com.squid.core.database.model.Column;
import com.squid.core.database.model.ForeignKey;
import com.squid.core.database.model.Index;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;
import com.squid.core.database.model.TableType;
import com.squid.core.domain.IDomain;

/**
 * A LazyTable knows how to populate itself
 * @author sfantino
 *
 */
public class LazyTable implements Table {

	private String name = "";
	private String catalog = "";
	private String description = "";
	
	private TableType type;
	
	private Schema schema = null;
	
	private ProxyTableDomain domain = null;
	
	private ArrayList<Column> columns = null;
	private ArrayList<ForeignKey> fks = null;
	private Index pk = null;

	private LazyDatabaseFactory df;
	
	private int column_state = 0;
	private int fk_state = 0;
	private int pk_state = 0;

	protected LazyTable(LazyDatabaseFactory df) {
		this.df = df;
	}
	
	@Override
	public void refresh() {
		synchronized (this) {
			// reset computed properties
			column_state = 0;
			fk_state = 0;
			pk_state = 0;
			columns = null;
			fks = null;
			pk = null;
		}
	}
	
	@Override
	public boolean isStale() {
		return schema!=null?schema.isStale():true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCatalog() {
		return catalog;
	}

	@Override
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public TableType getType() {
        return type;
    }

	@Override
    public void setType(TableType type) {
        this.type = type;
    }

    @Override
	public Schema getSchema() {
		return schema;
	}

	@Override
	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	@Override
	public List<Column> getColumns() throws ExecutionException {
		if (column_state==1) {
			return getColumns_internal();
		} else {
			synchronized (this) {
				if (column_state==0) {
					column_state = 2;
					try {
						this.df.getEngine().populateColumns(Collections.singletonList(this));
					} catch (DatabaseServiceException e) {
						column_state=0;
						throw e;
					}
					column_state = 1;
				} else {
					// already done...
				}
				return getColumns_internal();
			}
		}
	}
	
	private List<Column> getColumns_internal() {
		if (this.columns==null) {
			initColumns();
		}
		return this.columns;
	}
	
	@Override
	public void addColumn(Column column) {
		getColumns_internal().add(column);
	}

	protected void initColumns() {
		this.columns = new ArrayList<Column>();
	}

	@Override
	public Column findColumnByName(String col_name) throws ExecutionException {
		if (col_name==null) col_name="";
		for (Column col : getColumns()) {
			if (col.getName().equals(col_name)) {
				return col;
			}
		}
		// not found
		return null;
	}
	
	@Override
	public List<ForeignKey> getForeignKeys() throws ExecutionException {
		if (fk_state==1) {
			return getForeignKeys_internal();
		} else {
			synchronized (this) {
				if (fk_state==0) {
					fk_state = 2;
					try {
						this.df.getEngine().populateImportedKeys(this);
					} catch (DatabaseServiceException e) {
						fk_state=0;
						throw e;
					}
					fk_state = 1;
				} else {
					// already done...
				}
				return getForeignKeys_internal();
			}
		}
	}
	
	public List<ForeignKey> getForeignKeys_internal() {
		if (this.fks==null) {
			initForeignKeys();
		}
		return this.fks;
	}

	protected void initForeignKeys() {
		this.fks = new ArrayList<ForeignKey>();
	}

	@Override
	public ForeignKey findForeignKeyByName(String fk_name) throws ExecutionException {
		return findForeignKeyByName(getForeignKeys(), fk_name);
	}
	
	public ForeignKey addForeignKey(ForeignKey fk) {
		// check if exists
		ForeignKey check = findForeignKeyByName(getForeignKeys_internal(),fk.getName());
		if (check==null) {
			getForeignKeys_internal().add(fk);
			return fk;
		} else {
			return check;
		}
	}

	private ForeignKey findForeignKeyByName(List<ForeignKey> foreignkeys, String fk_name) {
		if (fk_name==null) fk_name="";
		for (ForeignKey fk : foreignkeys) {
			if (fk.getName().equals(fk_name)) {
				return fk;
			}
		}
		// not found
		return null;
	}

	@Override
	public IDomain getDomain() {
		if (this.domain==null) {
			this.domain = new ProxyTableDomain(this);
		}
		return this.domain;
	}
	
	@Override
	public String toString() {
		return this.schema.getName()+"."+getName();
	}
	
	@Override
	public Index getPrimaryKey() {
		if (pk_state==1) {
			return getPrimaryKey_internal();
		} else {
			synchronized (this) {
				if (pk_state==0) {
					pk_state = 2;
					try {
						this.df.getEngine().populatePK(this);
					} catch (DatabaseServiceException | ExecutionException e) {
						// ok, ignore the PK
					}
					pk_state = 1;
				} else {
					// already done...
				}
				return getPrimaryKey_internal();
			}
		}
	}
	
	public Index getPrimaryKey_internal() {
		return this.pk;
	}
	
	@Override
	public void setPrimaryKey(Index pk) {
		this.pk = pk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		LazyTable other = (LazyTable) obj;
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
		if (type != other.type)
			return false;
		return true;
	}

	
}
