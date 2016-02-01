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
package com.squid.core.database.statistics;

public class ColumnStatistics extends ObjectStatistics {

    private Object min = null;
    private Object max = null;
    private float density = -1;
    
    /**
     * undefined statistics constructor
     */
    public ColumnStatistics() {
        super();// undefined constructor
    }

    // GP, PG
    public ColumnStatistics(float size, Object min, Object max) {
        super(size);
        this.min = min;
        this.max = max;
    }

    // Oracle
    public ColumnStatistics(float size, Object min, Object max, float density) {
        super(size);
        this.min = min;
        this.max = max;
        this.density = density;
    }

    public ColumnStatistics(float size) {
        super(size);
    }

    public Object getMin() {
        return min;
    }

    public float getMinAsFloat() {
        return (Float)min;
    }

    public Object getMax() {
        return max;
    }

    public float getMaxAsFloat() {
        return (Float)max;
    }

    public float getDensity() {
        return density;
    }

}
