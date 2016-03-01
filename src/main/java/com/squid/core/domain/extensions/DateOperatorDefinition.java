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
import java.util.Hashtable;
import java.util.List;

import com.squid.core.domain.DomainMetaDomain;
import com.squid.core.domain.DomainNumericConstant;
import com.squid.core.domain.DomainStringConstant;
import com.squid.core.domain.IDomain;
import com.squid.core.domain.IDomainMetaDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

public class DateOperatorDefinition extends OperatorDefinition {

	public static final String DATE_BASE = "com.squid.domain.operator.date.";
	public static final String DATE_MONTHS_BETWEEN = DATE_BASE+"MONTHS_BETWEEN";
	public static final String DATE_SUB = DATE_BASE+"DATE_SUB";
	public static final String DATE_ADD = DATE_BASE+"DATE_ADD";
	public static final String DATE_INTERVAL = DATE_BASE+"DATE_INTERVAL";
	public static final String CURRENT_DATE = DATE_BASE+"CURRENT_DATE";
	public static final String CURRENT_TIMESTAMP = DATE_BASE+"CURRENT_TIMESTAMP";
	public static final String TO_UNIXTIME = DATE_BASE+"TO_UNIXTIME";
	public static final String FROM_UNIXTIME = DATE_BASE+"FROM_UNIXTIME";
	
	public static final Hashtable<String, IDomain> periods =  new Hashtable<String, IDomain>();
	

	public DateOperatorDefinition(String name, String ID, IDomain domain) {
		super(name,ID,PREFIX_POSITION,name,domain);
		init();
	}

	public DateOperatorDefinition(String name, String ID, IDomain domain, int categoryType) {
		super(name,ID,PREFIX_POSITION,name,domain, categoryType);
		init();
	}
	public void init() {
		periods.put("SECOND", IDomain.TIMESTAMP);
		periods.put("MINUTE", IDomain.TIMESTAMP);
		periods.put("HOUR", IDomain.TIMESTAMP);
		periods.put("DAY", IDomain.DATE);
		periods.put("MONTH", IDomain.DATE);
		periods.put("YEAR", IDomain.DATE);
	}

