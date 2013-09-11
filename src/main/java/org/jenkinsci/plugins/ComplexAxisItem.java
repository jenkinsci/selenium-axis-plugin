package org.jenkinsci.plugins;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Describable;
import hudson.model.Descriptor;

public abstract class ComplexAxisItem extends AbstractDescribableImpl<ComplexAxisItem> implements Comparable, Describable<ComplexAxisItem> {

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }



}
