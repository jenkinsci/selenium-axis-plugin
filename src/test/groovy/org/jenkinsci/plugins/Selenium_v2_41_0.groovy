package org.jenkinsci.plugins

import org.jsoup.Jsoup
import spock.lang.Specification

class Selenium_v2_41_0 extends Specification {

    def 'Tests'() {
        SeleniumHubCapabilityReader.metaClass.rawRead = {
            String s -> Jsoup.parse(this.class.getResourceAsStream(s), 'UTF-8', '')
        }

        when:
        def reader = new SeleniumHubCapabilityReader()
        reader.loadCapabilities('/grid-2.41.0.html')
        def sel = new Selenium(reader, SeleniumCapability)

        then:
        assert sel.seleniumCapabilities.size() == 1
        assert sel.seleniumCapabilities.toString() == 'SEL-MAC-chrome-Any\n'
    }
}
