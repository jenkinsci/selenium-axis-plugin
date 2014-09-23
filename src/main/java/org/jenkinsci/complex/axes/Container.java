package org.jenkinsci.complex.axes;

import java.util.List;

public abstract class Container extends Item {

    private List<? extends Item> complexAxisItems;

    public Container() {
        super();
    }

    public Container(List<? extends Item> complexAxisItems) {

        super();

        if(complexAxisItems == null)
            this.complexAxisItems = ItemList.emptyList();
        else
            this.complexAxisItems = complexAxisItems;
    }

    public List<? extends Item> getComplexAxisItems(){
        if(complexAxisItems == null)
            return ItemList.emptyList();
        else
            return complexAxisItems;
    }

    public void setComplexAxisItems(List<? extends Item> cai){
        complexAxisItems = cai;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Object item : getComplexAxisItems()) {
            ret.append(item.toString()).append(' ');
        }
        return ret.toString();
    }

}
