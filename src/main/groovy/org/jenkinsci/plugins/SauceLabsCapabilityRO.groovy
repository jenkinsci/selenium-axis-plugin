package org.jenkinsci.plugins

class SauceLabsCapabilityRO extends SeleniumCapability {

    SauceLabsCapabilityRO(String browserName, String platformName, String browserVersion, String capType) {
        super(browserName, platformName, browserVersion, 'SL')

        capType = 'SL'
    }
}
