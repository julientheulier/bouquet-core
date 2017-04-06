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
package com.squid.core.sql.db.templates;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.squid.core.database.model.Column;
import com.squid.core.database.model.DatabaseProduct;
import com.squid.core.database.model.Schema;
import com.squid.core.database.model.Table;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.expression.scope.ScopeException;
import com.squid.core.sql.db.features.IGroupingSetSupport;
import com.squid.core.sql.db.render.FromTablePiece;
import com.squid.core.sql.model.SQLScopeException;
import com.squid.core.sql.render.IJoinDecorator;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.ISkinFeatureSupport;
import com.squid.core.sql.render.OperatorPiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.groupby.GroupType;
import com.squid.core.sql.render.groupby.GroupingSetPiece;
import com.squid.core.sql.render.groupby.IGroupByElementPiece;
import com.squid.core.sql.render.groupby.IGroupByPiece;
import com.squid.core.sql.statements.FromSelectStatementPiece;
import com.squid.core.sql.statements.SelectStatement;
import com.squid.core.sql.statements.Statement;
import com.squid.core.sql.template.DefaultSQLSkin;
import com.squid.core.sql.template.ISkinHandler;

/**
 * The default skin that uses database metadata
 *
 * @author serge fantino
 */
public class DefaultJDBCSkin extends DefaultSQLSkin {

	public static final SQLSkin DEFAULT = new DefaultJDBCSkin(new DefaultSkinProvider());

	private DateFormat dateFormat;
	private DateFormat timestampFormat;
	private DecimalFormat decimalFormat;

	private String identifier_quote = "\"";
	private String literal_quote = "\'";
	private String endOfStatement_quote = ";";

	private ISkinProvider provider = null;

	private boolean isComments = true;// default is to display comments

	/**
	 * This flag is true when the Skin have successfully initiated and connected
	 * to the database.
	 */
	private boolean creatingSkinSucess = false;

	private DatabaseProduct product;

	/**
	 * instanciate using the SkinFactory.INSTANCE.createSkin() method
	 *
	 */
	protected DefaultJDBCSkin(ISkinProvider provider) {
		this.provider = provider;
		initFormat();
	}

	/**
	 * instanciate using the SkinFactory.INSTANCE.createSkin() method
	 *
	 */
	protected DefaultJDBCSkin(ISkinProvider provider, DatabaseProduct product) {
		this(provider);
		this.product = product;
	}

	@Override
	public ISkinHandler getSkinHandler() {
		return this.provider;
	}

