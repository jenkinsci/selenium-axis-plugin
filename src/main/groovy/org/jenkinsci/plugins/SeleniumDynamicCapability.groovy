package org.jenkinsci.plugins

import hudson.Extension
import org.kohsuke.stapler.DataBoundConstructor
import hudson.model.Descriptor
import hudson.DescriptorExtensionList
import jenkins.model.Jenkins

class SeleniumDynamicCapability extends  ComplexAxisItemContainer {

    SeleniumDynamicCapability() {
        super(new ArrayList<SeleniumCapabilityRO>())
    }

    List<SeleniumCapabilityRO> getSeleniumCapabilities(){

        //if(complexAxisItems.isEmpty()){
        //    def sel = new Selenium(DescriptorImpl.getTopLevelDescriptor().getServer(), SeleniumCapability.class)
        //    return sel.seleniumCapabilities
        //}else{
            return getComplexAxisItems()
        //}
    }

    @DataBoundConstructor
    SeleniumDynamicCapability(List<SeleniumCapabilityRO> seleniumCapabilities) {
        super( seleniumCapabilities)
    }


    @Extension public static class DescriptorImpl extends ComplexAxisItemContainerDescriptor {

        //so we need this to get at the name of the selenium server in the global config
        protected static Descriptor<? extends ComplexAxisDescriptor> getTopLevelDescriptor(){
            SeleniumAxis.DescriptorImpl xxx = Jenkins.getInstance().getDescriptor(SeleniumAxis.class)
            xxx.load()

            return xxx
        }

        @Override
        public   List<? extends ComplexAxisItem> loadDefaultItems(ArrayList<? extends ComplexAxisItem> cai){
            def sdc = new SeleniumDynamicCapability(loadDefaultItems())

            cai.add(sdc)

            cai
        }



        @Override
        public  List<SeleniumCapabilityRO> loadDefaultItems(){
            getTopLevelDescriptor().getSeleniumCapabilities()
        }

        @Override public String getDisplayName() {
            return "Detected Capability";
        }

    }
}
