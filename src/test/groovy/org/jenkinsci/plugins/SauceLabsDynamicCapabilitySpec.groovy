package org.jenkinsci.plugins

import hudson.util.Secret
import jenkins.model.Jenkins
import org.jenkinsci.plugins.saucelabs.CapabilityReader
import org.jenkinsci.plugins.saucelabs.DynamicCapability
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript
import org.jenkinsci.plugins.scriptsecurity.scripts.ApprovalContext
import org.jenkinsci.plugins.scriptsecurity.scripts.ClasspathEntry
import org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval
import org.jenkinsci.plugins.scriptsecurity.scripts.languages.GroovyLanguage
import org.jenkinsci.plugins.selenium.ICapability
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification

class SauceLabsDynamicCapabilitySpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    @Shared
            sauceLabsDescriptor

    void configure(sauceLabsFile) {

        CapabilityReader.metaClass.rawRead = {
            String s -> this.class.getResource(s).text
        }

        if (sauceLabsDescriptor == null) {
            sauceLabsDescriptor = Jenkins.instance.getDescriptor(org.jenkinsci.plugins.saucelabs.DynamicCapability)
        }

        sauceLabsDescriptor.sauceLabs = true
        sauceLabsDescriptor.sauceLabsName = 'test'
        sauceLabsDescriptor.sauceLabsPwd = new Secret('pass')
        sauceLabsDescriptor.sauceLabsAPIURL = sauceLabsFile
    }

    def 'Build'() {
        given:
        configure('/saucelabs_3.json')
        ScriptApproval.get().preapprove('', GroovyLanguage.get());
        SecureGroovyScript script = new SecureGroovyScript('true', true, Collections.<ClasspathEntry>emptyList() ).configuring(ApprovalContext.create())

        def sdc = new DynamicCapability(
                sauceLabsDescriptor.getRandomCapabilities('all', 3, script))
        //expect:
        //sdc.getCapabilities().size() == 3
    }
}

