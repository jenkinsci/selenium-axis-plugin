package org.jenkinsci.plugins

import hudson.Extension
import org.kohsuke.stapler.DataBoundConstructor
import org.jenkinsci.complex.axes.ItemDescriptor

class SeleniumCapabilityRO extends SeleniumCapability {

    @DataBoundConstructor
    SeleniumCapabilityRO(String browserName, String platformName, String browserVersion, String capType) {
        super(browserName, platformName, browserVersion, capType)
    }

    @Extension
    static class DescriptorImpl extends ItemDescriptor {
        final String displayName = 'Selenium Capability RO'
    }
}
