package org.jenkinsci.plugins

import spock.lang.Specification

class SauceLabsCapabilityROSpec extends Specification {

    def 'Create Param'() {

        def selCap = new SauceLabsCapabilityRO('Browser', 'Platform', 'Version', 'SEL')

        expect:
        selCap.browserName.matches('Browser')
        selCap.browserVersion.matches('Version')
        selCap.platformName.matches('Platform')
    }
}
