package org.jenkinsci.plugins

import spock.lang.*
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
//import hudson.tasks.Ant

class SeleniumAxisSpec extends Specification {

    @Rule
    JenkinsRule rule = new JenkinsRule()

    def 'Dynamic Add'() {

        given:
        def matrixProject = rule.createMatrixProject()

        def sel = new Selenium(Selenium.load("/grid-2.35.0.html"), SeleniumCapabilityRO.class)
        def selCap = new SeleniumDynamicCapability(sel.getSeleniumCapabilities())
        def sdca = new ArrayList<ComplexAxisItem>()

        sdca.add(selCap)
        SeleniumDynamicCapability.DescriptorImpl.getTopLevelDescriptor().setServer("/grid-2.35.0.html")

        def axis = new SeleniumAxis('TEST', sel.getSeleniumCapabilities())
        matrixProject.axes.add(axis)

        def axis2 = new SeleniumAxis('TEST2', sdca)
        matrixProject.axes.add(axis2)

        when:
        def build = matrixProject.scheduleBuild2(0).get()

        then:
        build.logFile.text.contains("Hello, kiy0taka!")
    }

    //def 'say Bonjour'() {
    //    given:
    //    def builder = new SeleniumAxis('kiy0taka')
    //    builder.descriptor.useFrench = true
    //
    //   and:
    //    def job = rule.createFreeStyleProject()
    //    job.buildersList.add(builder)

    //    when:
    //    def build = job.scheduleBuild2(0).get()

    //    then:
    //    build.logFile.text.contains("Bonjour, kiy0taka!")
    //}
}