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
package com.squid.core.sql.render;

/**
 * a piece that can get commented
 * @author sergefantino
 *
 */
public class CommentablePiece implements ICommentablePiece {
	
	private String comment;
	
	/**
	 * Add a comment to the piece; if many comments are added the renderer will use a multi-line comment
	 */
	@Override
	public void addComment(String comment) {
		if (this.comment==null) {
			this.comment = comment;
		} else {
			this.comment += '\n'+comment;
		}
	}
	
	protected boolean hasComment() {
		return comment!=null;
	}
	
	protected String renderComment(SQLSkin skin) {
		if (comment!=null) {
			return skin.quoteComment(comment);
		} else {
			return "";
		}
	}

}
