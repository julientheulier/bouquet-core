package com.squid.core.poi;

import java.util.Enumeration;
import java.util.Properties;

import com.squid.core.jdbc.formatter.DataFormatter;

/**
 * Ticket #1645: Integrate Excel file support in the �export data��.
 *
 * @author phuongtd
 */
public class ExcelSettingsBean extends DataFormatter {

  private static final String SETTING_PREF = "com.squid.thirdparty.poi.excel.prefs";

  /**
   * Default use of the enclosing character. DO NOT CHANGE he default boolean pref to true as it may cause a bug : when default is true & this pref not changed, the csv wizard export shows by default false the first time (but the csv pref wizard works on it side)
   */
  public static final boolean DEFAULT_INSERT_HEADER = true;
  public static final String INSERTHEADER_PREF = SETTING_PREF + "/INSERTHEADER";

  public static final ExcelFile DEFAULT_EXCELFILE = ExcelFile.XLSX;
  public static final String EXCELFILE_PREF = SETTING_PREF + "/EXCELFILE";

  private boolean insertHeader = DEFAULT_INSERT_HEADER;
  private ExcelFile excelFile = DEFAULT_EXCELFILE;

  public ExcelSettingsBean() {
  }

  public boolean isInsertHeader() {
    return insertHeader;
  }

  public void setInsertHeader(boolean insertHeader) {
    this.insertHeader = insertHeader;
  }

  public ExcelFile getExcelFile() {
    return excelFile;
  }

  public void setExcelFile(ExcelFile excelFile) {
    this.excelFile = excelFile;
  }

  public void setPreferences(Properties props) {
    if (props != null) {
      Enumeration<Object> propertiesKeys = props.keys();
      while (propertiesKeys.hasMoreElements()) {
        String key = (String) propertiesKeys.nextElement();
        String value = (String) props.get(key);
        if (key.equals(INSERTHEADER_PREF)) {
          setInsertHeader(new Boolean(value).booleanValue());
        } else if (key.equals(EXCELFILE_PREF)) {
          setExcelFile(ExcelFile.valueOf(value));
        }
      }
    }
  }
}
