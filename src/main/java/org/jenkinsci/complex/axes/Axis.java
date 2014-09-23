package org.jenkinsci.complex.axes;

import hudson.matrix.MatrixBuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class Axis extends hudson.matrix.Axis {

    private List<? extends Item> complexAxisItems;

    public Axis(String name, String... value){
        super(name, value);
        complexAxisItems = new ArrayList<Item>();
    }

    @SuppressWarnings("unchecked")
    public Axis(String name, List<? extends Item> complexAxisItems){
        super(name, Axis.convertToAxisValue(complexAxisItems));
        this.complexAxisItems = (complexAxisItems !=null)? complexAxisItems : ItemList.emptyList();
    }

    @SuppressWarnings("unchecked")
    public static List<String> convertToAxisValue(List<? extends Item> axisItems){
        List<String> ret = new ArrayList<String>();

        if(axisItems == null)
            axisItems = ItemList.emptyList();

        for (Item item : axisItems) {
            String i = item.toString();
            if( i.length() > 0){
                ret.add(item.toString());
            }
        }
        //there has to be something here for the Axis.value
        if (ret.size() == 0)
            ret.add("default");

        return ret;
    }

    public List<? extends Item> getComplexAxisItems(){
        return Collections.unmodifiableList(complexAxisItems);
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
