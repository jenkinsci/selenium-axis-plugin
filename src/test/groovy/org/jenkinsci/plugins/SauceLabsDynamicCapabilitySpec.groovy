package org.jenkinsci.plugins

import hudson.util.Secret
import jenkins.model.Jenkins
import org.jenkinsci.plugins.hub.Selenium
import org.jenkinsci.plugins.saucelabs.CapabilityReader
import org.jenkinsci.plugins.saucelabs.DynamicCapability
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript
import org.jenkinsci.plugins.scriptsecurity.scripts.ApprovalContext
import org.jenkinsci.plugins.scriptsecurity.scripts.ClasspathEntry
import org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval
import org.jenkinsci.plugins.scriptsecurity.scripts.languages.GroovyLanguage
import org.jenkinsci.plugins.selenium.Axis
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification

class SauceLabsDynamicCapabilitySpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    @Shared seleniumAxisDescriptor

    void configure(sauceLabsFile) {

        CapabilityReader.metaClass.rawRead = {
            String s -> this.class.getResource(s).text
        }

        if (seleniumAxisDescriptor == null) {
            seleniumAxisDescriptor = Jenkins.instance.getDescriptor(Axis)
        }

        seleniumAxisDescriptor.sauceLabs = true
        seleniumAxisDescriptor.sauceLabsName = 'test'
        seleniumAxisDescriptor.sauceLabsPwd = new Secret('pass')
        seleniumAxisDescriptor.sauceLabsAPIURL = sauceLabsFile
    }

    def 'Build'() {
        given:
        configure('/saucelabs_3.json')
        ScriptApproval.get().preapprove('', GroovyLanguage.get());
        SecureGroovyScript script = new SecureGroovyScript('true', true, Collections.<ClasspathEntry>emptyList() ).configuring(ApprovalContext.create())

        def sdc = new DynamicCapability(
                seleniumAxisDescriptor.getRandomSauceLabsCapabilities('all', 3, script))
        expect:
        sdc.seleniumCapabilities.size() == 3
    }
}

