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
package com.squid.core.expression;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.sql.render.SQLSkin;

public class DateConstant extends NamedExpression implements ConstantValue
{
	
	private Date value;
	
	public DateConstant(Date value) {
		this.value = value;
	}
	
	@Override
	public Object getValue() {
		return this.value;
	}
	
	public Date getIntrinsicValue() {
		return value;
	}

	@Override
	public ExtendedType computeType(SQLSkin skin) {
		ExtendedType type = this.getImageDomain().computeType(skin);
		return type;
	}

	@Override
	public IDomain getImageDomain() {
		return IDomain.DATE;
	}

	@Override
	public IDomain getSourceDomain() {
		return IDomain.NULL;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public String prettyPrint(PrettyPrintOptions options) {
	    DateFormat FormatDateFormat = new SimpleDateFormat("d/M/yyyy");// remember, it's not thread safe
		return "DATE(\""+FormatDateFormat.format(value)+"\")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateConstant other = (DateConstant) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
