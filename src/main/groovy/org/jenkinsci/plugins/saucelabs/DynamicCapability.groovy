package org.jenkinsci.plugins.saucelabs

import hudson.Extension
import hudson.init.InitMilestone
import hudson.init.Initializer
import hudson.model.Descriptor.FormException
import hudson.model.Items
import hudson.util.FormValidation
import hudson.util.ListBoxModel
import hudson.util.Secret
import net.sf.json.JSONObject
import org.jenkinsci.complex.axes.Container
import org.jenkinsci.complex.axes.Item
import org.jenkinsci.complex.axes.ItemList
import org.jenkinsci.plugins.hub.Selenium
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript
import org.jenkinsci.plugins.scriptsecurity.scripts.ApprovalContext
import org.jenkinsci.plugins.scriptsecurity.scripts.ClasspathEntry
import org.jenkinsci.plugins.selenium.ICapability
import org.jenkinsci.plugins.selenium.ICapabilityReader
import org.jenkinsci.plugins.selenium.Manual
import org.kohsuke.stapler.DataBoundConstructor
import org.kohsuke.stapler.QueryParameter
import org.kohsuke.stapler.StaplerRequest

class DynamicCapability extends Container {
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

    DynamicCapability(List<org.jenkinsci.plugins.saucelabs.CapabilityRO> capabilities) {
        super( capabilities)
    }

    @Override
    List<String> rebuild(List<String> list) {
        if (this.advanced) {
            setComplexAxisItems(descriptor.getRandomCapabilities(
                    this.criteria, this.number, this.secureFilter))
        } else {
            setComplexAxisItems(descriptor.getRandomCapabilities(
                    'latest', this.number, new SecureGroovyScript('', true)))
        }
            getComplexAxisItems().each { list.add(it.toString()) }
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
            this.complexAxisItems = descriptor.getRandomCapabilities(
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

//    String toString() {
//        'DetectedSauceLibs'
//    }

    @SuppressWarnings('UnusedPrivateMethod')
    private Object readResolve() {
        if (filter != null) {
            List<ClasspathEntry> cp = []

            secureFilter = new SecureGroovyScript(filter, false, cp).configuring(ApprovalContext.create())
            filter = null
        }
        this
    }

    @Override
    List<String> getValues(List<String> list) {
        complexAxisItems.each { list.add(it.toString()) }

        if (list.size() == 0) {
            list.add('Rebuilt at build time')
        }
        list
    }

    @Extension static class DescriptorImpl extends org.jenkinsci.plugins.hub.DynamicCapability.DescriptorImpl implements ICapability{
        Boolean sauceLabs = false
        String sauceLabsName
        Secret sauceLabsPwd
        String sauceLabsAPIURL = 'http://saucelabs.com/rest/v1/info/platforms/webdriver'
        String sauceLabsURL = 'http://ondemand.saucelabs.com:80'

        //don't serialize this
        @SuppressWarnings('UnnecessaryTransientModifier')
        transient Map<String, List<? extends Manual>> capabilities

        DescriptorImpl( ) {
            super()
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


        FormValidation doCheckSauceLabsURL(@QueryParameter String value) {
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

        FormValidation doCheckSauceLabsAPIURL(@QueryParameter String value) {
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

        @Override
        List<? extends Item> loadDefaultItems() {
            //topLevelDescriptor.sauceLabsCapabilities
            ItemList.emptyList()
        }

        String displayName = 'Detected SauceLabs Capability'


        List<? extends Manual> getCapabilities(String which) {
            if (sauceLabs) {
                try {
                    if (capabilities == null) {
                        ICapabilityReader reader = new org.jenkinsci.plugins.saucelabs.CapabilityReader()
                        reader.loadCapabilities(sauceLabsAPIURL)

                        Selenium sel = new Selenium(reader, org.jenkinsci.plugins.saucelabs.CapabilityRO)
                        capabilities = [:]

                        capabilities['latest'] = sel.seleniumLatest
                        capabilities['all'] = sel.seleniumCapabilities
                        capabilities['web'] = sel.seleniumSelected
                    }
                    return capabilities[which]

                } catch (ex) {
                    ItemList.emptyList()
                }
            } else {
                ItemList.emptyList()
            }
        }

        List<? extends Manual> getRandomCapabilities(String which, Integer count, SecureGroovyScript secureFilter) {

            ItemList<? extends Manual> selected = new ItemList<? extends Manual>()
            def cap = getCapabilities(which)
            def myCap = cap.clone()

            Collections.shuffle(myCap)

            if (myCap.size() == 0) {
                return ItemList.emptyList()
            }

            while (count > 0 && myCap.size() > 0) {
                def current = myCap.pop()
                boolean differentEnough = true

                if( secureFilter.script != '') {
                    Binding binding = new Binding()
                    binding.setVariable('current', current)
                    binding.setVariable('selected', selected)

                    differentEnough = secureFilter.evaluate(getClass().classLoader, binding)
                } else {
                    differentEnough = defaultDifferent(current, selected)
                }

                if (differentEnough) {
                    selected << current
                    count--
                }

            }
            selected
        }

        static boolean defaultDifferent(Item current, List<ItemList> selected) {
            def different = true
            selected.any {
                if (Levenshtien.distance(current.toString(), it.toString()) < 12) {
                    different = false
                    true
                }
            }
            return different
        }

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

            cbm << new ListBoxModel.Option('All', 'all', 'all' == criteria)
            cbm << new ListBoxModel.Option('Latest Browsers', 'latest', 'latest' == criteria)
            cbm << new ListBoxModel.Option('Latest Firefox, Chrome, Safari, IE, Edge', 'web', 'web' == criteria)

            cbm
        }

        FormValidation doRebuild(@QueryParameter('secureFilter') final SecureGroovyScript secureFilter,
                                        @QueryParameter('criteria') final String criteria,
                                        @QueryParameter('number') final Integer number) throws Exception {
            try {
                String s = getRandomCapabilities(criteria, number, secureFilter)

                return FormValidation.ok(s)
            } catch (Exception e) {
                return FormValidation.error('Client error : ' + e.message)
            }
        }
    }
}
