package org.jenkinsci.plugins

import spock.lang.Specification

class SeleniumCapabilityROSpec extends Specification {

    def 'Create Param'() {

        def selCap = new SeleniumCapabilityRO('Browser', 'Platform', 'Version', 'SEL')

        expect:
        selCap.browserName.matches('Browser')
        selCap.browserVersion.matches('Version')
        selCap.platformName.matches('Platform')
    }
}
