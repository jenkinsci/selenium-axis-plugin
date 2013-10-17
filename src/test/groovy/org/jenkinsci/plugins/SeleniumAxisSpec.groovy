package org.jenkinsci.plugins

import hudson.matrix.TextAxis
import spock.lang.*
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule

class SeleniumAxisSpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    //def sel = new Selenium(Selenium.load("/grid-2.35.0.html"), SeleniumCapabilityRO.class)
    //def selCap = new SeleniumDynamicCapability(sel.getSeleniumCapabilities())
    //def sdca = new ArrayList<ComplexAxisItem>()

    //def 'Dynamic Add'() {
    //    given:

     //   def matrixProject = rule.createMatrixProject()
     //   sdca.add(selCap)
     //   SeleniumDynamicCapability.DescriptorImpl.getTopLevelDescriptor().setServer("/grid-2.35.0.html")

     //   def axis = new SeleniumAxis('TEST', sdca)
     //   matrixProject.axes.add(axis)

        //when:
        //def build = matrixProject.scheduleBuild2(0).get()
        //then:
        //build.logFile.text.contains("Hello, kiy0taka!")

     //   when:
     //   def runs = matrixProject.scheduleBuild2(0).get().getRuns()
     //   then:
     //   runs.each(){it.logFile.text.contains("Hello, kiy0taka!")}
     //
    //}

    //def 'matrix'() {
    //    given:
    //    def matrixProject = rule.createMatrixProject()
    //    def axis = new TextAxis('TEST', "1", "2", "3")
    //    matrixProject.axes.add(axis)
    //
        //expect: matrixProject.scheduleBuild2(0).get().logFile.text.contains("Some String!")

    //    matrixProject.scheduleBuild2(0).get().getRuns().every{
    //        expect: it.logFile.text.contains("Another String")
    //    }
    //}

    def 'matrix2'() {
        given:
        def matrixProject = rule.createMatrixProject()
        def axis = new TextAxis('TEST', "1", "2", "3")
        matrixProject.axes.add(axis)

        expect:
        matrixProject.scheduleBuild2(0).get().getRuns().every{
            //logFile.text.contains("Some String!")
            //runs.every { it.logFile.text.contains("Another String") }
            it.logFile.text.contains("Another String")
        }
    }
}

