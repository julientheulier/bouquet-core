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
package com.squid.core.sql.render;


/**
 * Delegate the actual definition of the decorator until rendering (because that requires the SQLSkin)
 * @author serge fantino
 *
 */
public class DelegateSamplingDecorator 
implements ISamplingDecorator
{
	
	private double size = 1;
	private double percent = 1;
	private int mode = FRACTION;
	
	public DelegateSamplingDecorator(double size) {
		super();
		setSize(size);
		setPercent(size);
	}
	
	public DelegateSamplingDecorator(double size, int mode) {
		super();
		setSize(size,mode);
		setMode(mode);
	}
	
	public DelegateSamplingDecorator(double size, double percent, int mode) {
		super();
		setSize(size,mode);
		setMode(mode);
		setPercent(percent, mode);
	}

	public void setSize(double size) {
		this.size = size;
		this.mode = this.size<1?FRACTION:COUNT;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public String render(SQLSkin skin) throws RenderingException {
		ISamplingDecorator delegator = skin.createSamplingDecorator(this);
		return delegator.render(skin);
	}

	/* (non-Javadoc)
	 * @see com.squid.core.sql2.render.ISamplingDecorator#setSize(double, int)
	 */
	public void setSize(double size, int mode) {
		this.size = size;
		this.mode = mode;
		/*if (mode==FRACTION&&this.size>1) {
			this.size = this.size/100;
		}
		if (mode==FRACTION&&this.size>1) {
			this.size = 1;
		}*/
	}

	public int getMode() {
		return mode;
	}

	public double getSize() {
		return size;
	}

	public void setPercent(double percent) {
		this.percent = percent;
		this.mode = this.percent<1?FRACTION:COUNT;
	}

	/* (non-Javadoc)
	 * @see com.squid.core.sql2.render.ISamplingDecorator#setSize(double, int)
	 */
	public void setPercent(double percent, int mode) {
		this.percent = percent;
		this.mode = mode;
	}
	
	public double getPercent() {
		return percent;
	}

}
