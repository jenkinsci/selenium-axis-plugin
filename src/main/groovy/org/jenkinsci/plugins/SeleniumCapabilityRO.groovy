package org.jenkinsci.plugins

import hudson.Extension
import org.kohsuke.stapler.DataBoundConstructor

class SeleniumCapabilityRO extends SeleniumCapability{

    @DataBoundConstructor
    SeleniumCapabilityRO(String browserName, String platformName, String browserVersion) {
        super(browserName, platformName, browserVersion)
    }

    SeleniumCapabilityRO(String titleAttr){
        super(titleAttr)
    }


    @Extension
    public static class DescriptorImpl extends ComplexAxisItemDescriptor {

        @Override public String getDisplayName() {
            return "Selenium Capability RO";
        }
    }

}
