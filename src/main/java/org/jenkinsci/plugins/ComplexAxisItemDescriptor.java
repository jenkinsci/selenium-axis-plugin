package org.jenkinsci.plugins;

import hudson.model.Descriptor;


public abstract class ComplexAxisItemDescriptor extends Descriptor<ComplexAxisItem> {

    @Override public String getDisplayName() {
        return "ComplexAxisItem";
    }


}
