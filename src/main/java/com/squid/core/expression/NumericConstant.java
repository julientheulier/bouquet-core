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

import com.squid.core.database.model.impl.DatabaseImpl;
import com.squid.core.database.model.impl.DatabaseProductImpl;
import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.sql.db.templates.SkinRegistry;
import com.squid.core.sql.render.SQLSkin;
import com.squid.core.sql.render.SQLSkinProxy;

import java.sql.Types;

public class NumericConstant implements ConstantValue
{
	
	private double value;
	
	public NumericConstant(double value) {
		this.value = value;
	}
	
	@Override
	public Object getValue() {
		return this.value;
	}
	
	public double getIntrinsicValue() {
		return value;
	}

	@Override
	public ExtendedType computeType(SQLSkin skin){
		Double intrinsic = Math.abs(this.getIntrinsicValue());
		// precision
		double decimalPart = intrinsic-intrinsic.longValue();
//			double decimalPart = value-value.intValue();
		int precision = 0;
		for (;decimalPart>0;precision++) {
			decimalPart = decimalPart*10;
			decimalPart = decimalPart-(int)decimalPart;
		}
		// size
		int size = intrinsic==0?1:1+(int) Math.log10(intrinsic);
		return skin.createExtendedType(this.getImageDomain(), Types.NULL,null,size,precision);
	}

	@Override
	public IDomain getImageDomain() {
		return new DomainNumericConstant(value);
	}

	@Override
	public IDomain getSourceDomain() {
		return IDomain.NULL;
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
	
	@Override
	public String prettyPrint() {
		return Double.toString(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		NumericConstant other = (NumericConstant) obj;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

}
