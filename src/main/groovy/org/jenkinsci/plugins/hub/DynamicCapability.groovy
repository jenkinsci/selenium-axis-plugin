package org.jenkinsci.plugins.hub

import hudson.Extension
import hudson.init.InitMilestone
import hudson.init.Initializer
import hudson.model.Items
import hudson.util.FormValidation
import net.sf.json.JSONObject
import org.jenkinsci.complex.axes.ItemList
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript
import org.jenkinsci.plugins.selenium.Axis
import org.jenkinsci.plugins.selenium.Exception
import org.jenkinsci.plugins.selenium.ICapability
import org.jenkinsci.plugins.selenium.ICapabilityReader
import org.kohsuke.stapler.DataBoundConstructor
import hudson.model.Descriptor
import jenkins.model.Jenkins
import org.jenkinsci.complex.axes.AxisDescriptor
import org.jenkinsci.complex.axes.Item
import org.jenkinsci.complex.axes.Container
import org.jenkinsci.complex.axes.ContainerDescriptor
import org.kohsuke.stapler.QueryParameter
import org.kohsuke.stapler.StaplerRequest
import hudson.model.Descriptor.FormException

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

    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    public static void addAliases() {
        Items.XSTREAM2.addCompatibilityAlias("org.jenkinsci.plugins.SeleniumDynamicCapability", DynamicCapability);
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

    @Extension static class DescriptorImpl extends ContainerDescriptor implements ICapability{
        String server = 'http://localhost:4444'

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
        boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            super.configure( req, formData)
            //capabilities = null
            true
        }

        FormValidation doCheckServer(@QueryParameter String value) {
            if (value.isEmpty()) {
                return FormValidation.error('You must provide an URL.')
            }

            try {
                new URL(value)
            } catch (final MalformedURLException e) {
                return FormValidation.error('This is not a valid URL.')
            }
            FormValidation.ok()
        }

        List<? extends Capability> getCapabilities(String which) {
            try {
                //def sel = new Selenium(Selenium.load(server), SeleniumCapabilityRO)
                ICapabilityReader reader = new org.jenkinsci.plugins.hub.CapabilityReader()

                reader.loadCapabilities(server)

                Selenium sel = new Selenium(reader, CapabilityRO)
                sel.seleniumLatest

            } catch (ex) {
                ItemList.emptyList()
            }
        }
        List<? extends Capability> getRandomCapabilities(String which, Integer count, SecureGroovyScript secureFilter) {
            getCapabilities(which)
        }

        @Override
        List<? extends Item> loadDefaultItems() {
            getCapabilities('')
        }

        String displayName = 'Detected Selenium Capability'
    }
}
