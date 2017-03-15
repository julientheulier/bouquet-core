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
package com.squid.core.domain.operators;

import com.squid.core.domain.IDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrabiet on 12/05/16.
 */
public class OneNumericArgOperatorDefinition extends OperatorDefinition {

    /**
     * create simple prefix operator with symbol equals to operator's name
     *
     * @param name
     * @param id
     * @param domain
     */
    public OneNumericArgOperatorDefinition(String name, int id, IDomain domain) {
        super(name, id, domain);
        this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);
    }


    /**
     * @param name
     * @param id
     * @param position
     * @param domain
     */
    public OneNumericArgOperatorDefinition(String name, int id, int position, IDomain domain) {
        super(name, id, position, domain);
        this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);

    }

    /**
     * Create infix operator
     *
     * @param name
     * @param id
     * @param symbol
     * @param domain
     */
    public OneNumericArgOperatorDefinition(String name, int id, String symbol, IDomain domain) {
        super(name, id, symbol, domain);
        this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);
    }

    /**
     * Create operator at the specified position...
     *
     * @param name
     * @param id
     * @param position
     * @param symbol
     * @param domain
     */
    public OneNumericArgOperatorDefinition(String name, int id, int position, String symbol, IDomain domain) {
        super(name, id, position, symbol, domain);
        this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);
    }


    public OneNumericArgOperatorDefinition(String name, String extendedID, int position, String symbol, IDomain domain) {
        super(name, extendedID, position, symbol, domain);
        this.setCategoryType(OperatorDefinition.NUMERIC_TYPE);
    }


    @Override
    public List<String> getHint() {
        List<String> hint = new ArrayList<String>();
        hint.add("Description");
        return hint;
    }

    @Override
    public List getParametersTypes() {
        List type = new ArrayList<IDomain>();
        type.add(IDomain.NUMERIC);

        List poly = new ArrayList<List>();
        poly.add(type);
        return poly;
    }

    @Override
    public int getType() {
        return MATHS_TYPE;
    }

}
