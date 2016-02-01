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
package com.squid.core.sql.template;

import java.sql.Connection;
import java.sql.Types;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.Table;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.Operators;
import com.squid.core.sql.render.DelegateSamplingDecorator;
import com.squid.core.sql.render.ISamplingDecorator;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SQLTokenConstant;

public abstract class DefaultSQLSkin 
implements SQLSkin
{
	
	private String skinPrefix = "default";

	public String getSkinPrefix() {
		return skinPrefix;
	}
	
	public String overrideTemplateID(String templateID) {
		return templateID;
	}
	
	public String getToken(int token) throws RenderingException {
		switch (token) {
		case SQLTokenConstant.SELECT:return "SELECT";
		case SQLTokenConstant.FROM:return "FROM";
		case SQLTokenConstant.WHERE:return "WHERE";
		case SQLTokenConstant.INSERT:return "INSERT";
		case SQLTokenConstant.UPDATE:return "UPDATE";
		case SQLTokenConstant.AND:return "AND";
		case SQLTokenConstant.OR:return "OR";
		case SQLTokenConstant.NULL:return "NULL";
		case SQLTokenConstant.IN:return "IN";
		case SQLTokenConstant.NOT:return "NOT";
		case SQLTokenConstant.MERGE:return "MERGE";
		case SQLTokenConstant.LEFT_OUTER_JOIN:return "LEFT OUTER JOIN";
		case SQLTokenConstant.RIGHT_OUTER_JOIN:return "RIGHT OUTER JOIN";
		case SQLTokenConstant.FULL_OUTER_JOIN:return "FULL OUTER JOIN";
		case SQLTokenConstant.INNER_JOIN:return "INNER JOIN";
		case SQLTokenConstant.CROSS_JOIN:return "CROSS JOIN";
		default:throw new RenderingException("invalid SQL token");
		}
	}
	
	public OperatorDefinition overrideOperatorDefinition(OperatorDefinition op, ExtendedType[] args) {
		if (op.getId()==IntrinsicOperators.PLUS) {
			if (isAlpha(args)) {
				return Operators.CONCAT;
			} else {
				return op;
			}
		} else if ((op.getId()==IntrinsicOperators.PLUS || op.getId()==IntrinsicOperators.SUBTRACTION) 
				&& args.length==2
				&& (args[0].getDomain().isInstanceOf(IDomain.TEMPORAL) || args[1].getDomain().isInstanceOf(IDomain.TEMPORAL)) )
		{
			if (op.getId()==IntrinsicOperators.PLUS) {
				return Operators.DATE_ADD;
			} else if (op.getId()==IntrinsicOperators.SUBTRACTION) {
				return Operators.DATE_SUB;
			}
	    }
		// else
		return op;
	}
	
	protected boolean isAlpha(ExtendedType[] args) {
		for (int i=0;i<args.length;i++) {
			if (args[i].getDomain().isInstanceOf(IDomain.STRING)) {
				return true;
			}
		}
		return false;
	}
	
	public ISamplingDecorator createSamplingDecorator(DelegateSamplingDecorator sampling) throws RenderingException {
		throw new RenderingException("unsupported feature: SQLSkin.createSamplingDecorator()");
	}

	@Override
	public ExtendedType createExtendedType(IDomain domain, int dataType, String format, int size, int precision) {
		return new ExtendedType(domain, dataType, precision, size);
	}
	
	public ExtendedType createExtendedType(IDomain domain) {
		if (domain.isInstanceOf(IDomain.NUMERIC)) {
			return ExtendedType.NUMERIC;
		} else
		if (domain.isInstanceOf(IDomain.STRING)) {
			return ExtendedType.STRING;
		} else
		if (domain.isInstanceOf(IDomain.TIMESTAMP)) {
			return ExtendedType.TIMESTAMP;
		} else
		if (domain.isInstanceOf(IDomain.DATE)) {
			return ExtendedType.DATE;
		} else
		if (domain.isInstanceOf(IDomain.TIME)) {
			return ExtendedType.TIME;
		} else 
		if (domain.isInstanceOf(IDomain.INTERVAL)) {
			return ExtendedType.INTERVAL;
		} else 
		if (domain.isInstanceOf(IDomain.CONDITIONAL)) {
			return ExtendedType.BOOLEAN;
		} else {
			return ExtendedType.UNDEFINED;
		}
	}
	
	public String getTypeName(int SQLType) {
		switch (SQLType) {
		case Types.ARRAY:return "ARRAY";
		case Types.BIGINT:return "BIGINT";
		case Types.BINARY:return "BINARY";
		case Types.BIT:return "BIT";
		case Types.BLOB:return "BLOB";
		case Types.BOOLEAN:return "BOOLEAN";
		case Types.CHAR:return "CHAR";
		case Types.CLOB:return "CLOB";
		case Types.DATALINK:return "DATALINK";
		case Types.DATE:return "DATE";
		case Types.DECIMAL:return "DECIMAL";
		case Types.DOUBLE:return "DOUBLE";
		case Types.FLOAT:return "FLOAT";
		case Types.INTEGER:return "INTEGER";
		case Types.JAVA_OBJECT:return "JAVA_OBJECT";
		case Types.LONGNVARCHAR:return "LONGNVARCHAR";
		case Types.LONGVARBINARY:return "LONGVARBINARY";
		case Types.NCHAR:return "NCHAR";
		case Types.NCLOB:return "NCLOB";
		case Types.NULL:return "UNDEFINED";//
		case Types.NUMERIC:return "NUMERIC";
		case Types.NVARCHAR:return "NVARCHAR";
		case Types.OTHER:return "UNDEFINED";//
		case Types.REAL:return "REAL";
		case Types.REF:return "REF";
		case Types.ROWID:return "ROWID";
		case Types.SMALLINT:return "SMALLINT";
		case Types.SQLXML:return "SQLXML";
		case Types.STRUCT:return "STRUCT";
		case Types.TIME:return "TIME";
		case Types.TIMESTAMP:return "TIMESTAMP";
		case Types.TINYINT:return "TINYINT";
		case Types.VARBINARY:return "VARBINARY";
		case Types.VARCHAR:return "VARCHAR";
		default:return "UNDEFINED";//
		}
	}

	@Override
	public String getTypeDefinition(Column column) {
		return getTypeDefinition(column.getType());
	}
	
	@Override
	public String getTypeDefinition(ExtendedType type) {
		if (type==null) {
			return "NULL";
		}
		String name = getTypeName(type.getDataType());
		switch (type.getDataType()) {
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.BIGINT:
		case Types.FLOAT:
		case Types.DOUBLE:
			return name;
		case Types.DATE:
			return "DATE";
		default:
			return printOptionalSizeAndScale(name, type.getSize(), type.getScale());
		}
	}
	
	protected String printOptionalSizeAndScale(String typename, int size, int scale) {
		if (size==0 && scale==Integer.MIN_VALUE) {
			return typename;
		} else if (scale==0) {
			return typename+"("+size+")";
		} else {
			return typename+"("+size+","+scale+")";
		}
	}
    
    public String fullyQualified(Table table) {
        String res = "";
        if (table.getSchema()!=null&&!table.getSchema().isNullSchema()) {
            res += quoteSchemaIdentifier(table.getSchema())+".";
        }
        res += quoteTableIdentifier(table);
        return res;
    }
    
	/**
	 * Share-nothing architecture: this implies to create PKs at table creation for maximum optimization
	 */
	public boolean isShareNothing() {
		return false;
	}
	
	@Override
	public void initializeConnection(Connection conn) {
		// default do nothing
	}

}
