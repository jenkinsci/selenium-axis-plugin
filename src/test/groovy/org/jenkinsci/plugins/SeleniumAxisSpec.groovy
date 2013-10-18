package org.jenkinsci.plugins

import hudson.matrix.TextAxis
import spock.lang.*
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule

class SeleniumAxisSpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    def sel = new Selenium(Selenium.load("/grid-2.35.0.html"), SeleniumCapabilityRO.class)
    //def selCap = new SeleniumDynamicCapability(sel.getSeleniumCapabilities())
    def selCap = new SeleniumDynamicCapability()
    def sdca = new ArrayList<ComplexAxisItem>()

    def 'Dynamic Add'() {
        given:

        def matrixProject = rule.createMatrixProject()
        sdca.add(selCap)
        SeleniumDynamicCapability.DescriptorImpl.getTopLevelDescriptor().setServer("/grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sdca)
        matrixProject.axes.add(axis)

        def build = matrixProject.scheduleBuild2(0).get()


        expect: build.logFile.text.contains("Hello, kiy0taka!")

        when:
        def runs = build.getRuns()
        then:
        runs.each(){assert it.logFile.text.contains("Hello, kiy0taka!")}
     
    }

    def 'Dynamic Add Grid Gone'() {
        given:

        def matrixProject = rule.createMatrixProject()
        sdca.add(selCap)
        SeleniumDynamicCapability.DescriptorImpl.getTopLevelDescriptor().setServer("/grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sdca)
        matrixProject.axes.add(axis)

        SeleniumDynamicCapability.DescriptorImpl.getTopLevelDescriptor().setServer("null://")
        def build = matrixProject.scheduleBuild2(0).get()

        expect: build.logFile.text.contains("Hello, kiy0taka!")

        when:
        def runs = build.getRuns()
        then:
        runs.each(){assert it.logFile.text.contains("Hello, kiy0taka!")}
     
    }

    def 'Dynamic Add Grid Arrived'() {
        given:

        def matrixProject = rule.createMatrixProject()
        sdca.add(selCap)
        SeleniumDynamicCapability.DescriptorImpl.getTopLevelDescriptor().setServer("/empty-grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sdca)
        matrixProject.axes.add(axis)

        SeleniumDynamicCapability.DescriptorImpl.getTopLevelDescriptor().setServer("/grid-2.25.0.html")

        def build = matrixProject.scheduleBuild2(0).get()

        expect: build.logFile.text.contains("Hello, kiy0taka!")

        when:
        def runs = build.getRuns()
        then:
        runs.each(){assert it.logFile.text.contains("Hello, kiy0taka!")}

    }

    def 'matrix'() {
        given:
        def matrixProject = rule.createMatrixProject()
        def axis = new TextAxis('TEST', "1", "2", "3")
        matrixProject.axes.add(axis)
    
        def b =  matrixProject.scheduleBuild2(0).get()

        expect: b.logFile.text.contains("SUCCESS")

        b.getRuns().each(){
            assert it.logFile.text.contains("SUCCESS")
        }
    }
}


