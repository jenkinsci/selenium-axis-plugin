package org.jenkinsci.plugins

import spock.lang.*
import org.junit.Rule

class Selenium_v2_25_0 extends Specification{

    def 'Tests'() {
        when:
        def sel = new Selenium(Selenium.load("/grid-2.25.0.html"), SeleniumCapabilityRO.class)

        then:
        assert sel.seleniumCapabilities.size() == 5
        assert sel.getSeleniumVer().matches('Grid Hub 2.25.0')
        assert sel.getSeleniumCapabilities().toString() == '[Any-phantomjs-Any, MAC-chrome-Any, MAC-firefox-14, MAC-opera-Any, MAC-safari-Any]'
    }
}
