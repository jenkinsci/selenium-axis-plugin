package org.jenkinsci.plugins

import hudson.matrix.MatrixProject
import hudson.matrix.AxisList
import hudson.util.Secret
import jenkins.model.Jenkins
import org.jsoup.Jsoup
import spock.lang.Specification
import spock.lang.Shared
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule

class SeleniumAxisSpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    @Shared SeleniumAxis.DescriptorImpl seleniumAxisDescriptor

    MatrixProject configure(seleniumFile, sauceLabsFile) {
        SeleniumHubCapabilityReader.metaClass.rawRead = {
            String s -> Jsoup.parse(this.class.getResourceAsStream(s), 'UTF-8', '')
        }
        SauceLabsCapabilityReader.metaClass.rawRead = {
            String s -> this.class.getResource(s).text
        }

        if (seleniumAxisDescriptor == null) {
            seleniumAxisDescriptor = Jenkins.instance.getDescriptorOrDie(SeleniumAxis)
        }

        seleniumAxisDescriptor.server = seleniumFile
        seleniumAxisDescriptor.sauceLabs = true
        seleniumAxisDescriptor.sauceLabsName = 'test'
        seleniumAxisDescriptor.sauceLabsPwd = new Secret('pass')
        seleniumAxisDescriptor.sauceLabsAPIURL = sauceLabsFile

        def matrixProject = rule.createMatrixProject()

        SeleniumAxis axis = new SeleniumAxis('TEST', false, '', new Secret(''),
                seleniumAxisDescriptor.loadDefaultItems() )

        def axl = new AxisList()

        axl << axis
        matrixProject.setAxes(axl)

        matrixProject
    }

    MatrixProject configureManual() {

        def matrixProject = rule.createMatrixProject()

        def sc = new SeleniumCapability()
        def sc2 = new SeleniumCapability('Browser', 'Platform', 'Version', 'SEL')
        def scont = []
        scont.add(sc)
        scont.add(sc2)

        def axis = new SeleniumAxis('TEST', false, '', new Secret(''), scont)

        def axl = new AxisList()

        axl.add(axis)
        matrixProject.setAxes(axl)

        matrixProject
    }

    def 'Dynamic'() {
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

    def 'Dynamic Grid Gone'() {
        given:
        def matrixProject = configure('/grid-2.35.0.html', '/saucelabs_3.json')

        when:
        seleniumAxisDescriptor.setServer('null://')
        def build = matrixProject.scheduleBuild2(0).get()
        def runs = build.runs

        then:
        build.logFile.text.contains('SeleniumException')
        runs.size() == 0
    }

    def 'Dynamic Grid Arrived'() {
        given:

        def sad = Jenkins.instance.getDescriptor(SeleniumAxis)
        def matrixProject = rule.createMatrixProject()

        AxisList axl = new AxisList()
        sad.setServer('/empty-grid-2.35.0.html')

        def axis = new SeleniumAxis('TEST', false, '', new Secret(''), sad.loadDefaultItems())

        axl.add(axis)
        matrixProject.setAxes(axl)
        sad.setServer('/grid-2.25.0.html')

        def build = matrixProject.scheduleBuild2(0).get()

        expect: build.logFile.text.contains('SUCCESS')

        when:
        def runs = build.runs
        then:
        runs.each { assert it.logFile.text.contains('SUCCESS') }
    }

    def 'Empty Axis'() {
        given:
        def matrixProject = rule.createMatrixProject()

        AxisList axl = new AxisList()
        def axis = new SeleniumAxis('TEST', false, '', new Secret(''), null)

        axl.add(axis)
        matrixProject.setAxes(axl)
        def build = matrixProject.scheduleBuild2(0).get()

        expect:
        build.logFile.text.contains('SUCCESS')
        build.runs.size() == 0
    }

    def 'Manual'() {
        given:
        def matrixProject = configureManual()

        when:
        def build = matrixProject.scheduleBuild2(0).get()
        def runs = build.runs

        then:
        build.logFile.text.contains('SUCCESS')
        runs.every { it.logFile.text.contains('SUCCESS') }
        runs.size() == 2
    }
}
