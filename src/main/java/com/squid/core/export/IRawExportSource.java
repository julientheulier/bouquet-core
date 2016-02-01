package com.squid.core.export;

import java.util.Iterator;

import org.apache.avro.Schema;


public interface IRawExportSource extends Iterable<Object[]> {
	
	
	public int getNumberOfColumns();
	
	public String getColumnName(int pos);

	public int getColumnType(int pos) ;

	public int[] getColumnTypes() ;

	public String[] getColumnNames();
	
	public Iterator<Object[]> iterator();
		
	public Schema getSchema();
	
}