	@Override
	public int getType() {
		return ALGEBRAIC_TYPE;
	}

	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()<=1 && (CURRENT_DATE.equals(this.getExtendedID()) || CURRENT_TIMESTAMP.equals(this.getExtendedID()))) {
			return OperatorDiagnostic.IS_VALID;
		} else if (imageDomains.size()>0 && imageDomains.size()<=2) {
			if (imageDomains.size()==1 && (TO_UNIXTIME.equals(this.getExtendedID()) || FROM_UNIXTIME.equals(this.getExtendedID()))){
				if (TO_UNIXTIME.equals(this.getExtendedID()) && imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP)
						||FROM_UNIXTIME.equals(this.getExtendedID()) && imageDomains.get(0).isInstanceOf(IDomain.NUMERIC)) {
					return OperatorDiagnostic.IS_VALID;
				} else {
					return new OperatorDiagnostic("Invalid parameter",getName()+"("+(TO_UNIXTIME.equals(this.getExtendedID())?"timestamp":"integer")+")");
				}
			}
			int cpt = 0;
			for (IDomain domain : imageDomains) {
				cpt++;
				if (!domain.isInstanceOf(IDomain.TEMPORAL) && cpt==1 || cpt==2 && !domain.isInstanceOf(IDomain.TEMPORAL) && !domain.isInstanceOf(IDomain.NUMERIC)) {
					return new OperatorDiagnostic("Invalid type of parameters",getName()+"(temporal, temporal or integer)");
				}
			}
		} else if (imageDomains.size()==3) {
			if (DATE_INTERVAL.equals(this.getExtendedID())){
				if (!imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP) || !imageDomains.get(1).isInstanceOf(IDomain.TIMESTAMP) || !(imageDomains.get(2) instanceof DomainStringConstant)) {
					return new OperatorDiagnostic("Invalid type of parameters",getName()+"(timestamp, timestamp, string)");
				} else {
					String unit = ((DomainStringConstant)imageDomains.get(2)).getValue();
					if (!"SECOND".equals(unit) && !"MINUTE".equals(unit) && !"HOUR".equals(unit) && !"DAY".equals(unit)) {
						if ("MONTH".equals(unit) || "YEAR".equals(unit)) {
							return new OperatorDiagnostic("Invalid unit","Please use function MONTH_BETWEEN instead");
						}
						return new OperatorDiagnostic("Invalid unit",getName()+"timestamp, timestamp, unit (SECOND,MINUTE,HOUR,DAY)");
					}
				}
			} else {
				if (DATE_ADD.equals(this.getExtendedID()) || DATE_SUB.equals(this.getExtendedID())){
					if (!imageDomains.get(0).isInstanceOf(IDomain.DATE)) {
						return new OperatorDiagnostic("Invalid type of parameter #1, expecting a DATE or TIMESTAMP but found "+ imageDomains.get(0).toString(),getName()+"(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)");
					}else if (!(imageDomains.get(1) instanceof DomainNumericConstant)) {
						return new OperatorDiagnostic("Invalid type of parameter #2, expecting a NUMERIC CONSTANT but found "+ imageDomains.get(1).toString(),getName()+"(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)");
					} else if (!(imageDomains.get(2) instanceof DomainStringConstant)) {
						return new OperatorDiagnostic("Invalid type of parameter #3, expecting a STRING CONSTANT {SECOND,MINUTE,HOUR,DAY,MONTH,YEAR} but found "+ imageDomains.get(0).toString(),getName()+"(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)");
					} else {
						double d = ((DomainNumericConstant)imageDomains.get(1)).getValue();
						if (Math.floor(d)!=d || Math.abs(d)!=d) {
							return new OperatorDiagnostic("Invalid interval, it must be a positive integer",getName()+"(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)");
						}
						String unit = ((DomainStringConstant)imageDomains.get(2)).getValue();
						if (!"SECOND".equals(unit) && !"MINUTE".equals(unit) && !"HOUR".equals(unit) && !"DAY".equals(unit) && !"MONTH".equals(unit) && !"YEAR".equals(unit)) {
							return new OperatorDiagnostic("Invalid unit",getName()+"(date or timestamp, interval (integer), unit (SECOND,MINUTE,HOUR,DAY,MONTH,YEAR)");
						}
					}
				} else {
					return new OperatorDiagnostic("Invalid function",getName());
				}
			}
		}
		if (imageDomains.size()==2) {
			if (DATE_ADD.equals(this.getExtendedID())) {
				if (imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP) && imageDomains.get(1).isInstanceOf(IDomain.INTERVAL)
						|| imageDomains.get(0).isInstanceOf(IDomain.DATE) && !imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP) && imageDomains.get(1).isInstanceOf(IDomain.NUMERIC)) {
					return OperatorDiagnostic.IS_VALID;
				} else {
					return new OperatorDiagnostic("It is not possible to add 2 dates",getName()+"(date or timestamp, interval)");
				}
			} else if (DATE_INTERVAL.equals(this.getExtendedID())){
				return new OperatorDiagnostic("Invalid number of parameters",getName());
			} else {
				return OperatorDiagnostic.IS_VALID;
			}
		} else if (imageDomains.size()==3 && (DATE_INTERVAL.equals(this.getExtendedID()) || DATE_SUB.equals(this.getExtendedID()) || DATE_ADD.equals(this.getExtendedID()))){
			return OperatorDiagnostic.IS_VALID;
		} else {
			return new OperatorDiagnostic("Invalid number of parameters",getName());
		}
	}

	@Override
	public ExtendedType computeExtendedType(ExtendedType[] types) {
		return fixExtendedTypeDomain(computeRawExtendedType(types), types);
	}
	
	public ExtendedType computeRawExtendedType(ExtendedType[] types) {
		if (CURRENT_DATE.equals(this.getExtendedID())) {
			return ExtendedType.DATE;
		} else if (FROM_UNIXTIME.equals(this.getExtendedID())) {
			return ExtendedType.TIMESTAMP;
		} else if (TO_UNIXTIME.equals(this.getExtendedID())) {
			return ExtendedType.INTEGER;
		} else if (CURRENT_TIMESTAMP.equals(this.getExtendedID())) {
			return ExtendedType.TIMESTAMP;
		} else if (DATE_MONTHS_BETWEEN.equals(this.getExtendedID())) {
			return new ExtendedType(IDomain.NUMERIC,Types.FLOAT,0,0);
		} else if (DATE_INTERVAL.equals(this.getExtendedID())) {
			return new ExtendedType(IDomain.NUMERIC,Types.INTEGER,0,0);
		} else {
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
				if (DATE_SUB.equals(this.getExtendedID()) || DATE_ADD.equals(this.getExtendedID())) {
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
			}
		}
		return new ExtendedType(IDomain.NUMERIC,Types.INTEGER,0,0);
	}
	
	 @Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		 IDomain rawDomain = computeRawImageDomain(imageDomains);
		 for (IDomain domain : imageDomains) {
			 if (domain.isInstanceOf(DomainMetaDomain.META)) {
				 return ((IDomainMetaDomain)domain).createMetaDomain(rawDomain);
			 }
		 }
		 //
		 return rawDomain;
	}
	 
	public IDomain computeRawImageDomain(List<IDomain> imageDomains) {
		if (CURRENT_DATE.equals(this.getExtendedID())) {
			return IDomain.DATE;
		} else if (FROM_UNIXTIME.equals(this.getExtendedID())) {
			return IDomain.TIMESTAMP;
		} else if (TO_UNIXTIME.equals(this.getExtendedID())) {
			return IDomain.NUMERIC;
		} else if (CURRENT_TIMESTAMP.equals(this.getExtendedID())) {
			return IDomain.TIMESTAMP;
		} else if (DATE_MONTHS_BETWEEN.equals(this.getExtendedID())) {
			return IDomain.NUMERIC;
		} else if (DATE_INTERVAL.equals(this.getExtendedID())) {
			return IDomain.NUMERIC;
		} else {
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
				if (DATE_SUB.equals(this.getExtendedID()) || DATE_ADD.equals(this.getExtendedID())) {
					if (imageDomains.get(0).isInstanceOf(IDomain.DATE) && imageDomains.get(0).isInstanceOf(IDomain.TIMESTAMP)==false) {
						if (imageDomains.get(2) instanceof DomainStringConstant) {
							String unit = ((DomainStringConstant)imageDomains.get(2)).getValue();
							if ("SECOND".equals(unit) || "MINUTE".equals(unit) || "HOUR".equals(unit)) {
								return IDomain.TIMESTAMP;
							} else {
								return IDomain.DATE;
							}
						}
					}
					return IDomain.TIMESTAMP;
				}
			}
		}
		return IDomain.NUMERIC;
	 }
	
}
