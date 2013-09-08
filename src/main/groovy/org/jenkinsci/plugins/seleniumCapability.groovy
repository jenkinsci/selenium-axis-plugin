package org.jenkinsci.plugins

import java.util.regex.Matcher

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import hudson.model.Descriptor


class SeleniumCapability implements Comparable {

    private Integer maxInstances
    private String browserName
    private String platformName
    private String browserVersion

    SeleniumCapability() {
        browserName = "Any"
        platformName = "Any"
        browserVersion = "Any"
        maxInstances = 1
    }


    SeleniumCapability(String titleAttr) {

        this()

        Matcher m = (titleAttr =~ /(platform|browserName|version)=(\w+)/)

        while (m.find()) {
            if (m.group(1).equals("platform"))
                this.platformName = m.group(2)
            else if (m.group(1).equals("browserName"))
                this.browserName = m.group(2)
            else if (m.group(1).equals("version"))
                this.browserVersion = m.group(2)

        }
    }

    def incr = {->
        maxInstances++
    }

    def toString2 = {->
        String.format("%s %s %s %s", platformName, browserName, browserVersion, maxInstances)
    }

    def combinationFilter = {->
        String.format("(TEST_PLATFORM=='%s' && TEST_BROWSER=='%s' && TEST_VERSION=='%s')", platformName, browserName, browserVersion)
    }

    @Override
    String toString() {
        String.format("%s-%s-%s", platformName, browserName, browserVersion)
    }

    @Override
    boolean equals(Object o) {
        this.toString().equals(o.toString())
    }

    @Override
    int compareTo(Object o) {
        this.toString().compareTo o.toString()
    }


    //public static class SeleniumCapabilityDescriptor extends Descriptor<SeleniumCapability> {
    //    private SeleniumCapability sel
    //    SeleniumCapabilityDescriptor(SeleniumCapability sel){
    //        this.sel = sel
    //    }
    //
    //    public String getDisplayName(){ r
    //         return sel.toString()
    //    }
    //
    //}

    public Descriptor<SeleniumCapability> getDescriptor() {
        return Hudson.getInstance().getDescriptorByType(SeleniumCapability.class);
    }

    public String getDisplayName(){
        return toString()
    }
}
