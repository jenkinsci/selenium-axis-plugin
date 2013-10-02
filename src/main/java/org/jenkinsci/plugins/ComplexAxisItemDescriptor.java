package org.jenkinsci.plugins;

import hudson.model.Descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class ComplexAxisItemDescriptor extends Descriptor<ComplexAxisItem> {

    @Override public String getDisplayName() {
        return "ComplexAxisItem";
    }


    public   List<? extends ComplexAxisItem> loadDefaultItems(){
        return Collections.emptyList();
    }

    public   List<? extends ComplexAxisItem> loadDefaultItems(ArrayList<? extends ComplexAxisItem> cai){
        return cai;
    }
}
