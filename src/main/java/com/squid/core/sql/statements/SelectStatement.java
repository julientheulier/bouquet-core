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
package com.squid.core.sql.statements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.squid.core.database.impl.DataSourceReliable.FeatureSupport;
import com.squid.core.sql.model.Aliaser;
import com.squid.core.sql.model.Scope;
import com.squid.core.sql.render.ExpressionListPiece;
import com.squid.core.sql.render.IFromPiece;
import com.squid.core.sql.render.IOrderByPiece;
import com.squid.core.sql.render.IPiece;
import com.squid.core.sql.render.ISelectPiece;
import com.squid.core.sql.render.IWherePiece;
import com.squid.core.sql.render.RenderingException;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SelectPiece;
import com.squid.core.sql.render.groupby.GroupByPiece;
import com.squid.core.sql.render.groupby.IGroupByPiece;

public class SelectStatement 
extends Statement
implements ISelectStatement
{
	
	
	public interface SampleFeatureSupport extends FeatureSupport {
		
		public String SELECT_SAMPLE = SELECT+".SAMPLE";// how to use the sample feaute if supported
		
		public boolean isPercentageSupported();
		public boolean isCountSupported();
		
	}
	
	public static final int SELECT_DISTINCT = 1<<0;
	public static final int SELECT_ALL = 1<<1;
	
	private int style = 0;
	
	private Aliaser aliaser;

	private ArrayList<ISelectPiece> select = new ArrayList<ISelectPiece>();
	private ArrayList<IFromPiece> from = new ArrayList<IFromPiece>();
	private ArrayList<IWherePiece> where = new ArrayList<IWherePiece>();
	private ArrayList<IOrderByPiece> orderBy = new ArrayList<IOrderByPiece>();
	
	private long limitValue = -1;// means no Limit
	private long offsetValue = -1;// means no Offset
	
	private IGroupByPiece groupByPiece;
	private String comment = null;
	private String intoTemporaryTableName;
	
	public SelectStatement() {
		this.aliaser = new Aliaser();
		this.groupByPiece = createGroupByPiece();// late binding => pb d'index...
	}
	
	public SelectStatement(Aliaser aliaser) {
		this.aliaser = aliaser!=null?aliaser:new Aliaser();
		this.groupByPiece = createGroupByPiece();// late binding
	}
	
	/**
	 * add a new comment to the existing comment
	 * @param comment
	 */
	public void addComment(String comment) {
		this.comment = this.comment==null?comment:this.comment+"\n"+comment;
	}
	
	/**
	 * set or replace existing comment
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return comment!=null?comment:"";
	}
	
	public Aliaser getAliaser() {
		return aliaser;
	}

	public int getStyle() {
		return style;
	}

	/**
	 * A combination of SELECT_DISTINCT, SELECT_ALL
	 * @param style
	 */
	public void setStyle(int style) {
		this.style = style;
	}
	
	public boolean isStyleDistinct() {
		return (style&SELECT_DISTINCT)!=0;
	}
	
	public boolean isStyleAsterisk() {
		return (style&SELECT_ALL)!=0;
	}
	
	public List<ISelectPiece> getSelectPieces() {
		return select;
	}
	
	public List<IFromPiece> getFromPieces() {
		return from;
	}
	
	public List<IWherePiece> getFilterWherePieces(int type) {
		ArrayList<IWherePiece> results = new ArrayList<IWherePiece>();
		for (IWherePiece piece : where) {
			if ((piece.getType()&type)>0) {
				results.add(piece);
			}
		}
		return Collections.unmodifiableList(results);// this is to avoid undetected modification of the list
	}
	
	public boolean hasFilterWherePieces(int type) {
		for (IWherePiece piece : where) {
			if ((piece.getType()&type)>0) {
				return true;
			}
		}
		return false;
	}

	public List<IWherePiece> getConditionalPieces() {
		return where;
	}
	
	public List<IWherePiece> getWherePieces() {
		return getFilterWherePieces(IWherePiece.WHERE);
	}
	
	public boolean hasWherePieces() {
		return hasFilterWherePieces(IWherePiece.WHERE);
	}
	
	public List<IWherePiece> getHavingPieces() {
		return getFilterWherePieces(IWherePiece.HAVING);
	}
	
	public boolean hasHavingPieces() {
		return hasFilterWherePieces(IWherePiece.HAVING);
	}
	
	public IGroupByPiece getGroupByPiece() {
		if (groupByPiece==null) {
			this.groupByPiece = createGroupByPiece();
		}
		return groupByPiece;
	}
	
	protected IGroupByPiece createGroupByPiece() {
		return new GroupByPiece();
	}

	public List<IOrderByPiece> getOrderByPieces() {
		return orderBy;
	}
	
	public void setOrderByPieces( ArrayList<IOrderByPiece>  obp){
		this.orderBy = obp; 
	}
	
	public boolean hasOrderByPieces () {
		return !orderBy.isEmpty();
	}
	
	public List<IWherePiece> getQualifyPieces() {
		return getFilterWherePieces(IWherePiece.QUALIFY);
	}
	
	public boolean hasQualifyPieces () {
		return hasFilterWherePieces(IWherePiece.QUALIFY);
	}

	public SelectPiece createSelectPiece(Scope scope, IPiece piece) {
		if (piece instanceof ExpressionListPiece){
			return new SelectPiece(scope,piece,null);
		} else {
			return new SelectPiece(scope,piece,getAliaser().getUniqueAlias());
		}
	}

	public SelectPiece createSelectPiece(Scope scope, IPiece piece, String baseName) {
		return createSelectPiece(scope, piece, baseName, true);
	}
	
	public SelectPiece createSelectPiece(Scope scope, IPiece piece, String baseName, boolean useAlias, boolean normalizeAlias) {
		if (piece instanceof ExpressionListPiece || !useAlias){
			return new SelectPiece(scope,piece,null);
		} else {
			String alias = getAliaser().getUniqueAlias(baseName, normalizeAlias);
			SelectPiece select = new SelectPiece(scope,piece,alias);
			if (baseName!=null) {
				select.quoteAlias(true);
			}
			return select;
		}
	}
	
	public SelectPiece createSelectPiece(Scope scope, IPiece piece, String baseName, boolean useAlias) {
	    return createSelectPiece(scope, piece, baseName, useAlias, true);
    }
	
	/**
	 * set the LIMIT value for the select query
	 * @param value
	 */
	public void setLimitValue(long value) {
	    this.limitValue = value;
	}
	
	/**
	 * return the LIMIT value for the select query
	 * @return
	 */
	public long getLimitValue() {
        return limitValue;
    }
	
	/**
	 * return true if a LIMIT value is set
	 * @return
	 */
	public boolean hasLimitValue() {
	    return limitValue>=0;
	}
    
    /**
     * set the OFFSET value for the select query
     * @param value
     */
    public void setOffsetValue(long value) {
        this.offsetValue = value;
    }
    
    /**
     * return the OFFSET value for the select query
     * @return
     */
    public long getOffsetValue() {
        return offsetValue;
    }
    
    /**
     * return true if a OFFSET value is set
     * @return
     */
    public boolean hasOffsetValue() {
        return offsetValue>=0;
    }
	
	@Override
	public String getTemplateId() {
		//return "com.squid.core.sql2.statements.SelectStatement";
		return "selectStatement";
	}
	
	@Override
	public String render(SQLSkin skin) throws RenderingException {
		String render = super.render(skin);
		if (comment!=null) {
			render = skin.quoteComment(comment)+"\n"+render;
		}
		return render;
	}

	/**
	 * supporting SELECT ... INTO TEMPORARY TABLE
	 * @param tempTableName
	 */
	public void setIntoTemporaryTableName(String tempTableName) {
		this.intoTemporaryTableName = tempTableName;
	}
	
	public String getIntoTemporaryTableName() {
		return intoTemporaryTableName;
	}
	
	public boolean hasIntoTemporaryTable() {
		return intoTemporaryTableName!=null;
	}

}
