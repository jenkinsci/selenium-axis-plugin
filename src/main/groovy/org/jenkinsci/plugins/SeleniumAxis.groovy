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
import hudson.ExtensionPoint
import hudson.Functions
import hudson.Util
import hudson.matrix.Axis
import hudson.matrix.AxisDescriptor
import hudson.model.Descriptor
import hudson.matrix.LabelAxis
import net.sf.json.JSONObject
import org.kohsuke.stapler.DataBoundConstructor
import org.kohsuke.stapler.StaplerRequest
import hudson.model.Descriptor.FormException
import hudson.util.FormValidation
import org.kohsuke.stapler.QueryParameter;
import hudson.model.AbstractDescribableImpl
import hudson.util.ListBoxModel
import hudson.DescriptorExtensionList
import jenkins.model.Jenkins

public class SeleniumAxis extends Axis{

    public final List<SeleniumCapability> seleniumCapabilities

    public SeleniumAxis(String name, String value){
        super(name, value)
    }

    @DataBoundConstructor
    public SeleniumAxis(String name, List<SeleniumCapability> seleniumCapabilities){
        super(name, SeleniumAxis.convertToAxisValue(seleniumCapabilities))
        this.seleniumCapabilities = seleniumCapabilities
    }

    public static String convertToAxisValue(List<SeleniumCapability> seleniumCapabilities){
        def ret = new StringBuilder()

        seleniumCapabilities.each(){ret.append(it.toString()).append(' ')}

        return ret.toString()
    }

    public List<SeleniumCapability> getSeleniumCapabilities(){
        return Collections.unmodifiableList(seleniumCapabilities)
    }

    public boolean isSystem() {
        return true;
    }

    public void addBuildVariable(String value, Map<String,String> map) {

       //so the value is PLATFORM-BROWSER-VERSION

       def parts = value.split(/-/)

       map.put(name + "_PLATFORM", parts[0]);
       map.put(name + "_BROWSER", parts[1]);
       map.put(name + "_VERSION", parts[2]);
    }

    //public Iterator<String> iterator() {
    //    return getValues().iterator();
    //}

    //public int size() {
    //    return getValues().size();
    //}

    //public String value(int index) {
    //    return getValues().get(index);
    //}

    public int compareTo(SeleniumAxis that) {
        return this.name.compareTo(that.name)
    }


    @Extension
    public static class DescriptorImpl extends AxisDescriptor{
        public String server

        public DescriptorImpl() {
            load()
        }

        public DescriptorExtensionList<SeleniumCapability,Descriptor<SeleniumCapability> > seleniumCapabilities() {
            //def xxx =  Jenkins.getInstance().<SeleniumCapability,Descriptor<SeleniumCapability>>getDescriptorList(SeleniumCapability.class);
            DescriptorExtensionList<SeleniumCapability,Descriptor<SeleniumCapability> >  xxx =  Jenkins.getInstance().<SeleniumCapability,Descriptor<SeleniumCapability>>getDescriptorList(SeleniumCapability.class);

            def jjj = SeleniumCapability.class

            return xxx
        }

        public List<SeleniumCapability> getSeleniumCapabilities() {
             def sel = new Selenium(server)

            return sel.seleniumCapabilities
        }


        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            req.bindJSON(this, formData)
            save()
            return true
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
            return '\"' + Functions.jsStringEscape(String.format(body, args)) + '\"';
        }

        public String buildLabelCheckBox(SeleniumCapability sc, LabelAxis instance) {
            return jsstr("<input type='checkbox' name='values' json='%s' /><label class='attach-previous'>%s</label>",
                    Functions.htmlAttributeEscape(sc.toString()),
                    sc.toString())
        }

        public FormValidation doCheckServer(@QueryParameter String value) {
            if (value.isEmpty()) {
                return FormValidation.error("You must provide an URL.")
            }

            try {
                new URL(value)
            } catch (final MalformedURLException e) {
                return FormValidation.error("This is not a valid URL.")
            }

            return FormValidation.ok()
        }

        public FormValidation doCheckValues(@QueryParameter String value) {
            if (value.isEmpty()) {
                return FormValidation.error("You must provide a configuration.")
            }
            return FormValidation.ok()
        }
    }

}