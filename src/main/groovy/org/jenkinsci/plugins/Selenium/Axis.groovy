/*
* The MIT License
*
* Copyright (c) 2010, InfraDNA, Inc.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package org.jenkinsci.plugins.selenium

import hudson.Extension
import hudson.init.InitMilestone
import hudson.init.Initializer
import hudson.model.Items
import net.sf.json.JSONObject
import org.kohsuke.stapler.DataBoundConstructor
import hudson.util.FormValidation
import org.kohsuke.stapler.QueryParameter
import jenkins.model.Jenkins
import org.jenkinsci.complex.axes.AxisDescriptor
import org.jenkinsci.complex.axes.Item
import org.jenkinsci.complex.axes.ItemDescriptor
import hudson.util.Secret
import org.kohsuke.stapler.StaplerRequest
import hudson.model.Descriptor.FormException

class Axis extends org.jenkinsci.complex.axes.Axis {
    Boolean slOverride
    String slName
    Secret slPassword

    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    public static void addAliases() {
        Items.XSTREAM2.addCompatibilityAlias("org.jenkinsci.plugins.SeleniumAxis", Axis);
    }

    @DataBoundConstructor
    Axis(String name, Boolean slOverride, String slName, Secret slPassword,
         List<? extends Item> seleniumCapabilities) {
        super(name, seleniumCapabilities)

        this.slOverride = slOverride
        this.slName = slName
        this.slPassword = slPassword
    }

    List<? extends Manual> getSeleniumCapabilities() {
        this.complexAxisItems as List<? extends Manual>
    }

    String getURL(String which) {
        if (which == 'SEL') {
            descriptor.server
        } else if (which == 'SL') {
            if (slOverride) {
                buildURL(slName, slPassword, descriptor.sauceLabsURL)
            } else {
                buildURL(descriptor.sauceLabsName, descriptor.sauceLabsPwd, descriptor.sauceLabsURL)
            }
        }
    }

    static String buildURL(String name, Secret pwd, String url) {
        String myurl = url - 'http://'
        "http://${name}:${pwd}@${myurl}"
    }

    @Override
    void addBuildVariable(String value, Map<String,String> map) {

       //so the value is PLATFORM-BROWSER-VERSION

       List<String> parts = value.split(/-/)

       map.put(name + '_PLATFORM', parts[1])
       map.put(name + '_BROWSER', parts[2])
       map.put(name + '_VERSION', parts[3])
       map.put(name + '_URL', getURL(parts[0]))
    }

    @Extension
    static class DescriptorImpl extends AxisDescriptor {

        final String displayName = 'Selenium Capability Axis'

        List<ItemDescriptor> axisItemTypes() {
            def ait = Jenkins.instance.<Item,ItemDescriptor>getDescriptorList(Item)

            List<ItemDescriptor> ret = []

            for (int i = 0; i < ait.size(); i++) {
                String name = ait.get(i).getClass().name

                //don't want the RO version to appear in the add list as it is added as part of the Dynamic item
                if (!name.contains('RO$')) {
                    ret.add(ait.get(i))
                }
            }
            ret
        }

//        List<? extends Capability> getSauceLabsCapabilities(String which) {
//            if (sauceLabs) {
//                try {
//                    if (sauceLabsCapabilities == null) {
//                        ICapabilityReader reader = new org.jenkinsci.plugins.saucelabs.CapabilityReader()
//                        reader.loadCapabilities(sauceLabsAPIURL)
//
//                        Selenium sel = new Selenium(reader, org.jenkinsci.plugins.saucelabs.CapabilityRO)
//                        sauceLabsCapabilities = [:]
//
//                        sauceLabsCapabilities['latest'] = sel.seleniumLatest
//                        sauceLabsCapabilities['all'] = sel.seleniumCapabilities
//                        sauceLabsCapabilities['web'] = sel.seleniumSelected
//                    }
//                    return sauceLabsCapabilities[which]
//
//                } catch (ex) {
//                    ItemList.emptyList()
//                }
//            } else {
//                ItemList.emptyList()
//            }
//        }
//
//        List<? extends Capability> getSeleniumCapabilities() {
//            try {
//                //def sel = new Selenium(Selenium.load(server), SeleniumCapabilityRO)
//                ICapabilityReader reader = new org.jenkinsci.plugins.hub.CapabilityReader()
//
//                reader.loadCapabilities(server)
//
//                Selenium sel = new Selenium(reader, CapabilityRO)
//                sel.seleniumLatest
//
//            } catch (ex) {
//                ItemList.emptyList()
//            }
//        }
//        List<? extends Capability> getRandomSauceLabsCapabilities(String which, Integer count, SecureGroovyScript secureFilter) {
//
//            ItemList<? extends Capability> selected = new ItemList<? extends Capability>()
//            def cap = getSauceLabsCapabilities(which)
//            def myCap = cap.clone()
//
//            Collections.shuffle(myCap)
//
//            if (myCap.size() == 0) {
//                return ItemList.emptyList()
//            }
//
//            while (count > 0 && myCap.size() > 0) {
//                def current = myCap.pop()
//                boolean differentEnough = true
//
//                if( secureFilter.script != '') {
//                    Binding binding = new Binding()
//                    binding.setVariable('current', current)
//                    binding.setVariable('selected', selected)
//
//                    differentEnough = secureFilter.evaluate(getClass().classLoader, binding)
//                } else {
//                    differentEnough = defaultDifferent(current, selected)
//                }
//
//                if (differentEnough) {
//                    selected << current
//                    count--
//                }
//
//            }
//            selected
//        }
//
//        static boolean defaultDifferent(Item current, List<ItemList> selected) {
//            def different = true
//            selected.any {
//                if (Levenshtien.distance(current.toString(), it.toString()) < 12) {
//                    different = false
//                    true
//                }
//            }
//            return different
//        }

//        @Override
//        boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
//            super.configure( req, formData)
//            sauceLabsCapabilities = null
//            true
//        }

//        FormValidation doCheckServer(@QueryParameter String value) {
//            if (value.isEmpty()) {
//                return FormValidation.error('You must provide an URL.')
//            }
//
//            try {
//                new URL(value)
//            } catch (final MalformedURLException e) {
//                return FormValidation.error('This is not a valid URL.')
//            }
//            FormValidation.ok()
//        }
//
//        FormValidation doCheckSauceLabsURL(@QueryParameter String value) {
//            if (value.isEmpty()) {
//                return FormValidation.error('You must provide an URL.')
//            }
//
//            try {
//                new URL(value)
//            } catch (final MalformedURLException e) {
//                return FormValidation.error('This is not a valid URL.')
//            }
//            FormValidation.ok()
//        }
//
//        FormValidation doCheckSauceLabsAPIURL(@QueryParameter String value) {
//            if (value.isEmpty()) {
//                return FormValidation.error('You must provide an URL.')
//            }
//
//            try {
//                new URL(value)
//            } catch (final MalformedURLException e) {
//                return FormValidation.error('This is not a valid URL.')
//            }
//            FormValidation.ok()
//        }
    }
}
