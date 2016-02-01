package com.squid.core.csv;

/**
 Copyright 2005 Bytecode Pty Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

/**
 * major revision by Squid Solutions
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.export.IRawExportSource;
import com.squid.core.jdbc.formatter.IJDBCDataFormatter;

/**
 * A very simple CSV writer released under a commercial-friendly license.
 *
 * @author Glen Smith
 *
 */
public class CSVWriter {

    static final Logger logger = LoggerFactory.getLogger(CSVWriter.class);

	private char separator;

	private char quotechar;

	private char escapechar;

	private String fileEncoding;

	private String lineEnd;

	private boolean useEnclosing;

	private boolean insertHeader;

	private boolean handleSurrogateCharacters = false;

    private boolean computeStatistics = false;

	private int[] statistics = null;

	//protected Boolean[] isStringLikeColumn;

	private boolean doubleEnclose = false;
	
	private long linesWritten;

	public CSVWriter(CSVSettingsBean settings) {
		// perform a local copy for performances ?
		this.separator = settings.getSeparator();
		this.quotechar = settings.getQuotechar();
		this.escapechar = settings.getEscapechar();
		this.lineEnd = settings.getLineEnd();
		this.useEnclosing = settings.isUseEnclosing();
		this.insertHeader = settings.isInsertHeader();
		this.fileEncoding = settings.getFileEncoding();
		this.doubleEnclose = settings.isDoubleEnclose();
	}

	/**
	 * Writes the entire ResultSet to a CSV file.
	 *
	 * The caller is responsible for closing the ResultSet.
	 *
	 * @param rs the recordset to write
	 * @param includeColumnNames true if you want column names in the output, false otherwise
	 * @param monitor
	 *
	 */
	public List<String> writeAll(String fileFullName, int splitSize, java.sql.ResultSet rs, int[] normalizedTypes, IJDBCDataFormatter formatter)  throws SQLException, IOException {
		File file = new File(fileFullName);
		String fileName = file.getAbsolutePath();
		String fileExtention = "";
		if (fileName.lastIndexOf(".")!=-1) {
			fileExtention = fileName.substring(fileName.lastIndexOf("."));
			fileName = fileName.substring(0,fileName.lastIndexOf("."));
		}
		int index = 0;
		ArrayList<String> manifest = new ArrayList<String>();
		while (writeStep(index++, fileName, fileExtention, splitSize, manifest, rs,normalizedTypes, formatter)) {
			// split files
		}
		return manifest;
	}

	protected boolean writeStep(int index, String fileName, String fileExtension, int splitSize, List<String> manifest, java.sql.ResultSet rs,int[] normalizedTypes, IJDBCDataFormatter formatter) throws IOException, SQLException {
		Writer writer = null;
		try {
			String singleFile = genFileNamePart(index, fileName, fileExtension, splitSize);
			logger.info("write CSV to file "+singleFile);
			writer = openFile(singleFile);
			manifest.add(singleFile);
			resetLinesWritten();
		
			return writeResultSet(writer, splitSize, rs, normalizedTypes, formatter);
		} finally {
			if (writer!=null) {
				writer.flush();
				writer.close();
			}
		}
	}

	protected String genFileNamePart(int index, String fileName, String fileExtension, int splitSize) {
		return splitSize>0?(fileName+"_"+index+fileExtension):(fileName+fileExtension);
	}

	protected Writer openFile(String fileName) throws IOException {
		OutputStream stream = new FileOutputStream(fileName);
		if (fileName.endsWith(".gzip")||fileName.endsWith(".gz")) {
			stream = new GZIPOutputStream(stream);
		}
		OutputStreamWriter writer = new OutputStreamWriter(stream, fileEncoding);
		return new PrintWriter(writer);
	}

