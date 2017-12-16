package org.jenkinsci.plugins.saucelabs

import hudson.Extension
import hudson.init.InitMilestone
import hudson.init.Initializer
import hudson.model.Items
import hudson.util.ListBoxModel
import hudson.util.ListBoxModel.Option
import hudson.util.FormValidation
import org.jenkinsci.complex.axes.Item
import org.jenkinsci.complex.axes.ItemList
import org.jenkinsci.plugins.saucelabs.CapabilityRO
import org.jenkinsci.plugins.selenium.Exception
import org.kohsuke.stapler.DataBoundConstructor
import org.kohsuke.stapler.QueryParameter
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript
import org.jenkinsci.plugins.scriptsecurity.scripts.ApprovalContext
import org.jenkinsci.plugins.scriptsecurity.scripts.ClasspathEntry

class DynamicCapability extends  org.jenkinsci.plugins.hub.DynamicCapability {

    Integer number = 3
    Boolean advanced = false
    String criteria
    @SuppressWarnings('UnnecessaryTransientModifier')
    transient  String filter
    SecureGroovyScript secureFilter

    Boolean override = false

    DynamicCapability() {
        super([])
    }

    DynamicCapability(List<org.jenkinsci.plugins.saucelabs.CapabilityRO> sauceLabsCapabilities) {
        super( sauceLabsCapabilities)
    }

    @Override
    List<String> rebuild(List<String> list) {
        if (this.advanced) {
            setSeleniumCapabilities(descriptor.topLevelDescriptor.getRandomSauceLabsCapabilities(
                    this.criteria, this.number, this.secureFilter))
        } else {
            setSeleniumCapabilities(descriptor.topLevelDescriptor.getRandomSauceLabsCapabilities(
                    'latest', this.number, new SecureGroovyScript('', true)))
        }
            seleniumCapabilities.each { list.add(it.toString()) }
        list
    }

    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    public static void addAliases() {
        Items.XSTREAM2.addCompatibilityAlias("org.jenkinsci.plugins.SauceLabsDynamicCapability", DynamicCapability);
    }

    @DataBoundConstructor
    DynamicCapability(String number, Boolean advanced, String criteria, SecureGroovyScript secureFilter ) {
        if (number.isNumber()) {
            this.number = number.toInteger()
        } else {
            this.number = 3
        }
        this.advanced = advanced
        this.criteria = criteria
        this.secureFilter = secureFilter
        this.secureFilter.configuringWithKeyItem()

        try {
            this.complexAxisItems = descriptor.topLevelDescriptor.getRandomSauceLabsCapabilities(
                    this.criteria, this.number, this.secureFilter)
        } catch (Exception) {
            this.complexAxisItems = ItemList.emptyList()
        }
    }

    void setSauceLabsCapabilities(List<CapabilityRO> sc) {
        setComplexAxisItems(sc)
    }

    String getSauceLabsCapabilities() {
        complexAxisItems
    }

    String toString() {
        'DetectedSauceLibs'
    }

    @SuppressWarnings('UnusedPrivateMethod')
    private Object readResolve() {
        if (filter != null) {
            List<ClasspathEntry> cp = []

            secureFilter = new SecureGroovyScript(filter, false, cp).configuring(ApprovalContext.create())
            filter = null
        }
        this
    }

    @Extension static class DescriptorImpl extends org.jenkinsci.plugins.hub.DynamicCapability.DescriptorImpl {

        @Override
        List<? extends Item> loadDefaultItems(List<? extends Item> cai) {
            DynamicCapability sdc = new DynamicCapability(loadDefaultItems())

            cai.add(sdc)

            cai
        }

        @Override
        List<? extends Item> loadDefaultItems() {
            //topLevelDescriptor.sauceLabsCapabilities
            ItemList.emptyList()
        }

        String displayName = 'Detected SauceLibs Capability'

        FormValidation doCheckSlNum(@QueryParameter String value) {
            if (value.isEmpty()) {
                return FormValidation.error('You must provide a number of points to Add.')
            }

            try {
                Integer val = new Integer(value)
                if (val < 0) {
                    return FormValidation.error('This is not a valid Number.')
                }
            } catch (final NumberFormatException  e) {
                return FormValidation.error('This is not a valid Number.')
            }

            FormValidation.ok()
        }

        ListBoxModel doFillCriteriaItems(@QueryParameter String criteria) {
            ListBoxModel cbm = new ListBoxModel()

            cbm << new Option('All', 'all', 'all' == criteria)
            cbm << new Option('Latest Browsers', 'latest', 'latest' == criteria)
            cbm << new Option('Latest Firefox, Chrome, Safari, IE, Edge', 'web', 'web' == criteria)

            cbm
        }

        FormValidation doRebuild(@QueryParameter('secureFilter') final SecureGroovyScript secureFilter,
                                        @QueryParameter('criteria') final String criteria,
                                        @QueryParameter('number') final Integer number) throws Exception {
            try {
                String s = topLevelDescriptor.getRandomSauceLabsCapabilities(criteria, number, secureFilter)

                return FormValidation.ok(s)
            } catch (Exception e) {
                return FormValidation.error('Client error : ' + e.message)
            }
        }
    }
}