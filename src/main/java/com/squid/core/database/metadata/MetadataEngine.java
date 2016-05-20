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
package com.squid.core.database.metadata;

import java.sql.Connection; 
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squid.core.database.impl.DataSourceReliable;
import com.squid.core.database.model.Column;
import com.squid.core.database.model.Database;
import com.squid.core.database.model.DatabaseFactory;
import com.squid.core.database.model.ForeignKey;
import com.squid.core.database.model.Index;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.jdbc.vendor.VendorSupportRegistry;
import com.squid.core.sql.db.features.IMetadataPrimaryKeySupport;
import com.squid.core.sql.render.ISkinFeatureSupport;

/**
 * The generic implementation for JDBC metadata reverse-engineering
 * @author sergefantino
 *
 */
public class MetadataEngine implements IMetadataEngine {
    
    static final Logger logger = LoggerFactory.getLogger(MetadataEngine.class);
	
	private DatabaseFactory df;
	private DataSourceReliable ds;
	
	private VendorMetadataSupport vendorSpecific = new GenericMetadataSupport();
	
	private Hashtable<String, String> m_definitions;

	//private ClassLoader customCL;

	public MetadataEngine(DatabaseFactory df) {
		this.df = df;
		this.ds = df.getManager().getDatasource();
	}
	
	/**
	 * for test support
	 * @param df
	 */
	public MetadataEngine(DatabaseFactory df, DataSourceReliable ds) {
		this.df = df;
		this.ds = ds;
	}
	
	 public void init() {
		 if (m_definitions==null) {
			 m_definitions = new Hashtable<String, String>();
			 for (String def : MetadataConst.definitions) {
				 m_definitions.put(def,def);
			 }
		 }
	 }
    
	 /**
	  * check if the database support UTF-8 surrogate Characters
	  * @return
	  */
	 public boolean handleSurrogateCharacters() {
	     return vendorSpecific.handleSurrogateCharacters();
	 }
	
	 protected String getColumnDef(String def) {
		 if (m_definitions==null) {
			 init();
		 }
		 final String result = m_definitions.get(def);
		 return result!=null?result:def;
	 }
	 
	 protected Connection getBlockingConnection() throws SQLException {
		 return ds.getConnectionBlocking();
	 }

	 /**
	  * populate the database: retrieve product information and schemas
	  * @param database
	  * @throws SQLException
	  */
	 public void populateDatabase(Database database) throws ExecutionException {
		 Connection conn = null;
		 try {
			 conn = getBlockingConnection();
			 if (!(conn.getMetaData().getDatabaseProductName().equals("Apache Drill"))){
				 conn.setAutoCommit(true);
			 }
			 DatabaseMetaData metadata = conn.getMetaData();
			 // read standard JDBC 
			 database.setProductName(metadata.getDatabaseProductName());
			 database.setProductVersion(metadata.getDatabaseProductVersion());
			 database.setMajorVersion(metadata.getDatabaseMajorVersion());
			 database.setMinorVersion(metadata.getDatabaseMinorVersion());

			 // refine the detection (only for postgre (greenplum or redshift))
			 detectDatabaseVersion(database,conn);
			 // select specific vendor support
			 this.vendorSpecific = VendorSupportRegistry.INSTANCE.getVendorSupport(database).getVendorMetadataSupport();
			 if(logger.isDebugEnabled()){logger.debug("Vendor support for metadata "+this.vendorSpecific.getClass());};
		 } catch (SQLException e) {
        	 if (conn!=null) {
        		 try {
					if (!conn.getAutoCommit()) {
						 conn.rollback();
					 }
				} catch (SQLException e1) {
				}
        	 }
			 throw new ExecutionException("cannot populate database '"+database.getUrl()+"'", e);
		 } finally {
			 try {
				 if (conn!=null) {
					 if (!conn.getAutoCommit()) {
						 conn.commit();
					 }
					 conn.close();
					 ds.releaseSemaphore();
				 }
			 } catch (SQLException e) {
				 logger.error(e.getLocalizedMessage());
			 }
		 }
	 }
	 
