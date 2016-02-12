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
package com.squid.core.domain.extensions;

import java.sql.Types;
import java.util.List;

import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.domain.vector.VectorDomain;

public class CastOperatorDefinition extends OperatorDefinition {

	public static final String CAST_BASE = "com.squid.domain.operator.cast.";
	public static final String TO_CHAR = CAST_BASE + "TO_CHAR";
	public static final String TO_DATE = CAST_BASE + "TO_DATE";
	public static final String TO_NUMBER = CAST_BASE + "TO_NUMBER";
	public static final String TO_TIMESTAMP = CAST_BASE + "TO_TIMESTAMP";
	public static final String TO_INTEGER = CAST_BASE + "TO_INTEGER";

	public static final int DEFAULT_LENGTH_FOR_STRING = 255;

	public CastOperatorDefinition(String name, String ID, IDomain domain) {
		super(name, ID, PREFIX_POSITION, name, domain);
	}

	public CastOperatorDefinition(String name, String ID, IDomain domain,
			int categoryType) {
		super(name, ID, PREFIX_POSITION, name, domain, categoryType);
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return ALGEBRAIC_TYPE;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size() > 0 && imageDomains.size() <= 3) {
			if (TO_DATE.equals(getExtendedID())
					|| TO_TIMESTAMP.equals(getExtendedID())) {
				if (imageDomains.size() <= 2) {
					if (imageDomains.size() == 1) {
						if (imageDomains.get(0).isInstanceOf(IDomain.DATE) == false) {
							return new OperatorDiagnostic(
									"Invalid type for parameter #1, is "+imageDomains.get(0).getName()+" expecting "+IDomain.DATE.getName()
									, getName()+"(timestamp) or "+getName()+"(string,format)");
						}
					} else if (imageDomains.size() == 2) {
						if (!imageDomains.get(0)
							.isInstanceOf(IDomain.STRING)) {
							return new OperatorDiagnostic(
								"Invalid type for parameter #1, is "+imageDomains.get(0).getName()+" expecting "+IDomain.STRING.getName()
								, getName()+"(timestamp) or "+getName()+"(string,format)");
						} else if (!imageDomains.get(1)
								.isInstanceOf(IDomain.STRING)) {
							return new OperatorDiagnostic(
									"Invalid type for parameter #1, is "+imageDomains.get(0).getName()+" expecting "+IDomain.STRING.getName()
									, getName()+"(timestamp) or "+getName()+"(string,format)");
						} 
					}
				} else {
					return new OperatorDiagnostic(
							"Invalid number of parameters", getName()
									+ "(timestamp) or " + getName()
									+ "(string,format)");
				}
			} else if (TO_CHAR.equals(getExtendedID())) {
				if (imageDomains.size() <= 2) {
					if (imageDomains.size() == 2
							&& !imageDomains.get(1)
									.isInstanceOf(IDomain.STRING)) {
						return new OperatorDiagnostic(
								"Invalid type of parameters", getName()
										+ "(any,format)");
					}
				} else {
					return new OperatorDiagnostic("Invalid type of parameters",
							getName() + "(any,format)");
				}
			} else if (TO_NUMBER.equals(getExtendedID())) {
				if (imageDomains.get(0).isInstanceOf(IDomain.STRING) == false
						&& imageDomains.get(0).isInstanceOf(IDomain.NUMERIC) == false) {
					return new OperatorDiagnostic(
							"Invalid type of parameters",
							getName()
									+ ": first parameter must be a numeric or a string");
				}
				if (imageDomains.size() == 2
						&& imageDomains.get(1) != IDomain.STRING) {
					return new OperatorDiagnostic(
							"Invalid number of parameters", getName()
									+ "(any,format)");
				} else if (imageDomains.size() == 3) {
					if (!(imageDomains.get(1) instanceof DomainNumericConstant)
							|| !(imageDomains.get(2) instanceof DomainNumericConstant)) {
						return new OperatorDiagnostic(
								"Invalid number of parameters", getName()
										+ "(any,size,precision)");
					} else {
						double d1 = ((DomainNumericConstant) imageDomains
								.get(1)).getValue();
						double d2 = ((DomainNumericConstant) imageDomains
								.get(2)).getValue();
						if (Math.floor(d1) != d1 || Math.floor(d2) != d2) {
							return new OperatorDiagnostic(
									"Invalid parameters size and/or precision, they must be integer",
									getName() + "(any,size,precision)");
						}
					}
				} else if (imageDomains.size() == 4
						&& (imageDomains.get(1) != IDomain.NUMERIC
								|| imageDomains.get(2) != IDomain.NUMERIC || imageDomains
								.get(3) != IDomain.STRING)) {
					return new OperatorDiagnostic(
							"Invalid number of parameters", getName()
									+ "(any,size,precision, format)");
				} else if (imageDomains.size() > 4) {
					return new OperatorDiagnostic(
							"Invalid number of parameters", getName()
									+ "(any,size,precision, format)");
				}
			} else if (TO_INTEGER.equals(getExtendedID())) {
				if (imageDomains.size() >= 2) {
					return new OperatorDiagnostic(
							"Invalid number of parameters", getName() + "(any)");
				}
			}
		} else {
			return new OperatorDiagnostic("Invalid number of parameters",
					getName());
		}
		return OperatorDiagnostic.IS_VALID;
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		ExtendedType castExtendedType = null;
		if (getExtendedID().equals(TO_CHAR)) {
			int size = getPieceLength(types);
			if (types.length == 2
					&& types[1].getDomain() instanceof DomainStringConstant) {
				size = ((DomainStringConstant) types[1].getDomain()).getValue()
						.length();
			}
			castExtendedType = new ExtendedType(IDomain.STRING, Types.VARCHAR,
					0, size);
		} else if (getExtendedID().equals(TO_DATE)) {
			castExtendedType = ExtendedType.DATE;
		} else if (getExtendedID().equals(TO_TIMESTAMP)) {
			castExtendedType = ExtendedType.TIMESTAMP;
		} else if (getExtendedID().equals(TO_NUMBER)) {
			castExtendedType = ExtendedType.FLOAT;
			if (types.length == 3) {
				int size = 15;
				int precision = 0;
				if (types[1].getDomain() instanceof DomainNumericConstant) {
					Double d = ((DomainNumericConstant) types[1].getDomain())
							.getValue();
					if (d != Double.NaN) {
						size = d.intValue();
					}
				}
				if (types[2].getDomain() instanceof DomainNumericConstant) {
					Double d = ((DomainNumericConstant) types[2].getDomain())
							.getValue();
					if (d != Double.NaN) {
						precision = d.intValue();
					}
				}
				castExtendedType = new ExtendedType(IDomain.NUMERIC,
						Types.DECIMAL, precision, size);
			}

		} else if (getExtendedID().equals(TO_INTEGER)) {
			castExtendedType = ExtendedType.INTEGER;
		}
		return castExtendedType;
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
		IDomain argumentToCast = imageDomains.get(0);
		boolean is_meta = argumentToCast.isInstanceOf(IDomain.META);
		IDomain computedDomain = argumentToCast;
		if (getExtendedID().equals(TO_CHAR)) {
			computedDomain = IDomain.STRING;
		} else if (getExtendedID().equals(TO_DATE)) {
			computedDomain = IDomain.DATE;
		} else if (getExtendedID().equals(TO_TIMESTAMP)) {
			computedDomain = IDomain.TIMESTAMP;
		} else if (getExtendedID().equals(TO_NUMBER)) {
			computedDomain = IDomain.CONTINUOUS;
		} else if (getExtendedID().equals(TO_INTEGER)) {
			computedDomain = IDomain.NUMERIC;
		}
		if (is_meta) {
			IDomainMetaDomain meta = (IDomainMetaDomain) argumentToCast;
			IDomain proxy = meta.createMetaDomain(computedDomain);
			if (proxy instanceof VectorDomain) {
				((VectorDomain) proxy).setSize(imageDomains.size());
			}
			return proxy;
		} else {
			return computedDomain;
		}
	}

	public int getPieceLength(ExtendedType[] types) {
		int length = DEFAULT_LENGTH_FOR_STRING;
		int scale = 0;
		int size = 0;
		if (types != null) {
			for (ExtendedType type : types) {
				if (type.getDomain() == IDomain.NUMERIC) {
					scale = type.getScale();
					size = type.getSize() + 1;
					if (scale > 0) {
						size++;
					}
					length = size;
				} else if (type.getDomain() == IDomain.STRING) {
					length = type.getSize();
				}
			}
		}
		return length;
	}
}
