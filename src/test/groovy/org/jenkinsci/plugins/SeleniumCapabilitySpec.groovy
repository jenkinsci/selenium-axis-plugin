package org.jenkinsci.plugins

import org.junit.Rule
import spock.lang.Specification

class SeleniumCapabilitySpec extends Specification{

    def 'Create Param'() {

        def selCap = new SeleniumCapability('Browser', 'Platform', 'Version')

        expect:
        selCap.getBrowserName().matches('Browser')
        selCap.getBrowserVersion().matches('Version')
        selCap.getPlatformName().matches('Platform')
    }

    def 'Create No Param'() {

        def selCap = new SeleniumCapability()

        expect:
        selCap.getBrowserName().matches('Any')
        selCap.getBrowserVersion().matches('Any')
        selCap.getPlatformName().matches('Any')
    }
}