	 public void populateSchemas(Database database) throws ExecutionException {
		 Connection conn = null;
		 try {
			 conn = getBlockingConnection();
			 if (!conn.getMetaData().getDatabaseProductName().equals("Apache Drill") ){
				 conn.setAutoCommit(true);
			 }
			 //
			 // try to lookup the schemas
			 List <Schema> result = vendorSpecific.getSchemas(df, conn);
			 for (Schema schema: result) {
				 String name = schema.getName();
				 //
				 if (database.findSchema(name)==null) {
					 schema.setDatabase(database);
				 }
			 }
		 } catch (Exception e) {
        	 if (conn!=null) {
        		 try {
					if (!conn.getAutoCommit()) {
						 conn.rollback();
					 }
				} catch (SQLException e1) {
				}
        	 }
			 throw new ExecutionException("cannot populate schemas for database '"+database.getUrl()+"'", e);
		 } finally {
			 try {
				 if (conn!=null) {
					 if (!conn.getAutoCommit()) {
						 conn.commit();
					 }
					 conn.close();
					 ds.releaseSemaphore();
				 }
			 } catch (SQLException e) {
				 logger.error(e.getLocalizedMessage());
			 }
		 }
	 }
	 
	 /**
	  * specific code to detect the actual database product
	  * @param database
	  * @param conn
	 * @throws DatabaseServiceException 
	  */
	 private void detectDatabaseVersion(Database database, Connection conn) throws ExecutionException {
		 // if the driver is postgres, ask the database version in order to get more informed information: RS? GP?
		 if (database.getProductName().equals(POSTGRESQL_NAME)) {
			Statement statement = null;
			try {
				statement = conn.createStatement();
				if (statement.execute("select version();")) {
					ResultSet rs = statement.getResultSet();
					if (rs.next()) {
						String version = rs.getString(1);// only one column
						if (version.contains(REDSHIFT_NAME)) {
							database.setProductName(REDSHIFT_NAME);
						} else if (version.contains(GREENPLUM_NAME)) {
							database.setProductName(GREENPLUM_NAME);
						}
					}
				}
			} catch (SQLException e) {
				throw new ExecutionException("cannot analyze database '"+database.getUrl()+"'", e);
			} finally {
				try {
					if (statement!=null) statement.close();
				} catch (SQLException e) {
					logger.error(e.getLocalizedMessage());
				}
			}
		}
	}

	public void populateSchema(Schema schema) throws ExecutionException {
		Connection conn = null;
		 try {
			 conn = getBlockingConnection();
			 populateTables(conn, schema, null);
		 } catch (Exception e) {
        	 if (conn!=null) {
        		 try {
					if (!conn.getAutoCommit()) {
						 conn.rollback();
					 }
				} catch (SQLException e1) {
				}
        	 }
			 throw new ExecutionException("cannot populate schema '"+schema.getName()+"'", e);
		 } finally {
			 try {
				 if (conn!=null) {
					 if (!conn.getAutoCommit()) {
						 conn.commit();
					 }
					 conn.close();
					 ds.releaseSemaphore();
				 }
			 } catch (SQLException e) {
				 logger.error(e.getLocalizedMessage());
			 }
		 }
	 }
	
