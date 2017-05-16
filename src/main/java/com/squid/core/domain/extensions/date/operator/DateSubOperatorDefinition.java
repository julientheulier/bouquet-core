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
package com.squid.core.domain.extensions.date.operator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.squid.core.domain.DomainDate;
import com.squid.core.domain.DomainInterval;
import com.squid.core.domain.DomainMetaDomain;
import com.squid.core.domain.DomainNumeric;
import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.DomainTemporal;
import com.squid.core.domain.DomainTimestamp;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * Created by lrabiet on 03/05/16.
 */
public class DateSubOperatorDefinition extends DateOperatorDefinition {
	public static final String ID = DateOperatorDefinition.DATE_BASE + "DATE_SUB";

	public DateSubOperatorDefinition(String name, IDomain domain) {
		super(name, ID, domain);
	}

	public DateSubOperatorDefinition(String name, String ID, IDomain domain) {
		super(name, ID, domain);
	}

	@Override
	public List<String> getHint() {
		List<String> hint = new ArrayList<String>();
		hint.add("Substract an integer or timestamp (second argument) to the given timestamp, date or temporal (first argument)");
		hint.add("Substract an integer or timestamp (second argument) to the given timestamp, date or temporal (first argument)");
		hint.add("Substract an integer or timestamp (second argument) to the given timestamp, date or temporal (first argument)");
		//(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)"
		hint.add("Substract an interval (second argument) to the given timestamp (first argument)");
		hint.add("Substract an integer or timestamp (second argument) to the given timestamp, date or temporal (first argument)");
		//(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)"
		hint.add("SUBTRACT using (datex or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)");
		return hint;
	}

	@Override
	public List getParametersTypes() {
		List poly = new ArrayList<List>();
		List type = new ArrayList<IDomain>();

		IDomain temporal1 = new DomainTemporal();
		temporal1.setContentAssistLabel("temporal");
		IDomain temporal2 = new DomainTemporal();
		temporal2.setContentAssistLabel("temporal");
		IDomain timestamp1 = new DomainTimestamp();
		timestamp1.setContentAssistLabel("timestamp");
		IDomain num2 = new DomainNumeric();
		num2.setContentAssistLabel("num");
		IDomain date1 = new DomainDate();
		date1.setContentAssistLabel("date");
		IDomain interval2 = new DomainInterval();
		interval2.setContentAssistLabel("interval");
		IDomain numConst2 = new DomainNumericConstant();
		numConst2.setContentAssistLabel("numConst");
		IDomain stringConst3 = new DomainStringConstant("");
		stringConst3.setContentAssistLabel("unit");


		type.add(temporal1);
		type.add(num2);
		poly.add(type);

		type = new ArrayList<IDomain>();
		type.add(temporal1);
		type.add(temporal2);
		poly.add(type);

		type = new ArrayList<IDomain>();
		type.add(timestamp1);
		type.add(num2);
		poly.add(type);

		type = new ArrayList<IDomain>();
		type.add(timestamp1);
		type.add(interval2);
		poly.add(type);

		type = new ArrayList<IDomain>();
		type.add(date1);
		type.add(num2);
		poly.add(type);

		type = new ArrayList<IDomain>();
		type.add(date1);
		type.add(numConst2);
		type.add(stringConst3);
		poly.add(type);

		return poly;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if(imageDomains.size()>2){
			if(imageDomains.get(1).isInstanceOf(DomainNumericConstant.DOMAIN) && !imageDomains.get(1).isInstanceOf(IDomain.ANY) && !imageDomains.get(2).isInstanceOf(IDomain.ANY) && imageDomains.get(2).isInstanceOf(DomainStringConstant.DOMAIN)) {
				double d = ((DomainNumericConstant)imageDomains.get(1)).getValue();
				if (Math.floor(d)!=d || Math.abs(d)!=d) {
					return new OperatorDiagnostic("Invalid interval, it must be a positive integer",getName()+"(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)");
				}
				String unit = ((DomainStringConstant)imageDomains.get(2)).getValue();
				if (!"SECOND".equalsIgnoreCase(unit) && !"MINUTE".equalsIgnoreCase(unit) && !"HOUR".equalsIgnoreCase(unit) && !"DAY".equalsIgnoreCase(unit) && !"MONTH".equalsIgnoreCase(unit) && !"YEAR".equalsIgnoreCase(unit)) {
					return new OperatorDiagnostic("Invalid unit",getName()+"(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)");
				}
			}
		}
		return super.validateParameters(imageDomains);

	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return fixExtendedTypeDomain(computeExtendedTypeRaw(types), types);
	}

