package org.jenkinsci.plugins

import hudson.Extension
import org.kohsuke.stapler.DataBoundConstructor
import hudson.model.Descriptor

class SeleniumCapabilityRO extends SeleniumCapability{

    SeleniumCapabilityRO() {
        super()
    }

    SeleniumCapabilityRO(String titleAttr) {
        super(titleAttr)
    }

    @DataBoundConstructor
    SeleniumCapabilityRO(String browserName, String platformName, String browserVersion) {
        super(browserName, platformName, browserName)
    }

    @Extension public static class DescriptorImpl extends ComplexAxisItemDescriptor {

        @Override public String getDisplayName() {
            return "Selenium Capability RO";
        }
    }
}
