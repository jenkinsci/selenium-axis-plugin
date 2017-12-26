package org.jenkinsci.plugins

import hudson.matrix.AxisList
import hudson.matrix.MatrixProject
import hudson.util.Secret
import jenkins.model.Jenkins
import org.jenkinsci.plugins.saucelabs.CapabilityReader
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript
import org.jenkinsci.plugins.scriptsecurity.scripts.ApprovalContext
import org.jenkinsci.plugins.scriptsecurity.scripts.ClasspathEntry
import org.jenkinsci.plugins.selenium.Axis
import org.jenkinsci.plugins.selenium.Manual
import org.jsoup.Jsoup
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification

class URLSpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    def 'saucelabs_url'() {
        given:
        def sauceLabsDescriptor = Jenkins.instance.getDescriptor(org.jenkinsci.plugins.saucelabs.DynamicCapability)
        sauceLabsDescriptor.sauceLabsName = 'test'
        sauceLabsDescriptor.sauceLabsPwd = new Secret('pass')

        when:
        def url = sauceLabsDescriptor.getURL('SL')

        then:
        url.equals('http://test:pass@ondemand.saucelabs.com:80')

        when:
        url = sauceLabsDescriptor.getURL('SEL')

        then:
        url.equals('')

    }

    def 'hub_url'() {
        given:
        def hubDescriptor = Jenkins.instance.getDescriptor(org.jenkinsci.plugins.hub.DynamicCapability)

        when:
        def url = hubDescriptor.getURL('SL')

        then:
        url.equals('')

        when:
        url = hubDescriptor.getURL('SEL')

        then:
        url.equals('http://localhost:4444')
    }
}
