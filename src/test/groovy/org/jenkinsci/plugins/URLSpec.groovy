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
    @Shared sauceLabsDescriptor, hubDescriptor

    MatrixProject configure(seleniumFile, sauceLabsFile, advanced = false) {

        sauceLabsDescriptor = Jenkins.instance.getDescriptor(org.jenkinsci.plugins.saucelabs.DynamicCapability)
        hubDescriptor = Jenkins.instance.getDescriptor(org.jenkinsci.plugins.hub.DynamicCapability)

        sauceLabsDescriptor.sauceLabs = true
        sauceLabsDescriptor.sauceLabsName = 'test'
        sauceLabsDescriptor.sauceLabsPwd = new Secret('pass')
        sauceLabsDescriptor.sauceLabsAPIURL = sauceLabsFile

    }

    def 'hub_url'() {
        given:
        def matrixProject = configure('/grid-2.35.0.html', '/saucelabs_3.json', true)

        when:
        def build = matrixProject.scheduleBuild2(0).get()
        def runs = build.runs

        then:
        build.logFile.text.contains('SUCCESS')
        runs.every { it.logFile.text.contains('SUCCESS') }
        runs.size() == 7
    }

    def 'saucelabs_url'() {
        given:
        def matrixProject = configure('/grid-2.35.0.html', '/saucelabs_3.json')

        when:
        def build = matrixProject.scheduleBuild2(0).get()
        def runs = build.runs

        then:
        build.logFile.text.contains('SUCCESS')
        runs.every { it.logFile.text.contains('SUCCESS') }
        runs.size() == 7
    }
}
