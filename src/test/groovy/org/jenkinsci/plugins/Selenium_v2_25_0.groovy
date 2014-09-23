package org.jenkinsci.plugins

import org.jsoup.Jsoup
import spock.lang.Specification

class Selenium_v2_25_0 extends Specification {

    def 'Tests'() {

        SeleniumHubCapabilityReader.metaClass.rawRead = {
            String s -> Jsoup.parse(this.class.getResourceAsStream(s), 'UTF-8', '')
        }

        when:
        def reader = new SeleniumHubCapabilityReader()
        reader.loadCapabilities('/grid-2.25.0.html')
        def sel = new Selenium(reader, SeleniumCapabilityRO)

        then:
        assert sel.seleniumCapabilities.size() == 5
        //assert sel.seleniumVer.matches('Grid Hub 2.25.0')
        assert sel.seleniumCapabilities.toString() ==
                'SEL-Any-phantomjs-Any\nSEL-MAC-chrome-Any\nSEL-MAC-firefox-14\nSEL-MAC-opera-Any\nSEL-MAC-safari-Any\n'
    }
}
