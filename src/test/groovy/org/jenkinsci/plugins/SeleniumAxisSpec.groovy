package org.jenkinsci.plugins

import hudson.matrix.TextAxis
import spock.lang.*
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule

class SeleniumAxisSpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    def sel = new Selenium(Selenium.load("/grid-2.35.0.html"), SeleniumCapabilityRO.class)
    def selCap = new SeleniumDynamicCapability(sel.getSeleniumCapabilities())
    def sdca = new ArrayList<ComplexAxisItem>()

    def 'Dynamic Add'() {
        given:

        def matrixProject = rule.createMatrixProject()
        sdca.add(selCap)
        SeleniumDynamicCapability.DescriptorImpl.getTopLevelDescriptor().setServer("/grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sdca)
        matrixProject.axes.add(axis)

        when:
        def build = matrixProject.scheduleBuild2(0).get()
        then:
        build.logFile.text.contains("Hello, kiy0taka!")

        when:
        def runs = matrixProject.scheduleBuild2(0).get().getRuns()
        then:
        runs.each(){it.logFile.text.contains("Hello, kiy0taka!")}
     
    }

    def 'matrix'() {
        given:
        def matrixProject = rule.createMatrixProject()
        def axis = new TextAxis('TEST', "1", "2", "3")
        matrixProject.axes.add(axis)
    
        def b =  matrixProject.scheduleBuild2(0).get()

        expect: b.logFile.text.contains("SUCCESS")

        b.getRuns().each(){
            assert it.logFile.text.contains("TEST/1")
        }
    }

    def 'matrix2'() {
        given:
        def matrixProject = rule.createMatrixProject()
        def axis = new TextAxis('TEST', "1", "2", "3")
        matrixProject.axes.add(axis)

        expect:
        matrixProject.scheduleBuild2(0).get().getRuns().each(){
            //logFile.text.contains("Some String!")
            //runs.every { it.logFile.text.contains("Another String") }
            assert it.logFile.text.contains("TEST/1")
        }
    }

    def 'matrix3'() {
        given:
        def matrixProject = rule.createMatrixProject()
        def axis = new TextAxis('TEST', "1", "2", "3")
        matrixProject.axes.add(axis)

        expect:
        matrixProject.scheduleBuild2(0).get().with(){
            logFile.text.contains("Some String!")
            getRuns().each() { assert it.logFile.text.contains("TEST/1") }
        }
    }

    def 'matrix4'() {
        given:
        def matrixProject = rule.createMatrixProject()
        def axis = new TextAxis('TEST', "1", "2", "3")
        matrixProject.axes.add(axis)

        expect:
        with({ matrixProject.scheduleBuild2(0).get()}){
            logFile.text.contains("Some String!")
            it.getRuns().each() { assert it.logFile.text.contains("TEST/1") }
        }
    }


}


