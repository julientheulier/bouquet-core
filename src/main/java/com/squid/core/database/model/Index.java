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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Index {
	
	private String name;
	
	private ArrayList<ColumnPos> columns = new ArrayList<ColumnPos>();
	
	public Index(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<ColumnPos> getColumnPos() {
		return columns;
	}
	
	@JsonIgnore
	public List<Column> getColumns() {
		ArrayList<Column> copy = new ArrayList<Column>(columns.size());
		for (ColumnPos pos : columns) {
			copy.add(pos.getColumn());
		}
		return copy;
	}
	
	public void addColumn(Column column, int pos) {
		addColumn(new ColumnPos(column,pos));
	}

	public void addColumn(ColumnPos columnPos) {
		ArrayList<ColumnPos> copy = new ArrayList<ColumnPos>(this.columns);
		copy.add(columnPos);
		if (copy.size()>1) {
			Collections.sort(copy, new Comparator<ColumnPos>() {
				@Override
				public int compare(ColumnPos o1, ColumnPos o2) {
					return o1.getPos()-o2.getPos();
				}
			});
		}
		this.columns = copy;
	}

	public boolean contains(Column column) {
		for (ColumnPos pos : columns) {
			if (pos.getColumn().equals(column)) {
				return true;
			}
		}
		// else
		return false;
	}

}
