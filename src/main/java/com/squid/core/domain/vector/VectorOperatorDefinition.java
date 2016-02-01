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
package com.squid.core.domain.vector;

import java.util.List;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.ExtendedType;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorDiagnostic;

/**
 * The VECTOR operator allows to create a list of values.
 * It is handled as a intrinsic operator.
 * 
 * @author sergefantino
 *
 */
public class VectorOperatorDefinition extends OperatorDefinition {

	public static final String ID = "com.squid.domain.model.vector";
	
	public VectorOperatorDefinition() {
		super("VECTOR", ID, OperatorDefinition.PREFIX_POSITION, "VECTOR", VectorDomain.DOMAIN);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isExtendedID() {
		return false;// override
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public IDomain computeImageDomain(List<IDomain> imageDomains) {
		if (imageDomains.size()>=1) {
			return new ProxyVectorDomain(getSubDomain(imageDomains),imageDomains.size());
		} else {
			return IDomain.UNKNOWN;
		}
	}
	
	private IDomain getSubDomain(List<IDomain> imageDomains) {
	    for(IDomain domain : imageDomains) {
	        if(domain == IDomain.NULL) {
	            return IDomain.UNKNOWN;
	        }
	    }
	    return imageDomains.get(0);
	}
    
	/**
	 * SFA: copied from AlgebraicOperatorDefinition
	 */
    @Override
    public ExtendedType computeExtendedType(ExtendedType[] types) {
    	ExtendedType result = ExtendedType.UNDEFINED;
    	for (int i=0;i<types.length;i++) {
    		ExtendedType challenger = types[i];
    		if (ExtendedType.UNDEFINED.equals(challenger)) {
    			// ignore
    		} else if (ExtendedType.UNDEFINED.equals(result)) {
    			result = new ExtendedType(challenger);
    		} else {
				if (result.getDomain()==challenger.getDomain() && result.getDataType()==challenger.getDataType()) {
					result = result.scaleAndsize(
							Math.max(result.getScale(), challenger.getScale()),
							Math.max(result.getSize(), challenger.getSize()));
				} else
				if (result.getDomain()==challenger.getDomain()) {
					if (result.getDomain().isInstanceOf(IDomain.NUMERIC)) {
						
					} else
					if (result.getDomain().isInstanceOf(IDomain.TEMPORAL)) {
						
					} else
					if (result.getDomain().isInstanceOf(IDomain.STRING)) {
						
					}
				}
    		}
    	}
    	return result;
    }
	
	@Override
	public OperatorDiagnostic validateParameters(List<IDomain> imageDomains) {
		if (imageDomains.size()>=1) {
			IDomain domain = imageDomains.get(0);
			for (IDomain dd : imageDomains) {
			    // Define common domain
			    if(dd != IDomain.NULL && domain == IDomain.NULL) {
			        domain = dd;
			    }
			    
			    if (dd == IDomain.NULL) {
                    // ok
                } else if (dd.equals(domain)) {
					// ok
				} else if (dd.isInstanceOf(domain)) {
					// ok
				} else if (domain.isInstanceOf(dd)) {
					domain = dd;
				}  else {
					return new OperatorDiagnostic("All the parameter must conform to the same domain",getSymbol()+"(X1,X2,...)");
				}
			}
			// else
			return OperatorDiagnostic.IS_VALID;
		} else {
			return new OperatorDiagnostic("must takes parameters",getSymbol()+"(X1,X2,...)");
		}
	}
	
	@Override
	public String prettyPrint(String symbol, int position, String[] args,
			boolean showBrackets) {
		String result = "{";
        for (int i=0;i<args.length;i++) {
            if (i>0) {
                if (position==OperatorDefinition.INFIX_POSITION) {
                    result += symbol;
                } else {
                    result += ",";
                }
            }
            result += args[i];
        }
        result += "}";
        return result;
	}

}
