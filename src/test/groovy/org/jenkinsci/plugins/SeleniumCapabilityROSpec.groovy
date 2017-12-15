package org.jenkinsci.plugins

import org.jenkinsci.plugins.hub.CapabilityRO
import spock.lang.Specification

class SeleniumCapabilityROSpec extends Specification {

    def 'Create Param'() {

        def selCap = new CapabilityRO('Browser', 'Platform', 'Version', 'SEL')

        expect:
        selCap.browserName.matches('Browser')
        selCap.browserVersion.matches('Version')
        selCap.platformName.matches('Platform')
    }
}
