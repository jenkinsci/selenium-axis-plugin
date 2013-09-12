package org.jenkinsci.plugins;

import hudson.matrix.Axis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class ComplexAxis extends Axis {

    private final List<? extends ComplexAxisItem> complexAxisItems;

    public List<? extends ComplexAxisItem> getComplexAxisItems(){
        return Collections.unmodifiableList(complexAxisItems);
    }

    public ComplexAxis(String name, String value){
        super(name, value);
        complexAxisItems = new ArrayList<ComplexAxisItem>();
    }

    public ComplexAxis(String name, List<? extends ComplexAxisItem> complexAxisItem){
        super(name, ComplexAxis.convertToAxisValue(complexAxisItem));
        this.complexAxisItems = complexAxisItem;
    }

    public static String convertToAxisValue(List<? extends ComplexAxisItem> complexAxisItems){
        StringBuilder ret = new StringBuilder();

        for (ComplexAxisItem item : complexAxisItems) {
            ret.append(item.toString()).append(' ');
        }

        return ret.toString();
    }

    @Override
    public void addBuildVariable(String value, Map<String,String> map){}

    @Override
    public boolean isSystem() {
        return true;
    }
}
