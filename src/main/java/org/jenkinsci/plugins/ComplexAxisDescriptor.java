package org.jenkinsci.plugins;

import hudson.DescriptorExtensionList;
import hudson.matrix.Axis;
import hudson.matrix.AxisDescriptor;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ComplexAxisDescriptor extends AxisDescriptor {


    public ComplexAxisDescriptor(){
        load();
    }

    //public abstract DescriptorExtensionList<? extends ComplexAxisItem, Descriptor<? extends ComplexAxisItem> > complexAxisItemTypes();
    //public DescriptorExtensionList<ComplexAxisItem,Descriptor<ComplexAxisItem> > complexAxisItemTypes() {
    //    DescriptorExtensionList<ComplexAxisItem,Descriptor<ComplexAxisItem> >  xxx =  Jenkins.getInstance().<ComplexAxisItem,Descriptor<ComplexAxisItem>>getDescriptorList(ComplexAxisItem.class);
    //
    //    return xxx;
    //}

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        // To persist global configuration information,
        // set that to properties and call save().
        req.bindJSON(this, formData);
        save();
        return true;
    }

    public abstract DescriptorExtensionList<ComplexAxisItem,ComplexAxisItemDescriptor> complexAxisItemTypes();

    public  List<? extends ComplexAxisItem> loadDefaultItems(){

        DescriptorExtensionList<ComplexAxisItem,ComplexAxisItemDescriptor> cait =  complexAxisItemTypes();

        ArrayList<ComplexAxisItem> cai =  new ArrayList<ComplexAxisItem>();

        for( int i = 0; i < cait.size(); i++){
            cait.get(i).loadDefaultItems(cai);
        }

        return cai;
    }
    @Override
    public String getDisplayName() {
        return "Complex Axis";
    }

    @Override
    public boolean isInstantiable() {
        return true;
    }

    public FormValidation doCheckName(@QueryParameter String value) {
        if (value.isEmpty()) {
            return FormValidation.error("You must provide a Name");
        }

        return FormValidation.ok();
    }
}