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
package com.squid.core.database.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.Table;

public class PartitionInfo {

    private Table table;
    private HashSet<Column> keys = new HashSet<>();
    private List<PartitionTable> partitions = new ArrayList<>();

    public PartitionInfo(Table table) {
        this.table = table;
    }
    
    public boolean hasPartition() {
        return !keys.isEmpty();
    }
    
    public Table getTable() {
        return table;
    }
    
    public boolean hasKeys() {
        return !keys.isEmpty();
    }

    public void addPartitionKey(String columnname) throws ExecutionException {
        Column column = table.findColumnByName(columnname);
        if (column!=null) {
            keys.add(column);
        }
    }
    
    public Collection<Column> getKeys() {
        return keys;
    }

    public boolean isPartitionKey(Column column) {
        return keys.contains(column);
    }

    public List<PartitionTable> getPartitionTables() {
        return partitions;
    }

    public void addPartitionTable(String tablename, Object rangeStart, Object rangeEnd) throws ExecutionException {
        Table partition = table.getSchema().findTable(tablename);
        if (partition!=null) {
            partitions.add(new PartitionTable(partition, rangeStart, rangeEnd));
        }
    }

}
