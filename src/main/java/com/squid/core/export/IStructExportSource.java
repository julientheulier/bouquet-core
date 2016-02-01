package com.squid.core.export;

import java.util.List;

public interface IStructExportSource {
	
	public Iterable<IRow> getRows();	
	public List<ICol> getCols();
}
