package org.jenkinsci.plugins.hub

import hudson.Extension
import hudson.util.ListBoxModel
import org.kohsuke.stapler.DataBoundConstructor
import org.jenkinsci.complex.axes.Item
import org.jenkinsci.complex.axes.ItemDescriptor
import org.kohsuke.stapler.QueryParameter

class Capability extends  Item implements Comparable {

    String browserName
    String platformName
    String browserVersion
    String capType = 'SEL'
    @SuppressWarnings('UnnecessaryTransientModifier')
    transient Integer maxInstances

    Capability() {
        browserName = 'Any'
        platformName = 'Any'
        browserVersion = 'Any'
        maxInstances = 1
    }

    @DataBoundConstructor
    Capability(String browserName, String platformName, String browserVersion, String capType) {
        this.capType = capType ?: 'SEL'
        this.browserName = browserName ?: 'Any'
        this.platformName = platformName ?: 'Any'
        this.browserVersion = browserVersion ?: 'Any'
        this.maxInstances = 1
    }

    Capability(String browserName, String platformName, String browserVersion) {
        Capability(browserName, platformName, browserVersion, 'SEL')
    }

    String getCapType() {
        capType ?: 'SEL'
    }

    Integer incr() {
        maxInstances++
    }

    @Override
    String toString() {
        String.format('%s-%s-%s-%s', getCapType(), platformName, browserName, browserVersion)
    }

    @Extension static class DescriptorImpl extends ItemDescriptor {
        final String displayName = 'Defined Capability'

        ListBoxModel doFillCapTypeItems(@QueryParameter String capType) {
            ListBoxModel cbm = new ListBoxModel()

            cbm << new ListBoxModel.Option('Local Selenium', 'SEL', 'SEL' == capType)
            cbm << new ListBoxModel.Option('SauceLabs', 'SL', 'SL' == capType)

            cbm
        }
    }
}
