package org.jenkinsci.plugins

import jenkins.model.Jenkins
import org.jenkinsci.plugins.hub.Selenium
import org.jenkinsci.plugins.hub.DynamicCapability
import org.jenkinsci.plugins.selenium.Axis
import org.jvnet.hudson.test.JenkinsRule
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

class SeleniumDynamicCapabilitySpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    @Shared seleniumAxisDescriptor
    @Shared seleniumDynamicCapabilityDescriptor

    void configure(seleniumFile) {

        if (seleniumAxisDescriptor == null) {
            seleniumAxisDescriptor = Jenkins.instance.getDescriptor(Axis)
        }
        if (seleniumDynamicCapabilityDescriptor == null) {
            seleniumDynamicCapabilityDescriptor = Jenkins.instance.getDescriptor(DynamicCapability)
        }

        seleniumAxisDescriptor.setServer(seleniumFile)
    }

    def 'Build'() {
        given:
        configure('/grid-2.33.0.html')
        def sdc = new DynamicCapability(seleniumDynamicCapabilityDescriptor.loadDefaultItems())

        expect:
        sdc.seleniumCapabilities.size() == 4
    }
}

