/*******************************************************************************
 * Copyright © Squid Solutions, 2016
 * <p/>
 * This file is part of Open Bouquet software.
 * <p/>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 * <p/>
 * There is a special FOSS exception to the terms and conditions of the
 * licenses as they are applied to this program. See LICENSE.txt in
 * the directory of this program distribution.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * <p/>
 * Squid Solutions also offers commercial licenses with additional warranties,
 * professional functionalities or services. If you purchase a commercial
 * license, then it supersedes and replaces any other agreement between
 * you and Squid Solutions (above licenses and LICENSE.txt included).
 * See http://www.squidsolutions.com/EnterpriseBouquet/
 *******************************************************************************/
package com.squid.core.domain.extensions.cast;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainNumeric;
import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainString;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;
import com.squid.core.domain.vector.VectorDomain;

/**
 * Created by lrabiet on 03/05/16.
 */
public class CastToNumberOperatorDefinition extends CastOperatorDefinition {
	public static final String ID = CastOperatorDefinition.CAST_BASE + "TO_NUMBER";

	public CastToNumberOperatorDefinition(String name, IDomain domain) {
		super(name, ID, domain);
		this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);
	}

	public CastToNumberOperatorDefinition(String name, String ID, IDomain domain) {
		super(name, ID, domain);
		this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.isEmpty()) return IDomain.UNKNOWN;
		IDomain argumentToCast = imageDomains.get(0);
		boolean is_meta = argumentToCast.isInstanceOf(IDomain.META);
		IDomain computedDomain = IDomain.CONTINUOUS;
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

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("Cast the number to number using the format");
		hint.add("Cast the string to number using the format");
		hint.add("Cast the number to number using the size and the precision");
		hint.add("Cast the number to number using the size and the precision");
		hint.add("Cast the number to number using the size, the precision and the format");
		hint.add("Cast the number to number using the size, the precision and the format");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		IDomain num1 = new DomainNumeric();
		num1.setContentAssistLabel("Num");
		IDomain string1 = new DomainString();
		string1.setContentAssistLabel("String");
		IDomain string2 = new DomainNumeric();
		string2.setContentAssistLabel("format");
		IDomain num2 = new DomainNumeric();
		num2.setContentAssistLabel("size");
		IDomain num3 = new DomainNumeric();
		num3.setContentAssistLabel("precision");
		IDomain string4 = new DomainNumeric();
		string4.setContentAssistLabel("format");

		type = new ArrayList<IDomain>();

		type.add(string1);
		type.add(string2);


		poly.add(type);
		type = new ArrayList<IDomain>();

		type.add(num1);
		type.add(num2);
		type.add(num3);

		poly.add(type);

		type = new ArrayList<IDomain>();

		type.add(num1);
		type.add(num2);
		type.add(num3);
		type.add(string4);

		poly.add(type);
		type = new ArrayList<IDomain>();

		type.add(string1);
		type.add(num2);
		type.add(num3);
		type.add(string4);

		poly.add(type);

		return poly;
	}

	//Not accepting ANY
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size() > 0 && imageDomains.size() <= 3) {
			if (TO_DATE.equals(getExtendedID())
					|| TO_TIMESTAMP.equals(getExtendedID())) {
				if (imageDomains.size() <= 2) {
					if (imageDomains.size() == 1) {
						if (imageDomains.get(0).isInstanceOf(IDomain.DATE) == false || imageDomains.get(0).isInstanceOf(IDomain.ANY)) {
							return new OperatorDiagnostic(
									"Invalid type for parameter #1, is "+imageDomains.get(0).getName()+" expecting "+IDomain.DATE.getName(), getName()
									+ "(timestamp)");
						}
					} else if (imageDomains.size() == 2) {
						if (!imageDomains.get(0)
								.isInstanceOf(IDomain.STRING) || imageDomains.get(0).isInstanceOf(IDomain.ANY)) {
							return new OperatorDiagnostic(
									"Invalid type for parameter #1, is "+imageDomains.get(0).getName()+" expecting "+IDomain.STRING.getName(), getName()
									+ "(string,format)");
						} else if (!imageDomains.get(1)
								.isInstanceOf(IDomain.STRING) || imageDomains.get(1).isInstanceOf(IDomain.ANY)) {
							return new OperatorDiagnostic(
									"Invalid type for parameter #1, is "+imageDomains.get(0).getName()+" expecting "+IDomain.STRING.getName(), getName()
									+ "(string,format)");
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
							&& (!imageDomains.get(1)
									.isInstanceOf(IDomain.STRING) || imageDomains.get(1).isInstanceOf(IDomain.ANY))) {
						return new OperatorDiagnostic(
								"Invalid type of parameters", getName()
								+ "(any,format)");
					}
				} else {
					return new OperatorDiagnostic("Invalid type of parameters",
							getName() + "(any,format)");
				}
			} else if (TO_NUMBER.equals(getExtendedID())) {
				if ((imageDomains.get(0).isInstanceOf(IDomain.STRING) == false
						//&& imageDomains.get(0).isInstanceOf(IDomain.NUMERIC) == false) || imageDomains.get(0).isInstanceOf(IDomain.ANY)
						)) {
					return new OperatorDiagnostic(
							"Invalid type of parameters",
							getName()
							+ ": first parameter must be a text");
				}
				if (imageDomains.size() == 2
						&& (imageDomains.get(1).isInstanceOf(IDomain.STRING)==false || imageDomains.get(1).isInstanceOf(IDomain.ANY))) {
					return new OperatorDiagnostic(
							"Invalid number of parameters", getName()
							+ "(any,format)");
				} else if (imageDomains.size() == 3) {
					if (!(imageDomains.get(1) instanceof DomainNumericConstant) || imageDomains.get(1).isInstanceOf(IDomain.ANY)
							|| !(imageDomains.get(2) instanceof DomainNumericConstant) || imageDomains.get(2).isInstanceOf(IDomain.ANY)) {
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
						&& (imageDomains.get(1).isInstanceOf(IDomain.NUMERIC)==false || imageDomains.get(1).isInstanceOf(IDomain.ANY)
						|| imageDomains.get(2).isInstanceOf(IDomain.NUMERIC)==false || imageDomains.get(2).isInstanceOf(IDomain.ANY) || imageDomains
						.get(3).isInstanceOf(IDomain.STRING)==false || imageDomains.get(3).isInstanceOf(IDomain.ANY))) {
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
		return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		ExtendedType castExtendedType = ExtendedType.FLOAT;
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

		return castExtendedType;
	}

}
