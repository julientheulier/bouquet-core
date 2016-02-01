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
package com.squid.core.sql.utils;

/**
 * SQL Utils
 * @author sfantino
 *
 */
public class SQLUtils {
	
	private static final int MAX_LENGTH = 30;
	

	public static String normalizeColumnName(String name, String defaultPrefix) {
		return normalizeColumnName(name,defaultPrefix,null);
	}

	/**
	 * Normalize the column name in oder to be compatible with ANY database naming constraint
	 * note: we should rely on the Skin to enforce that, but no Skin is available at that point.
	 *       the other solution would be to delay the alias creation, but that may not be easy...
	 *       
	 * @param name: the name to normalize
	 * @param defaultPrefix: use defaultPrefix if the name is null after normalization
	 * @param forcePostfix: if not null, add the postfix (and trunk the name if needed)
	 * 
	 * @return
	 */
	public static String normalizeColumnName(String name, String defaultPrefix, String forcePostfix) {
		// force alias to be lowercase - some database may not enforce the alias case (RS)
	    name = name.toLowerCase();
		String norm = "";
		for (int i=0;i<name.length();i++) {
			char c = name.charAt(i);
			if ("AZERTYUIOPMLKJHGFDSQWXCVBNazertyuiopmlkjhgfdsqwxcvbn1234567890_".indexOf(c)>=0) {
				norm += c;
			} else {
				if (norm.length()==0) {
					// ignore
				} else if (norm.charAt(norm.length()-1)!='_') {
					norm += "_";
				}
			}
		}
		//
		while (norm.length()>=1 && norm.charAt(norm.length()-1)=='_') {
			norm = norm.substring(0, norm.length()-1);
		}
		//
		if (norm.length()==0 || norm.equals("_")) {
			norm = defaultPrefix;
		}
		//
		if (forcePostfix==null) {
			if (norm.length()>MAX_LENGTH) {
				norm = norm.substring(0, MAX_LENGTH-1);
			}
		} else {
			if (norm.length()+forcePostfix.length()>MAX_LENGTH) {
				norm = norm.substring(0, MAX_LENGTH-1-forcePostfix.length());
			}
			norm += forcePostfix;
		}
		//
		return norm;
	}

	/**
	 * Note: that method will force name to uppercase
	 * @param name
	 * @param defaultPrefix
	 * @return
	 */
	public static String normalizeTableName(String name, String defaultPrefix) {
		name = name.toUpperCase();
		return normalizeColumnName(name,defaultPrefix);
	}

}
