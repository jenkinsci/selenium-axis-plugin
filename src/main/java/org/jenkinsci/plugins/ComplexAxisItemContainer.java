package org.jenkinsci.plugins;

import hudson.model.Descriptor;
import hudson.DescriptorExtensionList;
import java.util.List;
import java.util.Collections;
import jenkins.model.Jenkins;

public abstract class ComplexAxisItemContainer extends  ComplexAxisItem {

    private List<? extends ComplexAxisItem> complexAxisItems;

    ComplexAxisItemContainer(List<? extends ComplexAxisItem> complexAxisItems) {
        this.complexAxisItems = complexAxisItems;
    }

    public List<? extends ComplexAxisItem> getComplexAxisItems(){
        return Collections.unmodifiableList(complexAxisItems);
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