    protected void populateTables(Schema schema, String tableName) throws ExecutionException {
		Connection conn = null;
		try {
			conn = getBlockingConnection();
			populateTables(conn, schema, tableName);
		} catch (Exception e) {
			if (conn != null) {
				try {
					if (!conn.getAutoCommit()) {
						conn.rollback();
					}
				} catch (SQLException e1) {
				}
			}
			throw new ExecutionException("failed to populate table '" + tableName + "'", e);
		} finally {
			try {
				if (conn != null) {
					if (!conn.getAutoCommit()) {
						conn.commit();
					}
					conn.close();
					ds.releaseSemaphore();
				}
			} catch (SQLException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
    }
	 

	 protected void populateTables(Connection conn, Schema schema, String tableName) throws ExecutionException {
		 
		 List<Table> tables = vendorSpecific.getTables(this.df, conn, schema.getCatalog(),schema.getName(),tableName);
		 for(Table table: tables){
			 table.setSchema(schema);
			 schema.addTable(table);
		 }

	 }

	 public Table refreshTable(Schema schema, String tableName) throws ExecutionException {
	     Table check = schema.findTable(tableName);
	     if (check!=null) {
	         schema.removeTable(check);
	     }
         Connection conn = null;
         try {
             conn = getBlockingConnection();
             populateTables(conn, schema, tableName);
             check = schema.findTable(tableName);
             if (check!=null) {
                 populateColumns(conn, Collections.singletonList(check));
                 //schema.addTable(check);
             }
             return check;
         } catch (Exception e) {
        	 if (conn!=null) {
        		 try {
					if (!conn.getAutoCommit()) {
						 conn.rollback();
					 }
				} catch (SQLException e1) {
				}
        	 }
        	 throw new ExecutionException("failed to refresh table '"+tableName+"' schema '"+schema.getName()+"'", e);
         } finally {
			 try {
				 if (conn!=null) {
						if (!conn.getAutoCommit()) {
							conn.commit();
						}
					 conn.close();
					 ds.releaseSemaphore();
				 }
			 } catch (SQLException e) {
				 logger.error(e.getLocalizedMessage());
			 }
         }
	 }
	 
	 /**
	  * populate the columns for every table in the schema
	  * @param schema
	 * @throws SQLException 
	 * @throws DatabaseServiceException 
	  */
	 public void populateColumns(Schema schema) throws ExecutionException {
		 Connection conn = null;
         try {
        	 conn = getBlockingConnection();
			 if (!conn.getMetaData().getDatabaseProductName().equals("Apache Drill")){
				 conn.setAutoCommit(true);
			 }
             List <ColumnData> datas = vendorSpecific.getColumns(conn, schema.getCatalog(),schema.getName(),null,null);
             for (ColumnData data : datas){
	             Table table = schema.findTable(data.table_name);
	             if (table!=null) {
	                 addColumn(table,vendorSpecific.normalizeColumnData(data));
	             }
             }
         } catch (SQLException e) {
        	 if (conn!=null) {
        		 try {
					if (!conn.getAutoCommit()) {
						 conn.rollback();
					 }
				} catch (SQLException e1) {
				}
        	 }
        	 throw new ExecutionException("failed to populate columns for schema '"+schema.getName()+"'", e);
         } finally {
			try {
				if (conn != null) {
					if (!conn.getAutoCommit()) {
						conn.commit();
					}
					conn.close();
					ds.releaseSemaphore();
				}
			} catch (SQLException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
	}

	public void populateColumns(List<? extends Table> tables) throws ExecutionException {
		Connection conn = null;
         try {
             conn = getBlockingConnection();
             populateColumns(conn, tables);
		 } catch (Exception e) {
			 if (conn!=null) {
				 try {
					 if (!conn.getAutoCommit()) {
						 conn.rollback();
					 }
				 } catch (SQLException ee) {
					 //
				 }
			 }
			 throw new ExecutionException("cannot populate columns", e);
         } finally {
			 try {
				 if (conn!=null) {
					 if (!conn.getAutoCommit()) {
						 conn.commit();
					 }
					 conn.close();
					 ds.releaseSemaphore();
				 }
			 } catch (SQLException e) {
				 logger.error(e.getLocalizedMessage());
			 }
         }
     }

	 public void populateColumns(Connection conn, List<? extends Table> tables) throws ExecutionException {
		 try {
			 for (Table table : tables) {
				  List<ColumnData> ldata = vendorSpecific.getColumns(conn, table.getCatalog(),table.getSchema().getName(),table.getName(),null);
				 // update in batch
				 for (ColumnData data : ldata) {
					 addColumn(table,data);
				 }
			 }
		 } catch (SQLException e) {
			 throw new ExecutionException("Failed to populate columns due to SQLException: "+e.getLocalizedMessage(), e);
		 }
	 }

	 private Column addColumn(Table table, ColumnData data) {
		 Column column = df.createColumn();
		 column.setName(data.column_name);
		 if (data.remarks!=null) {
			 column.setDescription(data.remarks);
		 }
		 column.setTable(table);
		 table.addColumn(column);
		 ExtendedType type = createType(data);
		 column.setType(type);
		 if (data.is_nullable.compareToIgnoreCase("NO")==0) {
			 column.setNotNullFlag(true);
		 }
		 return column;
	 }
	 
	 protected ExtendedType createType(ColumnData data) {
		 ExtendedType type = new ExtendedType(getDomain(data), data.type_name, data.data_type, data.decimal_digits, data.column_size);
		 return type;
	 }
		
	 public IDomain getDomain(ColumnData data) {
		 return ExtendedType.computeDomain(data.data_type,data.column_size,data.decimal_digits);
	}


	private int[] computeColumnPos(String[] columns, ResultSet result) throws SQLException {
		ResultSetMetaData meta = result.getMetaData();
		List<String> lookup = Arrays.asList(columns);
		int[] indexes = new int[columns.length];
		Arrays.fill(indexes,-1);
		for (int i=1;i<=meta.getColumnCount();i++) {
			String cname = meta.getColumnLabel(i);
			int index = lookup.indexOf(cname.toUpperCase());
			if (index>=0) {
				indexes[index] = i;
			}
		}
		return indexes;
	}
	
	 public boolean populateImportedKeys(List<? extends Table> tables) throws ExecutionException {
		 if (tables.isEmpty()) return false;
		 Database db = tables.get(0).getSchema().getDatabase();
		 String product = db.getProductName();
		 if (product.equals(REDSHIFT_NAME)) {
			 return false;
		 } else {
			 Connection conn = null;
			 try {
				 conn = getBlockingConnection();
				 for (Table table : tables) {
					 populateImportedKeys(conn, table);
				 }
				 return true;
			 } catch (Exception e) {
	        	 if (conn!=null) {
	        		 try {
						if (!conn.getAutoCommit()) {
							 conn.rollback();
						 }
					} catch (SQLException e1) {
					}
	        	 }
				 throw new ExecutionException("failed to populate imported keys", e);
			 } finally {
				 try {
					 if (conn!=null) {
						 if (!conn.getAutoCommit()) {
							 conn.commit();
						 }
						 conn.close();
						 ds.releaseSemaphore();
					 }
				 } catch (SQLException e) {
					 logger.error(e.getLocalizedMessage());
				 }
			 }
		 }
	 }
	 
	 public void populateImportedKeys(Table table) throws ExecutionException {
		 Connection conn = null;
		 try {
			 conn = getBlockingConnection();
			 populateImportedKeys(conn, table);
		 } catch (Exception e) {
        	 if (conn!=null) {
        		 try {
					if (!conn.getAutoCommit()) {
						 conn.rollback();
					 }
				} catch (SQLException e1) {
				}
        	 }
			 throw new ExecutionException("failed to populate imported keys", e);
		 } finally {
			 try {
				 if (conn!=null) {
					 if (!conn.getAutoCommit()) {
						 conn.commit();
					 }
					 conn.close();
					 ds.releaseSemaphore();
				 }
			 } catch (SQLException e) {
				 logger.error(e.getLocalizedMessage());
			 }
		 }
	 }

	 protected void populateImportedKeys(Connection conn, Table table) throws ExecutionException {
		 try {
			 // standard procedure
			 List<ForeignKeyData> ldata = new ArrayList<ForeignKeyData>();
			 ResultSet res = vendorSpecific.getImportedKeys(conn,table.getCatalog(),table.getSchema().getName(),table.getName());
			 try {
				 while (res.next()) {
					 ForeignKeyData data = new ForeignKeyData();
					 loadForeignKeyData(res,data);
					 ldata.add(data);
				 }
			 } finally {
				 res.close();
			 }
			 // update in batch
			 // note: updating may invoke lazy population for columns/tables => batch avoid nested jdbc call
			 for (ForeignKeyData data : ldata) {
				 updateForeignKey(table.getSchema().getDatabase(), table, data);
			 }
		 } catch (SQLException e) {
			 throw new ExecutionException("Failed to populate imported keys for table '"+table.getName()+"'", e);
		 }
	 }

	private final String[] FK_CNAMES = new String[]{
			 getColumnDef(MetadataConst.PKTABLE_CAT),
			 getColumnDef(MetadataConst.PKTABLE_SCHEM),
			 getColumnDef(MetadataConst.PKTABLE_NAME),
			 getColumnDef(MetadataConst.PKCOLUMN_NAME),
			 getColumnDef(MetadataConst.FKTABLE_CAT),
			 getColumnDef(MetadataConst.FKTABLE_SCHEM),
			 getColumnDef(MetadataConst.FKTABLE_NAME),
			 getColumnDef(MetadataConst.FKCOLUMN_NAME),
			 getColumnDef(MetadataConst.KEY_SEQ),
			 getColumnDef(MetadataConst.UPDATE_RULE),
			 getColumnDef(MetadataConst.DELETE_RULE),
			 getColumnDef(MetadataConst.DEFERRABILITY),
			 getColumnDef(MetadataConst.FK_NAME),
			 getColumnDef(MetadataConst.PK_NAME)};

	 private int[] FK_CPOS = null;
	 
	 private void loadForeignKeyData(ResultSet res, ForeignKeyData data) throws SQLException {
		 if (FK_CPOS==null) {
			 FK_CPOS = computeColumnPos(FK_CNAMES,res);
		 }
		 //
		 data.pktable_cat = res.getString(FK_CPOS[0]);//getColumnDef(PKTABLE_CAT));
		 data.pktable_schem = res.getString(FK_CPOS[1]);//getColumnDef(PKTABLE_SCHEM));
		 data.pktable_name = res.getString(FK_CPOS[2]);//getColumnDef(PKTABLE_NAME));
		 data.pkcolumn_name = res.getString(FK_CPOS[3]);//getColumnDef(PKCOLUMN_NAME));
		 data.fktable_cat = res.getString(FK_CPOS[4]);//getColumnDef(FKTABLE_CAT));
		 data.fktable_schem = res.getString(FK_CPOS[5]);//getColumnDef(FKTABLE_SCHEM));
		 data.fktable_name = res.getString(FK_CPOS[6]);//getColumnDef(FKTABLE_NAME));
		 data.fkcolumn_name = res.getString(FK_CPOS[7]);//getColumnDef(FKCOLUMN_NAME));
		 data.key_seq = res.getInt(FK_CPOS[8]);//getColumnDef(KEY_SEQ));
		 data.update_rule = res.getInt(FK_CPOS[9]);//getColumnDef(UPDATE_RULE));
		 data.delete_rule = res.getInt(FK_CPOS[10]);//getColumnDef(DELETE_RULE));
		 data.deferrability = res.getInt(FK_CPOS[11]);//getColumnDef(DEFERRABILITY));
		 data.fk_name = res.getString(FK_CPOS[12]);//getColumnDef(FK_NAME));
		 if (data.fk_name==null) {
			 data.fk_name = "";
		 }
		 data.pk_name = res.getString(FK_CPOS[13]);//getColumnDef(PK_NAME));
		 if (data.pk_name==null) {
			 data.pk_name = "";
		 }
	 }

	 protected ForeignKey updateForeignKey(Database database, Table table, ForeignKeyData data) throws ExecutionException {
		 //
		 Table fktable = database.findTable(data.fktable_schem,data.fktable_name);
		 if (fktable==null || !fktable.equals(table)) {
			 return null;
		 }
		 Column fkcolumn = fktable.findColumnByName(data.fkcolumn_name);
		 if (fkcolumn==null) {
			 return null;
		 }
		 Table pktable = database.findTable(data.pktable_schem,data.pktable_name);
		 if (pktable==null) {
			 return null;
		 }
		 Column pkcolumn = pktable.findColumnByName(data.pkcolumn_name);
		 if (pkcolumn==null) {
			 return null;
		 }
		 // the foreignkey is stored in the foreign table
		 // first create the key
		 ForeignKey fk = df.createForeignKey();
		 fk.setName(data.fk_name);
		 fk = table.addForeignKey(fk);// check if already defined
		 fk.add(pkcolumn, fkcolumn, data.key_seq);
		 //
		 return fk;
	 }

	 public void populatePK(Table table) throws ExecutionException {
		 if (df.getManager().getSkin().getFeatureSupport(IMetadataPrimaryKeySupport.ID)==ISkinFeatureSupport.IS_SUPPORTED) {
			 Connection conn = null;
			 try {
				 conn = getBlockingConnection();
				 ResultSet res = vendorSpecific.getPrimaryKeys(conn,table.getCatalog(),table.getSchema().getName(),table.getName());
				 try {
					 //
					 PrimaryKeyData data = new PrimaryKeyData();
					 //
					 while (res.next()) {
						 //
						 loadPKData(res,data);
						 //
						 createPK(table,data);
					 }
				 } finally {
					 if (res!=null) res.close();
				 }
			 } catch (Exception e) {
	        	 if (conn!=null) {
	        		 try {
						if (!conn.getAutoCommit()) {
							 conn.rollback();
						 }
					} catch (SQLException e1) {
					}
	        	 }
				 throw new ExecutionException("failed to populate primary key for table '"+table.getName()+"'", e);
			 } finally {
				 try {
					 if (conn!=null) {
						 if (!conn.getAutoCommit()) {
							 conn.commit();
						 }
						 conn.close();
						 ds.releaseSemaphore();
					 }
				 } catch (SQLException e) {
					 logger.error(e.getLocalizedMessage());
				 }
			 }
		 }
	 }

	 public void populatePK(Schema schema) throws ExecutionException {
		 if (df.getManager().getSkin().getFeatureSupport(IMetadataPrimaryKeySupport.ID)==ISkinFeatureSupport.IS_SUPPORTED) {
			 Connection conn = null;
			 try {
				 conn = getBlockingConnection();
				 ResultSet res = vendorSpecific.getPrimaryKeys(conn,schema.getCatalog(),schema.getName(),null);
				 try {
					 //
					 PrimaryKeyData data = new PrimaryKeyData();
					 //
					 while (res.next()) {
						 //
						 loadPKData(res,data);
						 //
						 Table table = schema.findTable(data.table_name);
						 if (table!=null) {
							 createPK(table,data);
						 }
					 }
				 } finally {
					 if (res!=null) res.close();
				 }
			 } catch (Exception e) {
	        	 if (conn!=null) {
	        		 try {
						if (!conn.getAutoCommit()) {
							 conn.rollback();
						 }
					} catch (SQLException e1) {
					}
	        	 }
				 throw new ExecutionException("failed to populate primary key for schema '"+schema.getName()+"'", e);
			 } finally {
				 try {
					 if (conn!=null) {
						 if (!conn.getAutoCommit()) {
							 conn.commit();
						 }
						 conn.close();
						 ds.releaseSemaphore();
					 }
				 } catch (SQLException e) {
					 logger.error(e.getLocalizedMessage());
				 }
			 }
		 }
	 }

	 private final String[] PK_CNAMES = new String[]{
			 getColumnDef(MetadataConst.PK_NAME),      //0
			 getColumnDef(MetadataConst.COLUMN_NAME),  //1
			 getColumnDef(MetadataConst.KEY_SEQ),      //2
			 getColumnDef(MetadataConst.TABLE_NAME)    //3
	 };

	 private int[] PK_CPOS = null;

	 private void loadPKData(ResultSet res, PrimaryKeyData data) throws SQLException {
		 if (PK_CPOS==null) {
			 PK_CPOS = computeColumnPos(PK_CNAMES,res);
		 }
		 //
		 data.table_name = res.getString(PK_CPOS[3]);    // TABLE_NAME
		 data.pk_name = res.getString(PK_CPOS[0]);       // PK_NAME
		 data.column_name = res.getString(PK_CPOS[1]);   // COLUMN_NAME
		 data.key_seq = res.getInt(PK_CPOS[2]);          // KEY_SEQ
		 //
	 }

	 private boolean createPK(Table table, PrimaryKeyData data) throws ExecutionException {
		 Column col = table.findColumnByName(data.column_name);
		 if (col!=null) {
			 if (table.getPrimaryKey()==null) {
				 table.setPrimaryKey(new Index(data.pk_name!=null?data.pk_name:""));
			 }
			 table.getPrimaryKey().addColumn(col, data.key_seq-1);
			 return true;
		 } else {
			 return false;
		 }
	 }
	 
	 public int[] normalizeDataType(ResultSet rs) throws SQLException {
		 return this.vendorSpecific.normalizeColumnType(rs);
	 }
	 
}
