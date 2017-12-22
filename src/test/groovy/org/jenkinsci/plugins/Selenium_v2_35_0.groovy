package org.jenkinsci.plugins

import org.jenkinsci.plugins.hub.Selenium
import org.jenkinsci.plugins.selenium.Capability
import org.jenkinsci.plugins.hub.CapabilityReader
import org.jsoup.Jsoup
import spock.lang.Specification

class Selenium_v2_35_0 extends Specification {

    def 'Tests'() {
        CapabilityReader.metaClass.rawRead = {
            String s -> Jsoup.parse(this.class.getResourceAsStream(s), 'UTF-8', '')
        }

        when:
        def reader = new CapabilityReader()
        reader.loadCapabilities('/grid-2.35.0.html')
        def sel = new Selenium(reader, Capability)

        then:
        assert sel.seleniumCapabilities.size() == 4
        assert sel.seleniumCapabilities.toString() ==
                'SEL-Any-phantomjs-Any\nSEL-MAC-chrome-Any\nSEL-MAC-firefox-14\nSEL-MAC-safari-Any\n'
    }
}
