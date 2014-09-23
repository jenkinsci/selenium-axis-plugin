package org.jenkinsci.plugins

import hudson.util.Secret
import jenkins.model.Jenkins
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification

class SauceLabsDynamicCapabilitySpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    @Shared seleniumAxisDescriptor

    void configure(sauceLabsFile) {

        SauceLabsCapabilityReader.metaClass.rawRead = {
            String s -> this.class.getResource(s).text
        }

        if (seleniumAxisDescriptor == null) {
            seleniumAxisDescriptor = Jenkins.instance.getDescriptor(SeleniumAxis)
        }

        seleniumAxisDescriptor.sauceLabs = true
        seleniumAxisDescriptor.sauceLabsName = 'test'
        seleniumAxisDescriptor.sauceLabsPwd = new Secret('pass')
        seleniumAxisDescriptor.sauceLabsAPIURL = sauceLabsFile
    }

    def 'Build'() {
        given:
        configure('/saucelabs_3.json')
        def sdc = new SauceLabsDynamicCapability(
                seleniumAxisDescriptor.getRandomSauceLabsCapabilities('all', 3, ''))

        expect:
        sdc.seleniumCapabilities.size() == 3
    }
}

