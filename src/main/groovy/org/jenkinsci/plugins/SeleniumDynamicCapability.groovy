package org.jenkinsci.plugins

import hudson.Extension
import org.kohsuke.stapler.DataBoundConstructor
import hudson.model.Descriptor
import hudson.DescriptorExtensionList
import jenkins.model.Jenkins

class SeleniumDynamicCapability extends  ComplexAxisItemContainer {

    SeleniumDynamicCapability() {
        super(new ArrayList<SeleniumCapability>())
    }

    List<SeleniumCapability> getSeleniumCapabilities(){
        return getComplexAxisItems()
    }

    @DataBoundConstructor
    SeleniumDynamicCapability(List<SeleniumCapability> seleniumCapabilities) {
        super( seleniumCapabilities)
    }

    public List<SeleniumCapability>getSeleniumCapability(){
        return getComplexAxisItems()
    }

    @Extension public static class DescriptorImpl extends ComplexAxisItemContainerDescriptor {

        //so we need this to get at the name of the selenium server in the global config
        protected static Descriptor<? extends ComplexAxisDescriptor> getTopLevelDescriptor(){
            SeleniumAxis.DescriptorImpl xxx = Jenkins.getInstance().getDescriptor(SeleniumAxis.class)
            xxx.load()

            return xxx
        }

        public static List<SeleniumCapability> loadDefaultItems(){
            SeleniumAxis.DescriptorImpl tld = getTopLevelDescriptor()

            def sel = new Selenium(tld.getServer(), SeleniumCapability.class)
            return sel.getSeleniumCapabilities()
        }

        @Override public String getDisplayName() {
            return "Selenium Dynamic Capability";
        }

        //public DescriptorExtensionList<? extends ComplexAxisItem,Descriptor<? extends ComplexAxisItem> > complexAxisItemTypes(){

        //}
    }
}
