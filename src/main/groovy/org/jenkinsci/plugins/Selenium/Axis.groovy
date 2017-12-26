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
import hudson.util.Secret
import jenkins.model.Jenkins
import org.jenkinsci.complex.axes.AxisDescriptor
import org.jenkinsci.complex.axes.Item
import org.jenkinsci.complex.axes.ItemDescriptor
import org.jenkinsci.plugins.hub.DynamicCapability
import org.kohsuke.stapler.DataBoundConstructor

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
        def descriptor = Jenkins.instance.getDescriptor(Axis)

        descriptor.axisItemTypes().find { d -> d.getURL(which) != ""}
    }


    @Override
    void addBuildVariable(String value, Map<String,String> map) {

       //so the value is TYPE-PLATFORM-BROWSER-VERSION

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
    }
}
