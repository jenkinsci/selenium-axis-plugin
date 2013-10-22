package org.jenkinsci.plugins

import hudson.matrix.AxisList
import hudson.matrix.MatrixProject
import jenkins.model.Jenkins
import org.jvnet.hudson.test.JenkinsRule
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

class SeleniumDynamicCapabilitySpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    @Shared seleniumAxisDescriptor
    @Shared seleniumDynamicCapabilityDescriptor

    void configure(seleniumFile){

        if(seleniumAxisDescriptor == null){
            seleniumAxisDescriptor= Jenkins.getInstance().getDescriptor(SeleniumAxis.class)
        }
        if(seleniumDynamicCapabilityDescriptor == null){
            seleniumDynamicCapabilityDescriptor= Jenkins.getInstance().getDescriptor(SeleniumDynamicCapability.class)
        }

        seleniumAxisDescriptor.setServer(seleniumFile)
    }

    def 'Build'() {
        given:
        configure("/grid-2.33.0.html")
        def sdc = new SeleniumDynamicCapability(seleniumDynamicCapabilityDescriptor.loadDefaultItems())

        expect:
        sdc.getSeleniumCapabilities().size() == 4
    }
}


