package org.jenkinsci.plugins

import hudson.Extension
import hudson.model.AbstractDescribableImpl
import hudson.model.Descriptor
import org.kohsuke.stapler.DataBoundConstructor

import java.util.regex.Matcher

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class SeleniumCapability extends  AbstractDescribableImpl<SeleniumCapability> implements Comparable {

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

    @DataBoundConstructor
    SeleniumCapability(String browserName, String platformName, String browserVersion) {
        this.browserName = browserName
        this.platformName = platformName
        this.browserVersion = browserVersion
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

    public Integer incr() {
        maxInstances++
    }

    public String toString2() {
        String.format("%s %s %s %s", platformName, browserName, browserVersion, maxInstances)
    }

    public String combinationFilter() {
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

    int compareTo(Object o) {
        this.toString().compareTo o.toString()
    }

    public String getDisplayName() {
        return toString()
    }

    @Extension public static class DescriptorImpl extends Descriptor<SeleniumCapability> {
        @Override public String getDisplayName() {
            return "Selenium Capability";
        }
    }

    public String getBrowserName(){
        this.browserName
    }

    public String getPlatformName(){
        this.platformName
    }

    public String getBrowserVersion(){
        this.browserVersion
    }

}
