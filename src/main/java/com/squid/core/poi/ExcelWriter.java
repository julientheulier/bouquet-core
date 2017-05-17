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
package com.squid.core.poi;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.export.IRawExportSource;
import com.squid.core.export.Selection;
import com.squid.core.jdbc.formatter.DurationFormatUtils;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;

/**
 * Ticket #1645: Integrate Excel file support in the 'export data'.
 *
 * @author phuongtd
 *
 */
public class ExcelWriter implements Closeable, Flushable {

	static final Logger logger = LoggerFactory.getLogger(ExcelWriter.class);

	private static final int XLS_MAX_ROWS = 65536;

	private static final int XLSX_MAX_ROWS = 1048576;

	private Workbook wb = null;
	private Sheet sheet;

	private DataFormat dataFormat = null;

	private OutputStream stream;

	private boolean insertHeader;

	private int[] maxCharsInColumns = null;
	private int[] maxRawCharsInColumns = null;
	private DataFormatter formatter = new DataFormatter();

	private Hashtable<String, CellStyle> styles = new Hashtable<String, CellStyle>();

	private int maxRows;
	private int linesWritten = 0;
	private static int ROW_ACCESS_WINDOW_SIZE = 100;

	private ExcelFile excelFile = ExcelFile.XLS;

	public ExcelWriter(OutputStream stream, ExcelSettingsBean settings) {
		this.stream = stream;
		this.insertHeader = settings.isInsertHeader();

		// NPE can occur in XMLBeans but no impact
		if (ExcelFile.XLSX.equals(settings.getExcelFile())) {
			excelFile = ExcelFile.XLSX;
			wb = new SXSSFWorkbook(null, ROW_ACCESS_WINDOW_SIZE, true, true);
		} else {
			wb = new HSSFWorkbook();
		}
		maxRows = getMaxRows();

		dataFormat = wb.createDataFormat();
		sheet = wb.createSheet("Data");
	}

	public void writeSelection(List<Selection> selections) {
		Sheet selectionSheet  = wb.createSheet("Information");
		CellStyle cs = wb.createCellStyle();
		cs.setWrapText(true);
		if (selectionSheet instanceof SXSSFSheet) {
			((SXSSFSheet)selectionSheet).trackAllColumnsForAutoSizing();
			cs.setVerticalAlignment(VerticalAlignment.CENTER);
		} else {
			cs.setVerticalAlignment(VerticalAlignment.CENTER);
		}
		CellStyle csh = getHeaderStyle();
		if (selections != null && selections.size()>0) {
			int rowNr = 0;
			Row headerRow = selectionSheet.createRow(rowNr++);
			Cell cell = headerRow.createCell(0);
			cell.setCellValue("Filter");
			cell.setCellStyle(csh);
			cell = headerRow.createCell(1);
			cell.setCellValue("Selection");
			cell.setCellStyle(csh);
			boolean hasCompare = false;
			for(Selection selection : selections) {
				Row row = selectionSheet.createRow(rowNr++);
				int cellNr = 0;
				cell = row.createCell(cellNr++);
				cell.setCellValue(selection.getName());
				cell.setCellStyle(cs);
				cell = row.createCell(cellNr++);
				String separator = "";
				for (String value: selection.getValues()){
					cell.setCellValue(cell.getStringCellValue() + separator + value);
					separator = " \n";
				}
				row.setHeightInPoints((selection.getValues().size()*sheet.getDefaultRowHeightInPoints()));
				cell.setCellStyle(cs);
				if (selection.getCompared() != null && selection.getCompared().size()>0) {
					hasCompare = true;
					cell = row.createCell(cellNr++);
					separator = "";
					for (String value: selection.getCompared()){
						cell.setCellValue(cell.getStringCellValue() + separator + value);
						separator = " \n";
					}
					cell.setCellStyle(cs);
				}
			}
			if (hasCompare) {
				cell = headerRow.createCell(2);
				cell.setCellValue("Compared with");
				cell.setCellStyle(csh);
			}
			selectionSheet.autoSizeColumn(0);
			selectionSheet.autoSizeColumn(1);
			if (hasCompare) {
				selectionSheet.autoSizeColumn(2);
			}
		}
	}

