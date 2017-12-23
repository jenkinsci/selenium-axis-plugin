package org.jenkinsci.plugins.hub

import hudson.Extension
import hudson.init.InitMilestone
import hudson.init.Initializer
import hudson.model.Items
import org.kohsuke.stapler.DataBoundConstructor
import org.jenkinsci.complex.axes.ItemDescriptor

class CapabilityRO extends Capability {

    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    public static void addAliases() {
        Items.XSTREAM2.addCompatibilityAlias("org.jenkinsci.plugins.SeleniumCapabilityRO", CapabilityRO);
    }

    @DataBoundConstructor
    CapabilityRO(String browserName, String platformName, String browserVersion, String capType) {
        super(browserName, platformName, browserVersion, capType)
    }

    @Extension
    static class DescriptorImpl extends ItemDescriptor {
        final String displayName = 'Selenium Capability RO'
    }
}
