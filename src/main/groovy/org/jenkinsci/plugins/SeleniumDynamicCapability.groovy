package org.jenkinsci.plugins

import hudson.Extension
import hudson.util.FormValidation
import org.kohsuke.stapler.DataBoundConstructor
import hudson.model.Descriptor
import hudson.DescriptorExtensionList
import jenkins.model.Jenkins
import org.kohsuke.stapler.QueryParameter

class SeleniumDynamicCapability extends  ComplexAxisItemContainer {

    SeleniumDynamicCapability() {
        super(new ArrayList<SeleniumCapabilityRO>())
    }

    List<SeleniumCapabilityRO> getSeleniumCapabilities(){
        return getComplexAxisItems()
    }

    void setSeleniumCapabilities(List<SeleniumCapabilityRO> sc){
        setComplexAxisItems(sc)
    }

    @DataBoundConstructor
    SeleniumDynamicCapability(List<SeleniumCapabilityRO> seleniumCapabilities) {
        super( seleniumCapabilities)
    }

    String toString(){
        "DetectedSelenium"
    }

    @Override
    public List<String> rebuild(List<String> list){
        SeleniumDynamicCapability.DescriptorImpl sdcd = Jenkins.getInstance().getDescriptor(SeleniumDynamicCapability.class)

        List<SeleniumCapabilityRO> sc = sdcd.loadDefaultItems()

        if (sc.size() == 0)
            throw(new SeleniumException("No selenium capabilities detected"))

        setSeleniumCapabilities(sc)

        sc.each{list.add(it.toString())}
        return list;
    }

    @Override
    public List<String> getValues(List<String> list){
        getSeleniumCapabilities().each{list.add(it.toString())}

        if (list.size() == 0)
            list.add('Rebuilt at build time')

        return list;
    }

    @Extension public static class DescriptorImpl extends ComplexAxisItemContainerDescriptor {

        //so we need this to get at the name of the selenium server in the global config
        protected static Descriptor<? extends ComplexAxisDescriptor> getTopLevelDescriptor(){
            SeleniumAxis.DescriptorImpl sad = Jenkins.getInstance().getDescriptor(SeleniumAxis.class)
            sad.load()

            return sad
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