	private CellStyle getHeaderStyle() {
		CellStyle csh = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		csh.setFont(font);
		return csh;
	}

	protected void writeColumnNames(final String[] columnNames) throws SQLException {
		writeNext(columnNames, true, null, null);
	}

	protected static Object getColumnValue(final IJDBCDataFormatter formatter, final ResultSet rs, int colType,
			int colIndex) throws SQLException, IOException {

		Object value = "";
		Object colValue = rs.getObject(colIndex);
		if (colValue != null) {
			value = formatter.unboxJDBCObject(colValue, colType);
		}
		return value;

	}

	private void writeNext(final Object[] nextLine, final boolean isHeader, String[] columnsFormat, CellType[] cellsType) {
		Row row = getRow();
		CellStyle csh = null;
		if (isHeader) {
			csh = getHeaderStyle();
			if (sheet instanceof SXSSFSheet) {
				csh.setVerticalAlignment(VerticalAlignment.CENTER);
			} else {
				csh.setVerticalAlignment(VerticalAlignment.CENTER);
			}
		}
		for (int iCell = 0; iCell < nextLine.length; iCell++) {
			Cell cell = row.createCell(iCell); // Create cell
			if (isHeader) { // Apply header and footer style
				cell.getRow().setHeight((short) 500);
			}

			Object value = nextLine[iCell];
			String format = "General";
			if (columnsFormat != null && columnsFormat.length > iCell && columnsFormat[iCell] != null) {
				cell.setCellStyle(getStyle(columnsFormat[iCell]));
			} else {
				cell.setCellStyle(getStyle(format));
			}

			// Guess cell data type
			if (cellsType != null && cellsType.length > iCell) {
				cell.setCellType(cellsType[iCell]);
			} else {
				cell.setCellType(CellType.STRING);
			}
			if (value instanceof String) {
				int length = ((String) value).length() + (isHeader? 1:0);
				if (maxCharsInColumns[iCell]<length) {
					maxCharsInColumns[iCell] = length;
					maxRawCharsInColumns[iCell] = length;
				}
				cell.setCellValue((String) value);
			} else if (value instanceof Date) {
				int length = columnsFormat[iCell].length();
				if (maxCharsInColumns[iCell]<length) {
					maxCharsInColumns[iCell] = length;
				}
				cell.setCellValue((Date) value);
			} else if (value instanceof Boolean) {
				if (maxCharsInColumns[iCell]<5) {
					maxCharsInColumns[iCell] = 5;
				}
				cell.setCellValue((Boolean) value);
			} else if (value instanceof Number) {
				String durationFormat = "";
				if (columnsFormat != null && columnsFormat.length > iCell && columnsFormat[iCell] != null) {
					durationFormat = columnsFormat[iCell];
				}
				if (DurationFormatUtils.isDurationFormatPattern(durationFormat)) {
					cell.setCellValue(DurationFormatUtils.format(durationFormat, ((Number) value).doubleValue()));
					int length = columnsFormat[iCell].length();
					if (maxCharsInColumns[iCell]<length) {
						maxCharsInColumns[iCell] = length;
					}
				} else {
					cell.setCellValue(((Number) value).doubleValue());
					int length = ((Number) value).toString().length();
					if (maxRawCharsInColumns[iCell]<length) {
						String result = formatter.formatCellValue(cell);
						maxRawCharsInColumns[iCell] = length;
						maxCharsInColumns[iCell] = result.length();
					}
				}
			}
			if (isHeader) { // Apply header and footer style
				cell.getRow().setHeight((short) 500);
				cell.setCellStyle(csh);
			}

		}
	}

	private CellStyle getStyle(String format) {
		if (!styles.containsKey(format)) {
			CellStyle style = wb.createCellStyle();
			style.setDataFormat(dataFormat.getFormat(format));
			styles.put(format, style);
		}
		return styles.get(format);
	}

	private Row getRow() {

		Row r = sheet.getRow(linesWritten);
		if (r == null) {
			r = sheet.createRow(linesWritten);
		}
		return r;
	}

	private int getMaxRows() {
		int maxRows = XLS_MAX_ROWS;
		switch (excelFile) {
			case XLS:
				maxRows = XLS_MAX_ROWS;
				break;
			case XLSX:
				maxRows = XLSX_MAX_ROWS;
				break;
			default:
				break;
		}
		;
		return maxRows;
	}

