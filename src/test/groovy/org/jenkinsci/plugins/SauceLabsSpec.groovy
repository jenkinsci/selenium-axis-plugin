package org.jenkinsci.plugins

import spock.lang.Specification

class SauceLabsSpec extends Specification {

    def 'Available'() {
        SauceLabsCapabilityReader.metaClass.rawRead = { String s -> this.class.getResource(s).text }

        when:
        def reader = new SauceLabsCapabilityReader()
        reader.loadCapabilities('/saucelabs_full.json')
        def sel = new Selenium(reader, SeleniumCapability)

        then:
        assert sel.seleniumCapabilities.size() == 330
        assert sel.seleniumLatest.size() == 39
        assert sel.seleniumSelected.size() == 28
    }

    def 'No Connection'() {
        SauceLabsCapabilityReader.metaClass.rawRead = { String s -> this.class.getResource(s).text }

        when:
        def reader = new SauceLabsCapabilityReader()
        reader.loadCapabilities('/saucelabs_noaccess.json')
        new Selenium(reader, SeleniumCapability)

        then:
        thrown(SeleniumException)
    }

}
