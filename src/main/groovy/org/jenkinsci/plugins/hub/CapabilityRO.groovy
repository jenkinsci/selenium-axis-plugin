package org.jenkinsci.plugins.hub

import hudson.Extension
import org.kohsuke.stapler.DataBoundConstructor
import org.jenkinsci.complex.axes.ItemDescriptor

class CapabilityRO extends Capability {

    @DataBoundConstructor
    CapabilityRO(String browserName, String platformName, String browserVersion, String capType) {
        super(browserName, platformName, browserVersion, capType)
    }

    @Extension
    static class DescriptorImpl extends ItemDescriptor {
        final String displayName = 'Selenium Capability RO'
    }
}
