package org.jenkinsci.complex.axes;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Describable;
import java.util.List;

public abstract class Item extends AbstractDescribableImpl<Item> implements Comparable, Describable<Item> {


    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }

    public List<String> rebuild(List<String> list){
        list.add(toString());
        return list;
    }

    public List<String> getValues(List<String> list){
        list.add(toString());
        return list;
    }
}
