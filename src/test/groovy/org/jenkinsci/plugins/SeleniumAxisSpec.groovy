package org.jenkinsci.plugins

import hudson.matrix.*
import jenkins.model.Jenkins
import spock.lang.*
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule

class SeleniumAxisSpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    @Shared seleniumAxisDescriptor

    MatrixProject configure(seleniumFile){

        if(seleniumAxisDescriptor == null){
            seleniumAxisDescriptor= Jenkins.getInstance().getDescriptor(SeleniumAxis.class)
        }
        def matrixProject = rule.createMatrixProject()

        def axis = new SeleniumAxis('TEST', seleniumAxisDescriptor.loadDefaultItems())
        def axl = new AxisList();

        seleniumAxisDescriptor.setServer(seleniumFile)

        axl.add(axis)
        matrixProject.setAxes(axl)

        matrixProject
    }


    def 'Dynamic'() {
        given:
        def matrixProject = configure("/grid-2.35.0.html")

        when:
        def build = matrixProject.scheduleBuild2(0).get()
        def runs = build.getRuns()

        then:
        build.logFile.text.contains("SUCCESS")
        runs.every(){it.logFile.text.contains("SUCCESS")}
        runs.size() == 4

    }

    def 'Dynamic Grid Gone'() {
        given:
        def sad = Jenkins.getInstance().getDescriptor(SeleniumAxis.class)

        def matrixProject = rule.createMatrixProject()
        AxisList axl = new AxisList();

        sad.setServer("/grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sad.loadDefaultItems())
        axl.add(axis)
        matrixProject.setAxes(axl)

        sad.setServer("null://")
        def build = matrixProject.scheduleBuild2(0).get()

        expect:
        build.logFile.text.contains("SeleniumException")
        build.getRuns().size() == 0

    }

    def 'Dynamic Grid Arrived'() {
        given:

        def sad = Jenkins.getInstance().getDescriptor(SeleniumAxis.class)
        def matrixProject = rule.createMatrixProject()

        AxisList axl = new AxisList();
        sad.setServer("/empty-grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sad.loadDefaultItems())

        axl.add(axis)
        matrixProject.setAxes(axl)
        sad.setServer("/grid-2.25.0.html")

        def build = matrixProject.scheduleBuild2(0).get()

        expect: build.logFile.text.contains("SUCCESS")

        when:
        def runs = build.getRuns()
        then:
        runs.each(){assert it.logFile.text.contains("SUCCESS")}

    }

    def 'Empty Axis'() {
        given:

        def matrixProject = rule.createMatrixProject()

        AxisList axl = new AxisList();
        def axis = new SeleniumAxis('TEST', null)

        axl.add(axis)
        matrixProject.setAxes(axl)
        def build = matrixProject.scheduleBuild2(0).get()

        expect:
        build.logFile.text.contains("SUCCESS")
        build.getRuns().size()==0
    }
}


