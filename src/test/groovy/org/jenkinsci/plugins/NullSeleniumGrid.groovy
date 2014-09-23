package org.jenkinsci.plugins

import spock.lang.Specification

class NullSeleniumGrid extends Specification {

    def 'noServer'() {
        when:
        def reader = new SeleniumStringReader()
        reader.loadCapabilities('')
        new Selenium(reader, SeleniumCapability)
        then:
        thrown(SeleniumException)
    }
}
