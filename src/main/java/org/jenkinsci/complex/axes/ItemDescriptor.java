package org.jenkinsci.complex.axes;

import hudson.model.Descriptor;
import java.util.List;

public abstract class ItemDescriptor extends Descriptor<Item> {

    @Override public String getDisplayName() {
        return "Item";
    }

    @SuppressWarnings("unchecked")
    public   List<? extends Item> loadDefaultItems(){
        return ItemList.emptyList();
    }

    public   List<? extends Item> loadDefaultItems(List<? extends Item> cai){
        return cai;
    }
}
