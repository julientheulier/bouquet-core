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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.export.IRawExportSource;
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
      wb = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
      ((SXSSFWorkbook) wb).setCompressTempFiles(true);
    } else {
      wb = new HSSFWorkbook();
    }
    maxRows = getMaxRows();

    dataFormat = wb.createDataFormat();
    sheet = wb.createSheet();
  }

  protected void writeColumnNames(final String[] columnNames) throws SQLException {
    writeNext(columnNames, true, null, null);
  }

  protected static Object getColumnValue(final IJDBCDataFormatter formatter, final ResultSet rs, int colType, int colIndex) throws SQLException, IOException {

    Object value = "";
    Object colValue = rs.getObject(colIndex);
    if (colValue != null) {
      value = formatter.unboxJDBCObject(colValue, colType);
    }
    return value;

  }

  private void writeNext(final Object[] nextLine, final boolean isHeader, String[] columnsFormat, int[] cellsType) {
    Row row = getRow();
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
        cell.setCellType(Cell.CELL_TYPE_STRING);
      }

      if (value instanceof String) {
        cell.setCellValue((String) value);
      } else if (value instanceof Date) {
        cell.setCellValue((Date) value);
      } else if (value instanceof Boolean) {
        cell.setCellValue((Boolean) value);
      } else if (value instanceof Number) {
        String durationFormat = "";
        if (columnsFormat != null && columnsFormat.length > iCell && columnsFormat[iCell] != null) {
          durationFormat = columnsFormat[iCell];
        }
        if (DurationFormatUtils.isDurationFormatPattern(durationFormat)) {
          cell.setCellValue(DurationFormatUtils.format(durationFormat, ((Number) value).doubleValue()));
        } else {
          cell.setCellValue(((Number) value).doubleValue());
        }
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
    int[] cellsType = getCellsType(source);
    if (logger.isDebugEnabled()) {
      logger.debug((source.getColumnTypes().toString()));
    }

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

  private int[] getCellsType(IRawExportSource source) throws SQLException {
    int[] cellTypes = null;
    if (source != null && source.getNumberOfColumns() > 0) {
      cellTypes = new int[source.getNumberOfColumns()];
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
        stringFormat = "0";
        break;

      case Types.DOUBLE:
      case Types.FLOAT:
      case Types.DECIMAL:
        stringFormat = "0.00";
        break;
    }
    return stringFormat;
  }

  private static int getCellTypeFromColumn(int colType) {
    int cellType = Cell.CELL_TYPE_STRING;
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
        cellType = Cell.CELL_TYPE_NUMERIC;
        break;
      case Types.BOOLEAN:
        cellType = Cell.CELL_TYPE_BOOLEAN;
        break;
    }
    return cellType;
  }
}