	@Override
	public ExtendedType computeExtendedTypeRaw(ExtendedType[] types) {
		if (types.length==2) {
			if (types[0].getDomain().isInstanceOf(IDomain.TIMESTAMP) && types[1].getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
				return ExtendedType.INTERVAL;
			} else if (types[0].getDomain().isInstanceOf(IDomain.DATE) && types[0].getDomain().isInstanceOf(IDomain.TIMESTAMP)==false && types[1].getDomain().isInstanceOf(IDomain.DATE) && types[1].getDomain().isInstanceOf(IDomain.TIMESTAMP)==false) {
				return new ExtendedType(IDomain.NUMERIC,Types.INTEGER,0,0);
			} else if (types[0].getDomain().isInstanceOf(IDomain.DATE) && types[0].getDomain().isInstanceOf(IDomain.TIMESTAMP)==false) {
				if (types[1].getDomain().isInstanceOf(IDomain.INTERVAL)) {
					if (types[1].getScale()==1 || types[1].getScale()==2 || types[1].getScale()==3) {
						return ExtendedType.DATE;
					} else {
						return ExtendedType.TIMESTAMP;
					}
				} else if (types[1].getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
					return ExtendedType.INTERVAL;
				} else if (types[1].getDomain().isInstanceOf(IDomain.NUMERIC)) {
					return ExtendedType.DATE;
				} else {
					return new ExtendedType(IDomain.NUMERIC,Types.INTEGER,0,0);
				}
			} else if (types[0].getDomain().isInstanceOf(IDomain.TIMESTAMP)) {
				if (types[1].getDomain().isInstanceOf(IDomain.DATE) && types[1].getDomain().isInstanceOf(IDomain.TIMESTAMP)==false) {
					return ExtendedType.INTERVAL;
				}
				return ExtendedType.TIMESTAMP;
			} else if (types[0].getDomain().isInstanceOf(IDomain.INTERVAL) ){
				return ExtendedType.INTERVAL;
			}
		} else if (types.length==3) {
			if (types[0].getDomain().isInstanceOf(IDomain.DATE) && types[0].getDomain().isInstanceOf(IDomain.TIMESTAMP)==false) {
				if (types[2].getDomain() instanceof DomainStringConstant) {
					String unit = ((DomainStringConstant)types[2].getDomain()).getValue();
					if ("SECOND".equals(unit) || "MINUTE".equals(unit) || "HOUR".equals(unit)) {
						return ExtendedType.TIMESTAMP;
					} else {
						return ExtendedType.DATE;
					}
				}
			}
			return ExtendedType.TIMESTAMP;
		}

		return new ExtendedType(IDomain.NUMERIC,Types.INTEGER,0,0);
	}

	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		IDomain rawDomain = computeRawImageDomain(imageDomains);
		for (IDomain domain : imageDomains) {
			if (!domain.isInstanceOf(IDomain.ANY) && domain.isInstanceOf(DomainMetaDomain.META)) {
				return ((IDomainMetaDomain)domain).createMetaDomain(rawDomain);
			}
		}
		//
		return rawDomain;
	}

	public IDomain computeRawImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()==2) {
			if (imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP) && imageDomains.get(1).isInstanceOf(IDomain.TIMESTAMP)) {
				return IDomain.INTERVAL;
			} else if (imageDomains.get(0).isInstanceOf(IDomain.DATE) && imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP)==false && imageDomains.get(1).isInstanceOf(IDomain.DATE) && imageDomains.get(1).isInstanceOf(IDomain.TIMESTAMP)==false) {
				return IDomain.NUMERIC;
			} else if (imageDomains.get(0).isInstanceOf(IDomain.DATE) && imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP)==false) {
				if (imageDomains.get(1).isInstanceOf(IDomain.INTERVAL)) {
					return IDomain.DATE;
				} else if (imageDomains.get(1).isInstanceOf(IDomain.TIMESTAMP)) {
					return IDomain.INTERVAL;
				} else if (imageDomains.get(1).isInstanceOf(IDomain.NUMERIC)) {
					return IDomain.DATE;
				} else {
					return IDomain.NUMERIC;
				}
			} else if (imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP)) {
				if (imageDomains.get(1).isInstanceOf(IDomain.DATE) && imageDomains.get(1).isInstanceOf(IDomain.TIMESTAMP)==false) {
					return IDomain.INTERVAL;
				}
				return IDomain.TIMESTAMP;
			} else if (imageDomains.get(0).isInstanceOf(IDomain.INTERVAL) ){
				return IDomain.INTERVAL;
			}
		} else if (imageDomains.size()==3) {
			if (imageDomains.get(0).isInstanceOf(IDomain.DATE) && imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP)==false) {
				if (imageDomains.get(2) instanceof DomainStringConstant) {
					String unit = ((DomainStringConstant)imageDomains.get(2)).getValue();
					if ("SECOND".equals(unit) || "MINUTE".equals(unit) || "HOUR".equals(unit)) {
						return IDomain.TIMESTAMP;
					} else {
						return IDomain.DATE;
					}
				}
				return IDomain.TIMESTAMP;
			}
		}
		return IDomain.NUMERIC;
	}
}
