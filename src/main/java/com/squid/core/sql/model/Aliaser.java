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
package com.squid.core.sql.model;

import java.util.HashSet;

import com.squid.core.sql.utils.SQLUtils;

/**
 * Something that creates Aliases
 * @author serge fantino
 *
 */
public class Aliaser {

    private String m_prefix = "a";
    private int m_uid = 1;
    
	private HashSet<String> usedAliases = new HashSet<String>();
    
    public Aliaser() {
		// TODO Auto-generated constructor stub
	}

    /**
     * return an Unique alias; always return a different value;
     * @return
     */
    public String getUniqueAlias() {
        String alias = m_prefix;
		for (;m_uid<100;++m_uid) {
			String aliasn = alias+m_uid;
			if (!usedAliases.contains(aliasn)) {
				usedAliases.add(aliasn);
				return aliasn;
			}
		}
		m_prefix = m_prefix+"a";
		m_uid = 1;
		return getUniqueAlias();
    }

	public String getUniqueAlias(String alias, boolean normalizeAlias) {
		if (alias==null) {
			return getUniqueAlias();
		} else {
		    if(normalizeAlias) {
		        alias = SQLUtils.normalizeColumnName(alias,m_prefix);
		    }
			if (!usedAliases.contains(alias)) {
				usedAliases.add(alias);
				return alias;
			} else {
				for (int n=1;true;++n) {
					String aliasn = alias+n;
					if (!usedAliases.contains(aliasn)) {
						usedAliases.add(aliasn);
						return aliasn;
					}
				}
			}
		}
	}
    
    /**
     * Get prefix of alias
     * @return
     * 
     */
    public String getPrefix(){
    	return m_prefix;
    }
}
