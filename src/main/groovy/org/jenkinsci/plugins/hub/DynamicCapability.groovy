package org.jenkinsci.plugins.hub

import hudson.Extension
import org.jenkinsci.plugins.selenium.Axis
import org.jenkinsci.plugins.selenium.Exception
import org.kohsuke.stapler.DataBoundConstructor
import hudson.model.Descriptor
import jenkins.model.Jenkins
import org.jenkinsci.complex.axes.AxisDescriptor
import org.jenkinsci.complex.axes.Item
import org.jenkinsci.complex.axes.Container
import org.jenkinsci.complex.axes.ContainerDescriptor

class DynamicCapability extends  Container {

    DynamicCapability() {
        super( [] )
    }

    List<CapabilityRO> getSeleniumCapabilities() {
        complexAxisItems
    }

    void setSeleniumCapabilities(List<CapabilityRO> sc) {
        setComplexAxisItems(sc)
    }

    @DataBoundConstructor
    DynamicCapability(List<CapabilityRO> seleniumCapabilities) {
        super( seleniumCapabilities)
    }

    String toString() {
        'DetectedSelenium'
    }

    @Override
    List<String> rebuild(List<String> list) {
        List<? extends Item> sc = descriptor.loadDefaultItems()

        if (sc.size() == 0) {
            throw (new Exception('No capabilities detected'))
        }
        setSeleniumCapabilities(sc)

        sc.each { list.add(it.toString()) }
        list
    }

    @Override
    List<String> getValues(List<String> list) {
        seleniumCapabilities.each { list.add(it.toString()) }

        if (list.size() == 0) {
            list.add('Rebuilt at build time')
        }
        list
    }

    @Extension static class DescriptorImpl extends ContainerDescriptor {

        //so we need this to get at the name of the selenium server in the global config
        static Descriptor<? extends AxisDescriptor> getTopLevelDescriptor() {
            Axis.DescriptorImpl sad = Jenkins.instance.getDescriptor(Axis)
            sad.load()

            sad
        }

        @Override
        List<? extends Item> loadDefaultItems(List<? extends Item> cai) {
            DynamicCapability sdc = new DynamicCapability(loadDefaultItems())

            cai.add(sdc)

            cai
        }

        @Override
        List<? extends Item> loadDefaultItems() {
            topLevelDescriptor.seleniumCapabilities
        }

        String displayName = 'Detected Selenium Capability'
    }
}
