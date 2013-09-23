package org.jenkinsci.plugins;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Created with IntelliJ IDEA.
 * User: jeremym
 * Date: 9/13/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ComplexAxisItemContainerDescriptor extends ComplexAxisItemDescriptor{

    //public abstract DescriptorExtensionList<? extends ComplexAxisItem,Descriptor<? extends ComplexAxisItem> > complexAxisItemTypes();
    //public DescriptorExtensionList<ComplexAxisItem,Descriptor<ComplexAxisItem> > complexAxisItemTypes() {
    //    DescriptorExtensionList<ComplexAxisItem,Descriptor<ComplexAxisItem> >  xxx =  Jenkins.getInstance().<ComplexAxisItem,Descriptor<ComplexAxisItem>>getDescriptorList(ComplexAxisItem.class);
    //
    //    return xxx;
    //}

    public ComplexAxisItemContainerDescriptor () {
        load();
    }

}
