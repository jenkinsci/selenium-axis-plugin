
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
package org.jenkinsci.plugins

import hudson.Extension
import hudson.Functions
import hudson.Util
import jenkins.model.Jenkins
import org.kohsuke.stapler.DataBoundConstructor
import hudson.matrix.*
import hudson.model.labels.LabelAtom


import java.util.Arrays;
import java.util.List;

/**
 * {@link Axis} that selects available JDKs.
 *
 * @author Kohsuke Kawaguchi
 */
public class SeleniumAxis extends Axis {
    /**
     * JDK axis was used to be stored as a plain "Axis" with the name "jdk",
     * so it cannot be configured by any other name.
     */
    @DataBoundConstructor
    public SeleniumAxis(String name, List<String> values) {
        super(name, values);
    }

    @Override
    public boolean isSystem() {
        return true;
    }

    @Override
    public String getValueString() {
        return Util.join(getValues(),"/");
    }
    @Override
    public void addBuildVariable(String value, Map<String,String> map) {

       //so the value is PLATFORM-BROWSER-VERSION

       def parts = value.split(/-/)

       map.put(name + "_PLATFORM", parts[0]);
       map.put(name + "_BROWSER", parts[1]);
       map.put(name + "_VERSION", parts[2]);
    }

    @Extension
    public static class DescriptorImpl extends AxisDescriptor {

        private Selenium sel

        public DescriptorImpl(){
            sel = new Selenium("http://oriel-lc-devci:4444")
        }

        public List<SeleniumCapability> getSeleniumCapabilities(){
            return sel.seleniumCapabilities
        }

        @Override
        public String getDisplayName() {
            return "Selenium Capability Axis"
        }

        @Override
        public boolean isInstantiable() {
            return true
        }


        private String jsstr(String body, Object... args) {
            return '\"'+Functions.jsStringEscape(String.format(body,args))+'\"';
        }

        public String buildLabelCheckBox(SeleniumCapability sc, LabelAxis instance) {
            return jsstr("<input type='checkbox' name='values' json='%s' /><label class='attach-previous'>%s</label>",
                    Functions.htmlAttributeEscape(sc.toString()),
                    sc.toString())
            // '${h.jsStringEscape('<input type="checkbox" name="values" json="'+h.htmlAttributeEscape(l.name)+'" ')}'+has("${h.jsStringEscape(l.name)}")+'${h.jsStringEscape('/><label class="attach-previous">'+l.name+' ('+l.description+')</label>')}'
        }

    }
}