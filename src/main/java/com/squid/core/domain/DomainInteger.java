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
package com.squid.core.domain;

/**
 * Created by lrabiet on 23/05/16.
 */
public class DomainInteger extends DomainBase {

	/*
    private static Image ICON = null;

    static {
        URL url = DomainModelPlugin.getDefault().getBundle().getEntry("/icons/numeric/numeric.png");
        ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
        ICON = descriptor.createImage();
    }
    */

    /**
     *
     */
    public DomainInteger() {
        this(INTRINSIC);
    }

    /**
     * @param parent
     */
    protected DomainInteger(IDomain parent) {
        super(parent);
        setName("Integer");
    }

    /*
    public Image getIcon() {
        return ICON;
    }
    */

    public IDomain getSingleton() {
        return IDomain.INT;
    }
}