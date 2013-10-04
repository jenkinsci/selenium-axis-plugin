package org.jenkinsci.plugins;

import hudson.matrix.Axis;
import hudson.matrix.MatrixBuild;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class ComplexAxis extends Axis{

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
        this.complexAxisItems = (complexAxisItem!=null)?complexAxisItem:ComplexAxisItem.emptyList();
    }

    public static String convertToAxisValue(List<? extends ComplexAxisItem> complexAxisItems){
        StringBuilder ret = new StringBuilder();
        boolean valueDefined = false;

        if(complexAxisItems == null)
            complexAxisItems = ComplexAxisItem.emptyList();

        for (ComplexAxisItem item : complexAxisItems) {
            String i = item.toString();
            if( i.length() > 0){
                valueDefined = true;
                ret.append(item.toString()).append(' ');
            }
        }
        //there has to be something here for the Axis.value
        if (!valueDefined)
            ret.append("default");

        return ret.toString();
    }

    @Override
    public void addBuildVariable(String value, Map<String,String> map){}

    @Override
    public List<String> rebuild( MatrixBuild.MatrixBuildExecution context )
    {
        List<String> ret = new ArrayList<String>();

        for( int i = 0; i < complexAxisItems.size(); i++){
            complexAxisItems.get(i).rebuild(ret);
        }

        return ret;

    }

    @Override
    public List<String> getValues( )
    {
        List<String> ret = new ArrayList<String>();

        for( int i = 0; i < complexAxisItems.size(); i++){
            complexAxisItems.get(i).getValues(ret);
        }

        return ret;

    }
}
