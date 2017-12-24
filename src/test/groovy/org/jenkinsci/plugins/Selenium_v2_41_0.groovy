package org.jenkinsci.plugins

import org.jenkinsci.plugins.hub.Selenium
import org.jenkinsci.plugins.selenium.Manual
import org.jenkinsci.plugins.hub.CapabilityReader
import org.jsoup.Jsoup
import spock.lang.Specification

class Selenium_v2_41_0 extends Specification {

    def setup() {
        CapabilityReader.metaClass.rawRead = {
            String s -> Jsoup.parse(this.class.getResourceAsStream(s), 'UTF-8', '')
        }
    }

    def 'Tests'() {
        when:
        def reader = new CapabilityReader()
        reader.loadCapabilities('/grid-2.41.0.html')
        def sel = new Selenium(reader, Manual)

        then:
        assert sel.seleniumCapabilities.size() == 1
        assert sel.seleniumCapabilities.toString() == 'SEL-MAC-chrome-Any\n'
    }
}
