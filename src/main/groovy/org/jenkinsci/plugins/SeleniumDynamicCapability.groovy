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

    @DataBoundConstructor
    SeleniumDynamicCapability(List<SeleniumCapability> seleniumCapabilities) {
        super( seleniumCapabilities)
    }

    @Extension public static class DescriptorImpl extends ComplexAxisItemContainerDescriptor {

        private String server

        @Override public String getDisplayName() {
            return "Selenium Dynamic Capability";
        }

    }
}
