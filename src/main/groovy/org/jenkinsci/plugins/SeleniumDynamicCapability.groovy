package org.jenkinsci.plugins

import hudson.Extension
import org.kohsuke.stapler.DataBoundConstructor
import hudson.model.Descriptor
import hudson.DescriptorExtensionList
import jenkins.model.Jenkins

class SeleniumDynamicCapability extends  ComplexAxisItem implements Comparable {


    private List<SeleniumCapability> seleniumCapabilities

    SeleniumDynamicCapability() {
        seleniumCapabilities = new ArrayList<SeleniumCapability>();
    }

    @DataBoundConstructor
    SeleniumDynamicCapability(List<SeleniumCapability> seleniumCapabilities) {
        this.seleniumCapabilities = seleniumCapabilities
    }

    public List<SeleniumCapability> getComplexAxisItems(){
        return Collections.unmodifiableList(seleniumCapabilities);
    }

    @Override
    String toString() {
        StringBuilder ret = new StringBuilder()
        seleniumCapabilities.each(){ret.append(it.toString()).append(' ')}
        return ret.toString()
    }


    public List<SeleniumCapability> getSeleniumCapabilities(){
        this.seleniumCapabilities
    }

    @Extension public static class DescriptorImpl extends ComplexAxisItemDescriptor {
        @Override public String getDisplayName() {
            return "Selenium Dynamic Capability";
        }

        public DescriptorExtensionList<ComplexAxisItem,Descriptor<ComplexAxisItem> > complexAxisItemTypes() {
            DescriptorExtensionList<ComplexAxisItem,Descriptor<ComplexAxisItem> >  xxx =  Jenkins.getInstance().<ComplexAxisItem,Descriptor<ComplexAxisItem>>getDescriptorList(ComplexAxisItem.class);

            return xxx;
        }
    }
}
