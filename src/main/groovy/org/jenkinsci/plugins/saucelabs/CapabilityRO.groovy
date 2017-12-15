package org.jenkinsci.plugins.saucelabs

import org.jenkinsci.plugins.hub.Capability

class CapabilityRO extends Capability {

    CapabilityRO(String browserName, String platformName, String browserVersion, String capType) {
        super(browserName, platformName, browserVersion, 'SL')

        capType = 'SL'
    }
}
