package org.jenkinsci.plugins;

import hudson.DescriptorExtensionList;
import hudson.matrix.AxisDescriptor;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import java.util.List;

public abstract class ComplexAxisDescriptor extends AxisDescriptor {

    public DescriptorExtensionList<ComplexAxisItem,Descriptor<ComplexAxisItem> > complexAxisItemTypes() {
        DescriptorExtensionList<ComplexAxisItem,Descriptor<ComplexAxisItem> >  xxx =  Jenkins.getInstance().<ComplexAxisItem,Descriptor<ComplexAxisItem>>getDescriptorList(ComplexAxisItem.class);

        return xxx;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        // To persist global configuration information,
        // set that to properties and call save().
        req.bindJSON(this, formData);
        save();
        return true;
    }

    public abstract List<? extends ComplexAxisItem> loadDefaultItems();

    @Override
    public String getDisplayName() {
        return "Complex Axis";
    }

    @Override
    public boolean isInstantiable() {
        return true;
    }

}