	@Override
	public void flush() throws IOException {
		if (wb != null && stream != null) {
			wb.write(stream);
		}
		if (stream != null) {
			stream.flush();
		}

	}

	@Override
	public void close() throws IOException {
		if (stream != null) {
			stream.close();
		}
		dispose();
	}

	public void dispose() throws IOException {
		if (wb instanceof SXSSFWorkbook && wb != null) {
			((SXSSFWorkbook) wb).dispose();
		}
	}

	public long getLinesWritten() {
		return linesWritten;
	}

	public boolean writeResultSet(IRawExportSource source, IJDBCDataFormatter formatter) throws SQLException {
		String[] columnsFormat = getColumnsFormat(source);
		CellType[] cellsType = getCellsType(source);
		maxCharsInColumns = new int[columnsFormat.length];
		maxRawCharsInColumns = new int[columnsFormat.length];
		if (logger.isDebugEnabled()) {
			logger.debug((source.getColumnTypes().toString()));
		}
		/*
		if (sheet instanceof SXSSFSheet) {
			((SXSSFSheet)sheet).trackAllColumnsForAutoSizing();
		}
		 */
		if (insertHeader) {
			writeColumnNames(source.getColumnNames());
			++linesWritten;
		}
		// write data up to split limit
		Iterator<Object[]> iter = source.iterator();
		while (iter.hasNext()) {
			Object[] rawRow = iter.next();
			if (rawRow != null) {
				writeNext(rawRow, false, columnsFormat, cellsType);
				++linesWritten;
			}
			if (linesWritten >= maxRows) {
				break;
			}
		}
		for (int i=0; i<source.getColumnNames().length; i++) {
			//int width = ((maxCharsInColumns[i]*defaultCharWidth+5)/defaultCharWidth*256);
			int width = maxCharsInColumns[i] * 256;
			sheet.setColumnWidth(i, Math.min(width, 40*256));
		}
		return true;

	}

	private String[] getColumnsFormat(IRawExportSource source) throws SQLException {
		String[] columnsFormat = null;
		if (source != null && source.getNumberOfColumns() > 0) {
			columnsFormat = new String[source.getNumberOfColumns()];
			for (int i = 0; i < source.getNumberOfColumns(); i++) {
				columnsFormat[i] = ExcelWriter.getExcelFormatFromColumn(source.getColumnType(i));
			}
		}
		return columnsFormat;
	}

	private CellType[] getCellsType(IRawExportSource source) throws SQLException {
		CellType[] cellTypes = null;
		if (source != null && source.getNumberOfColumns() > 0) {
			cellTypes = new CellType[source.getNumberOfColumns()];
			for (int i = 0; i < source.getNumberOfColumns(); i++) {
				cellTypes[i] = ExcelWriter.getCellTypeFromColumn(source.getColumnType(i));
			}
		}
		return cellTypes;
	}

	private static String getExcelFormatFromColumn(int colType) {
		String stringFormat = null;
		switch (colType) {

			case Types.DATE:
				stringFormat = "yyyy-mm-dd";
				break;
			case Types.TIME:
				stringFormat = "hh:mm:ss";
				break;
			case Types.TIMESTAMP:
				stringFormat = "yyyy-mm-dd hh:mm:ss";
				break;
			case Types.TINYINT:
			case Types.SMALLINT:
			case Types.INTEGER:
			case Types.BIGINT:
			case Types.NUMERIC:
				stringFormat = HSSFDataFormat.getBuiltinFormat((short) 3);
				break;

			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.DECIMAL:
				stringFormat = HSSFDataFormat.getBuiltinFormat((short) 4);
				break;
		}
		return stringFormat;
	}

	private static CellType getCellTypeFromColumn(int colType) {
		CellType cellType = CellType.STRING;
		switch (colType) {

			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
			case Types.TINYINT:
			case Types.SMALLINT:
			case Types.INTEGER:
			case Types.BIGINT:
			case Types.NUMERIC:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.DECIMAL:
				cellType = CellType.NUMERIC;
				break;
			case Types.BOOLEAN:
				cellType = CellType.BOOLEAN;
				break;
		}
		return cellType;
	}
}