	/**
	 * write data to writer up to the split limit if defined; return true if there are still some data to output
	 * @param writer
	 * @param rs
	 * @param formatter
	 * @throws SQLException
	 * @throws IOException
	 */
	public boolean writeResultSet(Writer writer, int splitSize, java.sql.ResultSet rs,int[] normalizedTypes, IJDBCDataFormatter formatter)  throws SQLException, IOException {
		ResultSetMetaData metadata = rs.getMetaData();
		
		int columnCount =  metadata.getColumnCount();

		for (int i=0;i<columnCount;i++) {
			logger.info(metadata.getColumnLabel(i+1) +"  "+ metadata.getColumnName(i+1) + " " + metadata.getColumnTypeName(i+1));
		}
		
		int[] columnTypes = normalizedTypes;

		if(logger.isDebugEnabled()){logger.debug((columnTypes.toString()));}

		boolean[] isStringLikeColumn = new boolean[columnCount];// ???
		for (int i=0;i<columnCount;i++) {
			isStringLikeColumn[i] = isColumnStringLike(columnTypes[i]);
		}

		// write header if require, even if the resultset is empty
        if (computeStatistics && statistics==null) {
            statistics = new int[columnCount];
        }
		if (insertHeader ) {
			writer.write(writeColumnNames(metadata, isStringLikeColumn));
		}
		// write data up to split limit
		while (rs.next())
		{
			++linesWritten;
			String[] nextLine = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				Object value = rs.getObject(i+1);
				nextLine[i] = value!=null?formatter.formatJDBCObject(value, columnTypes[i]):"";
			}
			writer.write(formatLine(nextLine, false, isStringLikeColumn).toString());
			if (linesWritten==splitSize && splitSize>0) {
				logger.info("wrote "+linesWritten+" to file, reach split limit");
				return true;// still some data to write
			}
			if (linesWritten%1000000==0) {
				writer.flush();
				if(logger.isDebugEnabled()){logger.debug(("flush "+linesWritten));}
			}
		}
		// done
		if(logger.isDebugEnabled()){logger.debug(("wrote "+linesWritten+" to file, reach end of file"));}
		return false;
	}

	
	public boolean writeResultSet(Writer writer, int splitSize, IRawExportSource source, IJDBCDataFormatter formatter) throws SQLException, IOException{
		
		
		int columnCount = source.getNumberOfColumns();

		if(logger.isDebugEnabled()){logger.debug((source.getColumnTypes().toString()));}

		boolean[] isStringLikeColumn = new boolean[columnCount];// ???
		for (int i=0;i<columnCount;i++) {
			isStringLikeColumn[i] = isColumnStringLike(source.getColumnType(i));
		}

		// write header if require, even if the resultset is empty
        if (computeStatistics && statistics==null) {
            statistics = new int[columnCount];
        }
		if (insertHeader ) {
			
			writer.write(writeColumnNames(source.getColumnNames(), isStringLikeColumn));
		}
		// write data up to split limit
		Iterator<Object[]> iter = source.iterator();
		
		while (iter.hasNext())
		{
			++linesWritten;
			
			Object[] rawRow = iter.next();
			if (rawRow !=null){
				String[] nextLine = new String[columnCount];			
				for (int i = 0; i < columnCount; i++) {
					Object value = rawRow[i];
					nextLine[i] = value!=null?formatter.formatJDBCObject(value, source.getColumnType(i)):"";
				}
				writer.write(formatLine(nextLine, false, isStringLikeColumn).toString());
				if (linesWritten==splitSize && splitSize>0) {
					logger.info("wrote "+linesWritten+" to file, reach split limit");
					return true;// still some data to write
				}
				if (linesWritten%1000000==0) {
					writer.flush();
					if(logger.isDebugEnabled()){logger.debug(("flush "+linesWritten));}
				}
			}
		}
		// done
		if(logger.isDebugEnabled()){logger.debug(("wrote "+linesWritten+" to file, reach end of file"));}
		return false;
		
		
		
		
	}
	
	
	protected String writeColumnNames(String[] columnNames, boolean[] isStringLikeColumn ){
		return formatLine(columnNames, true, isStringLikeColumn).toString();
	}
	
	
	
	protected String writeColumnNames(ResultSetMetaData metadata, boolean[] isStringLikeColumn) throws SQLException {
		int columnCount =  metadata.getColumnCount();
		String[] nextLine = new String[columnCount];
		for (int i = 0; i < columnCount; i++) {
			nextLine[i] = metadata.getColumnName(i + 1).toLowerCase();
		}
		return formatLine(nextLine, true, isStringLikeColumn).toString();
	}

	protected boolean append(boolean isStringLikeColumn, char at) {
        // Remove UTF-16 chars, not supported in RS
        return handleSurrogateCharacters || !isStringLikeColumn || (!Character.isSurrogate(at));
	}

	/**
	 * format the data as CSV
	 * @param nextLine
	 * @param isHeader
	 * @return
	 */
	protected StringBuffer formatLine(String[] nextLine, boolean isHeader, boolean[] isStringLikeColumn) {
		StringBuffer sb = new StringBuffer();
		if (nextLine == null) {
			return null;
		}

		for (int i = 0; i < nextLine.length; i++) {

			if (i != 0) {
				sb.append(separator);
			}

			String nextElement = nextLine[i];
			if (nextElement == null) {
				continue;
			} else if (!isHeader && computeStatistics && isStringLikeColumn[i]) { //Only for char types
			    statistics[i]=Math.max(statistics[i], nextElement.getBytes(StandardCharsets.UTF_8).length);
			}
			if (nextElement.length()>0) {
				if (quotechar !=  CSVSettingsBean.NO_QUOTE_CHARACTER && useEnclosing && (isStringLikeColumn[i] ||isHeader)) {
					sb.append(quotechar);
				}

				for (int j = 0; j < nextElement.length(); j++) {
					char nextChar = nextElement.charAt(j);

					if (nextChar=='\n' && j+1 < nextElement.length() && nextElement.charAt(j+1)=='\r') {
						sb.append("\\n\\r");
						j++;
					} else if (nextChar=='\r' && j+1 < nextElement.length() && nextElement.charAt(j+1)=='\n') {
						sb.append("\\r\\n");
						j++;
					} else if (nextChar=='\r') {
						sb.append("\\r");
					} else if (nextChar=='\n') {
						sb.append("\\n");
					} else if (nextChar == quotechar && useEnclosing) {
						sb.append((doubleEnclose|| escapechar == CSVSettingsBean.NO_ESCAPE_CHARACTER)?quotechar:escapechar).append(nextChar);
					} else if (escapechar != CSVSettingsBean.NO_ESCAPE_CHARACTER && nextChar == escapechar) {
						sb.append(escapechar).append(nextChar);
					} else if (escapechar != CSVSettingsBean.NO_ESCAPE_CHARACTER && nextChar == separator && !useEnclosing) {
						sb.append(escapechar).append(nextChar);
					} else if (nextChar!=(char)0x00){
						if (append(isStringLikeColumn[i], nextChar)) {
							sb.append(nextChar);
						}
					}
				}
				if (quotechar != CSVSettingsBean.NO_QUOTE_CHARACTER && useEnclosing && (isStringLikeColumn[i] ||isHeader)) {
					sb.append(quotechar);
				}
			}
		}

		sb.append(lineEnd);
		return sb;
	}

	public static boolean isColumnStringLike(int colType) throws SQLException {
		switch (colType)
		{
		case Types.BOOLEAN:
		case Types.CLOB:
		case Types.JAVA_OBJECT:
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
		case Types.CHAR:
			return true;
		default:
			return false;
		}
	}


    public boolean isComputeStatistics() {
        return computeStatistics;
    }

    public void setComputeStatistics(boolean computeStatistics) {
        this.computeStatistics = computeStatistics;
    }

    public int[] getStatistics() {
        return statistics;
    }

    public void setStatistics(int[] statistics) {
        this.statistics = statistics;
    }

    public boolean handleSurrogateCharacters() {
        return handleSurrogateCharacters;
    }

    public void setHandleSurrogateCharacters(boolean handleSurrogateCharacters) {
        this.handleSurrogateCharacters = handleSurrogateCharacters;
    }

	public long getLinesWritten() {
		return linesWritten;
	}

	public void resetLinesWritten() {
		this.linesWritten = 0;
	}

    
}