	/**
	 *
	 */
	protected void initFormat() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// decimalFormat = new DecimalFormat("#.#################;(#)");
		decimalFormat = new DecimalFormat("#.#################");
		// decimalFormat.setNegativePrefix("(-");
		// decimalFormat.setNegativeSuffix(")");
		// decimalFormat.getDecimalFormatSymbols().setDecimalSeparator('.');
		DecimalFormatSymbols dfs = decimalFormat.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(dfs);
		decimalFormat.setDecimalSeparatorAlwaysShown(false);
		decimalFormat.setGroupingUsed(false);
	}

	protected ISkinProvider getProvider() {
		return provider;
	}

	@Override
	public DatabaseProduct getProduct() {
		return product;
	}

	public void configure(DatabaseMetaData metadata) {
		try {
			// m_product = MetadataEngine.getProduct(metadata);
			identifier_quote = metadata.getIdentifierQuoteString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String quoteColumnIdentifier(String identifier) {
		return quoteIdentifier(identifier);
	}

	@Override
	public String quoteColumnIdentifier(Column column) {
		return quoteIdentifier(column.getName());
	}

	@Override
	public String quoteSchemaIdentifier(String identifier) {
		return quoteIdentifier(identifier);
	}

	@Override
	public String quoteSchemaIdentifier(Schema schema) {
		return quoteIdentifier(schema.getName());
	}

	@Override
	public String quoteTableIdentifier(String identifier) {
		return quoteIdentifier(identifier);
	}

	@Override
	public String quoteTableIdentifier(Table table) {
		return quoteIdentifier(table.getName());
	}

	@Override
	public String quoteIdentifier(String identifier) {
		return getIdentifier_quote() + identifier + getIdentifier_quote();
	}

	@Override
	public void setComments(boolean comments) {
		this.isComments = comments;
	}

	@Override
	public boolean isComments() {
		return isComments;
	}

	@Override
	public String comment(String text) {
		String res = text.replaceAll("\\r\\n", "");
		res = res.replaceAll("\\n", "");
		return res;
	}

	@Override
	public String quoteComment(String text) {
		if (isComments) {
			if (text.contains("\n")) {
				return "\n/*\n" + text + "\n*/\n";
			} else {
				return "-- " + comment(text) + "\n";
			}
		} else {
			return "";
		}
	}

	@Override
	public String quoteLiteral(String literal) {
		String res = literal.replaceAll("\\\\", "\\\\\\\\");
		res = literal.replaceAll("'", "''");
		res = res.replaceAll(literal_quote, "\\\\" + literal_quote);
		return literal_quote + res + literal_quote;
	}

	public String quoteStringConstant(String literal) {
		// Old code before T671
		/*
		 * String res = literal.replaceAll("\\\\", "\\\\\\\\"); // res =
		 */
		// T671: regexp_replace can use \\#group_nr. There is no need to escape
		// \ in SQL for constant
		// But constants should have single quote escaped all the time
		String res = literal.replaceAll(literal_quote, literal_quote + literal_quote);
		return literal_quote + res + literal_quote;
	}

	public String fullyQualified(Object table) {
		return table.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.squid.sql.core.model.Skin#quoteConstant(java.lang.Object)
	 */
	@Override
	public String quoteConstant(Object value, IDomain domain) {
		if (domain.isInstanceOf(IDomain.TIMESTAMP)) {
			String date = "";
			if (value instanceof Date) {
				date = timestampFormat.format((Date) value);
			} else {
				date = value.toString();
			}
			return "TIMESTAMP " + quoteLiteral(date);
		} else if (domain.isInstanceOf(IDomain.DATE)) {
			String date = "";
			if (value instanceof Date) {
				date = dateFormat.format((Date) value);
			} else {
				date = value.toString();
			}
			return "DATE " + quoteLiteral(date);
		} else if (domain.isInstanceOf(IDomain.STRING)) {
			// return quoteLiteral((String)value);
			return quoteStringConstant(value != null ? (String) value : "");
		} else if (domain.isInstanceOf(IDomain.BOOLEAN)) {
			if (value instanceof Boolean) {
				return (Boolean) value ? "(1=1)" : "!(1=1)";
			} else if (value instanceof Double && Math.floor((Double) value) == (Double) value) {
				return new Integer(((Double) value).intValue()).toString();
			} else {
				return value.toString();
			}
		} else if (domain.isInstanceOf(IDomain.NUMERIC)) {
			String x = quoteNumber(value);
			if (x.startsWith("-")) {
				return "(" + x + ")";
			} else {
				return x;
			}
		} else {
			return value.toString();
		}
	}

	private String quoteNumber(Object value) {
		if (value instanceof String) {
			try {
				return decimalFormat.format(value);
			} catch (IllegalArgumentException e) {
				return value.toString();
			}
		} else if (value instanceof Double && Math.floor((Double) value) == (Double) value) {
			return new Integer(((Double) value).intValue()).toString();
		} else {
			return decimalFormat.format(value);
			// return value.toString();
		}
	}

	@Override
	public String render(SQLSkin skin, IPiece piece) throws RenderingException {
		try {
			if (piece instanceof FromTablePiece) {
				return render(skin, (FromTablePiece) piece);
			} else if (piece instanceof OperatorPiece) {
				return render(skin, (OperatorPiece) piece);
			} else if (piece instanceof FromSelectStatementPiece) {
				return render(skin, (FromSelectStatementPiece) piece);
			} else if (piece instanceof IGroupByPiece) {
				return render(skin, (IGroupByPiece) piece);
			} else {
				// throw new RenderingException("unsupported piece");
				return null;// return piece.render(this);
			}
		} catch (IOException e) {
			throw new RenderingException(e);
		}
	}

	protected String render(SQLSkin skin, IGroupByPiece piece) throws RenderingException {
		try {
			ISkinFeatureSupport feature = skin.getFeatureSupport(IGroupingSetSupport.ID);
			if (feature == IGroupingSetSupport.IS_SUPPORTED) {
				List<IGroupByElementPiece> pieces = piece.getAllPieces();
				if (!pieces.isEmpty()) {
					return "GROUP BY " + renderElements(skin, pieces);
				} else {
					return "";
				}
			} else {
				List<IGroupByElementPiece> pieces = piece.getPieces(IGroupByPiece.GROUP_BY);
				if (!pieces.isEmpty()) {
					return "GROUP BY " + renderElements(skin, pieces);
				} else {
					return "";
				}
			}
		} catch (SQLScopeException | ScopeException e) {
			throw new RenderingException(e);
		}
	}

	protected String renderElements(SQLSkin skin, List<? extends IPiece> pieces) throws RenderingException {
		String result = "";
		boolean first = true;
		for (IPiece piece : pieces) {
			if (!first) {
				result += " , ";
			}
			result += piece.render(skin);
			first = false;
		}
		return result;
	}

	protected String render(SQLSkin skin, FromTablePiece piece) throws RenderingException, IOException {
		//
		String render = "";
		if (piece.getJoinDecorators() != null) {
			render += "(";// need to inforce evaluation order because outer
							// joins are not associative/commutative operations
		}
		//
		final Table table = piece.getTable();
		if (table == null) {
			throw new RenderingException("table definition is null");
		}
		render += skin.fullyQualified(table);
		//
		// alias
		render += " " + piece.getAlias();
		//
		// joining
		/*
		 * if (piece.getJoinDecorator()!=null) { for (Iterator<IJoinDecorator>
		 * iter = piece.getJoinDecorator().iterator();iter.hasNext();) { render
		 * += " \n     "+iter.next().render(this); } }
		 */
		render += renderJoinDecorator(skin, piece);
		//
		if (piece.getJoinDecorators() != null) {
			render += ")";
		}
		//
		return render;
	}

	protected String renderJoinDecorator(SQLSkin skin, FromTablePiece piece) {
		String render = "";
		if (piece.getJoinDecorators() != null) {
			for (IJoinDecorator decorator : piece.getJoinDecorators()) {
				try {
					render += " \n     " + decorator.render(skin);
				} catch (RenderingException e) {
					render += "\n-- error while rendering join decorator";
				}
			}
		}
		return render;
	}

	protected String render(SQLSkin skin, OperatorPiece piece) throws RenderingException, IOException {
		String[] args = new String[piece.getParams().length];
		for (int i = 0; i < piece.getParams().length; i++) {
			if (piece.getParams()[i] == null) {
				args[i] = "???";
			} else {
				args[i] = piece.getParams()[i].render(skin);
			}
		}
		//
		OperatorDefinition override = piece.getOpDef();
		override = overrideOperatorDefinition(override, piece.getParamTypes());
		return render(skin, piece, override, args);
	}

	protected String render(SQLSkin skin, FromSelectStatementPiece piece) throws RenderingException, IOException {
		String render = "(";
		render = render + piece.getSelect().render(skin);
		render = render + ")";
		// alias
		render += " " + piece.getAlias();
		String joinRender = "";
		if (piece.getJoinDecorators() != null) {
			for (IJoinDecorator decorator : piece.getJoinDecorators()) {
				try {
					joinRender += " \n     " + decorator.render(skin);
				} catch (Exception e) {
					joinRender += "\n-- error while rendering join decorator";
				}
			}
		}
		render += joinRender;
		//
		return render;
	}

	@Override
	public String render(SQLSkin skin, IGroupByElementPiece piece) throws RenderingException {
		if (piece instanceof GroupingSetPiece) {
			String result = renderType(skin, piece.getType()) + "(";
			result += renderElements(skin, ((GroupingSetPiece) piece).getPieces());
			result += ")";
			return result;
		} else {
			return piece.render(this);
		}
	}

	protected String renderType(SQLSkin skin, GroupType groupType) throws RenderingException {
		switch (groupType) {
		case CUBE:
			return "CUBE";
		case GROUP_BY:
			return "GROUP BY";
		case GROUPING_SETS:
			return "GROUPING SETS";
		case ROLLUP:
			return "ROLLUP";
		case INNER:
			return "";
		default:
			throw new RenderingException("Unsupported Group Type");
		}
	}

	@Override
	public String render(SQLSkin skin, OperatorPiece piece, OperatorDefinition opDef, String[] args)
			throws RenderingException {
		//
		// the default is to let the driver override
		if (getProvider().getDelegateRendererRegistry().canRender(opDef.getExtendedID())) {
			return getProvider().getDelegateRendererRegistry().render(skin, piece, opDef, args);
		} else if (opDef.getId() == IntrinsicOperators.DIVIDE && args.length == 2) {
			// handle ticket #627
			// return "(CASE WHEN "+args[1]+"=0 THEN NULL ELSE
			// "+args[0]+"/"+args[1]+" END)";
			String arg0 = args[0];
			String arg1 = "(NULLIF(" + args[1] + ",0))";
			if (piece.getParamTypes() != null) {
				ExtendedType t0 = piece.getParamTypes()[0];
				ExtendedType t1 = piece.getParamTypes()[1];
				if (t0 != null && t1 != null && t0.isExactNumber()) {
					// cast result as float ?
					ExtendedType ext = ExtendedType.FLOAT;
					// ColumnType type =
					// createColumnType(ext.getDomain(),ext.getDataType(),null,ext.getSize(),ext.getScale());
					arg0 = "(CAST ((" + arg0 + ") AS " + getTypeDefinition(ext) + "))";
				}
			}
			String safeOp = arg0 + opDef.getSymbol() + arg1;
			return safeOp;
		} else if (opDef.getId() == IntrinsicOperators.ISNULL && args.length == 1) {
			return "(" + args[0] + ") IS NULL";
		} else if (opDef.getId() == IntrinsicOperators.IS_NOTNULL && args.length == 1) {
			return "(" + args[0] + ") IS NOT NULL";
		} else if (opDef.getId() == IntrinsicOperators.LIKE) {
			return opDef.prettyPrint(" LIKE ", opDef.getPosition(), args, true);
		} else if (opDef.getId() == IntrinsicOperators.RLIKE) {
			return opDef.prettyPrint(" ~ ", opDef.getPosition(), args, true);
		} else if (opDef.getId() == IntrinsicOperators.AND) {
			return opDef.prettyPrint(" AND ", opDef.getPosition(), args, true);
		} else if (opDef.getId() == IntrinsicOperators.OR) {
			return opDef.prettyPrint(" OR ", opDef.getPosition(), args, true);
		} else if (opDef.getId() == IntrinsicOperators.SUBTRACTION && args.length == 2) {
			//
			// rule: if A-B where A is timestamp and B is date (resp B and
			// A...), then cast the Timestamp as a Date
			//
			if (piece.getParamTypes()[0].getDomain() == IDomain.TIMESTAMP
					&& piece.getParamTypes()[1].getDomain() == IDomain.DATE) {
				args[0] = "CAST(" + args[0] + " AS DATE)";
			} else if (piece.getParamTypes()[0].getDomain() == IDomain.DATE
					&& piece.getParamTypes()[1].getDomain() == IDomain.TIMESTAMP) {
				args[1] = "CAST(" + args[1] + " AS DATE)";
			}
		} else if (opDef.getId() == IntrinsicOperators.COUNT_DISTINCT) {
			return "COUNT(DISTINCT (" + args[0] + "))";
		}
		// let the opDef do the job...
		return opDef.prettyPrint(args, true);
	}

	@Override
	public boolean canRender(String id) {
		return getProvider().canRender(id);
	}

	@Override
	public List<String> canRender() {
		return getProvider().canRender();
	}

	@Override
	public ISkinFeatureSupport getFeatureSupport(String featureID) {
		return getProvider().getFeatureSupport(this, featureID);
	}

	public boolean isCreatingSkinSucess() {
		return creatingSkinSucess;
	}

	protected DateFormat getDateFormat() {
		return dateFormat;
	}

	protected DateFormat getTimestampFormat() {
		return timestampFormat;
	}

	protected void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	protected DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}

	protected void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	protected String getIdentifier_quote() {
		return identifier_quote;
	}

	protected void setIdentifier_quote(String identifier_quote) {
		this.identifier_quote = identifier_quote;
	}

	protected String getLiteral_quote() {
		return literal_quote;
	}

	protected void setLiteral_quote(String literal_quote) {
		this.literal_quote = literal_quote;
	}

	@Override
	public String quoteEndOfStatement(String statement) {
		return statement + endOfStatement_quote;
	}

	protected String getEndOfStatement_quote() {
		return endOfStatement_quote;
	}

	protected void setEndOfStatement_quote(String endOfStatement_quote) {
		this.endOfStatement_quote = endOfStatement_quote;
	}

	@Override
	public String renderEmptyFromClause() {
		return "-- EMPTY FROM";
	}

	@Override
	public Statement prepareStatement(SelectStatement statement) {
		return statement;
	}

}
