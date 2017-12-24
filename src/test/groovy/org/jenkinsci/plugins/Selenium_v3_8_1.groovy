package org.jenkinsci.plugins

import org.jenkinsci.plugins.hub.Selenium
import org.jenkinsci.plugins.selenium.Manual
import org.jenkinsci.plugins.hub.CapabilityReader
import org.jsoup.Jsoup
import spock.lang.Specification

class Selenium_v3_8_1 extends Specification {

    def 'Tests'() {
        CapabilityReader.metaClass.rawRead = {
            String s -> Jsoup.parse(this.class.getResourceAsStream(s), 'UTF-8', '')
        }

        when:
        def reader = new CapabilityReader()
        reader.loadCapabilities('/grid-3.8.1.html')
        def sel = new Selenium(reader, Manual)

        then:
        assert sel.seleniumCapabilities.size() == 2
        assert sel.seleniumCapabilities.toString() == 'SEL-LINUX-chrome-62\nSEL-LINUX-firefox-57\n'
    }
}
