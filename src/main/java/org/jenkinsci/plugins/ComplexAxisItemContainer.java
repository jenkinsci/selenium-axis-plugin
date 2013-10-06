package org.jenkinsci.plugins;

import hudson.model.Descriptor;
import hudson.DescriptorExtensionList;
import java.util.List;
import java.util.Collections;
import jenkins.model.Jenkins;

public abstract class ComplexAxisItemContainer extends  ComplexAxisItem {

    private List<? extends ComplexAxisItem> complexAxisItems;

    ComplexAxisItemContainer(List<? extends ComplexAxisItem> complexAxisItems) {

        if(complexAxisItems == null)
            this.complexAxisItems = emptyList();
        else
            this.complexAxisItems = complexAxisItems;
    }

    public List<? extends ComplexAxisItem> getComplexAxisItems(){
        if(complexAxisItems == null)
            return emptyList();
        else
            return complexAxisItems;
    }

    public void setComplexAxisItems(List<? extends ComplexAxisItem> cai){
        complexAxisItems = cai;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (ComplexAxisItem item : getComplexAxisItems()) {
            ret.append(item.toString()).append(' ');
        }
        return ret.toString();
    }

}
