package org.jenkinsci.plugins

import hudson.matrix.TextAxis
import jenkins.model.Jenkins
import spock.lang.*
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule

class SeleniumAxisSpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()


    def 'Dynamic'() {
        given:

        def matrixProject = rule.createMatrixProject()
        def sad = Jenkins.getInstance().getDescriptor(SeleniumAxis.class)

        sad.setServer("/grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sad.loadDefaultItems())
        matrixProject.axes.add(axis)

        def build = matrixProject.scheduleBuild2(0).get()

        expect: build.logFile.text.contains("SUCCESS")

        when:
        def runs = build.getRuns()
        then:
        runs.each(){assert it.logFile.text.contains("SUCCESS")}
     
    }

    def 'Dynamic Grid Gone'() {
        given:
        def sad = Jenkins.getInstance().getDescriptor(SeleniumAxis.class)

        def matrixProject = rule.createMatrixProject()

        sad.setServer("/grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sad.loadDefaultItems())
        matrixProject.axes.add(axis)

        sad.setServer("null://")
        def build = matrixProject.scheduleBuild2(0).get()

        expect: build.logFile.text.contains("Hello, kiy0taka!")

        when:
        def runs = build.getRuns()
        then:
        runs.each(){assert it.logFile.text.contains("Hello, kiy0taka!")}
     
    }

    def 'Dynamic Grid Arrived'() {
        given:

        def sad = Jenkins.getInstance().getDescriptor(SeleniumAxis.class)
        def matrixProject = rule.createMatrixProject()

        sad.setServer("/empty-grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sad.loadDefaultItems())
        matrixProject.axes.add(axis)

        sad.setServer("/grid-2.25.0.html")

        def build = matrixProject.scheduleBuild2(0).get()

        expect: build.logFile.text.contains("Hello, kiy0taka!")

        when:
        def runs = build.getRuns()
        then:
        runs.each(){assert it.logFile.text.contains("Hello, kiy0taka!")}

    }

    def 'Empty Axis'() {
        given:

        def matrixProject = rule.createMatrixProject()

        def axis = new SeleniumAxis('TEST', null)
        matrixProject.axes.add(axis)

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


