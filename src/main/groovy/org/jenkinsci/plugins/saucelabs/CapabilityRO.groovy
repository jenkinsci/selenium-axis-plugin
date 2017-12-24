package org.jenkinsci.plugins.saucelabs

import hudson.init.InitMilestone
import hudson.init.Initializer
import hudson.model.Items
import org.jenkinsci.plugins.selenium.Manual

class CapabilityRO extends Manual {

    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    public static void addAliases() {
        Items.XSTREAM2.addCompatibilityAlias("org.jenkinsci.plugins.SauceLabsCapabilityRO", CapabilityRO);
    }

    CapabilityRO(String browserName, String platformName, String browserVersion, String capType) {
        super(browserName, platformName, browserVersion, 'SL')

        capType = 'SL'
    }
}
