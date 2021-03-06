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
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lrabiet on 12/05/16.
 */
public class ListContentAssistEntry {

    private List<ContentAssistEntry> contentAssistEntries;

    public ListContentAssistEntry(List<String> description, List<List<IDomain>> poly){
        if(poly==null /*|| description == null*/){
            return;
        }
        if(description.size()!=poly.size()){
            return;
        }
        this.setContentAssistEntries(new ArrayList<ContentAssistEntry>());
        for(List<IDomain> type : poly){
            String proposal="";
            String label="";
            ContentAssistEntry entry = new ContentAssistEntry();
            if(type == null){
                label += "No argument";
                proposal += "";
            }else {
                for(int i = 0; i<type.size(); i++ ) {
                    IDomain domain = type.get(i);
                    label += domain.getContentAssistLabel()+",";
                    proposal+= domain.getContentAssistProposal(i+1)+",";
                }
                if(label.length()>1){
                    label=label.substring(0, label.length()-1);
                }
                if(proposal.length()>1){
                    proposal=proposal.substring(0, proposal.length()-1);
                }
            }
            entry.setLabel(label);
            entry.setProposal(proposal);
            // Description need to be function's specific.
            entry.setDescription(description.get(poly.indexOf(type)));
            getContentAssistEntries().add(entry);
        }
    }

    public List<ContentAssistEntry> getContentAssistEntries() {
        return contentAssistEntries;
    }

    public void setContentAssistEntries(List<ContentAssistEntry> contentAssistEntries) {
        this.contentAssistEntries = contentAssistEntries;
    }

    @Override
    public String toString(){
        String res = "";
        if(getContentAssistEntries()!=null && getContentAssistEntries().size()!=0){
            for(ContentAssistEntry entry : getContentAssistEntries()) {
                res += entry.getLabel();
                res+="|";
            }
            res=res.substring(0, res.length()-1);
        }
        return res;
    }
}
