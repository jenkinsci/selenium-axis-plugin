package org.jenkinsci.plugins

import hudson.Extension
import org.kohsuke.stapler.DataBoundConstructor
import hudson.model.Descriptor

import java.util.regex.Matcher

class SeleniumCapability extends  ComplexAxisItem implements Comparable {

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

    public String combinationFilter() {
        String.format("(TEST_PLATFORM=='%s' && TEST_BROWSER=='%s' && TEST_VERSION=='%s')", platformName, browserName, browserVersion)
    }

    @Override
    String toString() {
        String.format("%s-%s-%s", platformName, browserName, browserVersion)
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

    @Extension public static class DescriptorImpl extends ComplexAxisItemDescriptor {
        private static Descriptor<? extends ComplexAxisDescriptor> topLevelDescriptor;

        protected static Descriptor<? extends ComplexAxisDescriptor> getTopLevelDescriptor(){
            return topLevelDescriptor;
        }

        protected static void setTopLevelDescriptor( Descriptor<? extends ComplexAxisDescriptor> topLevelDescriptor){
            ComplexAxisItemDescriptor.topLevelDescriptor = topLevelDescriptor;
        }

        @Override public String getDisplayName() {
            return "Selenium Capability";
        }
    }
}
