package org.jenkinsci.plugins

import org.junit.Rule
import spock.lang.Specification

class Selenium_v2_35_0 extends Specification{

    def 'Tests'() {
        when:
        def sel = new Selenium(Selenium.load("/grid-2.35.0.html"), SeleniumCapability.class)

        then:
        assert sel.seleniumCapabilities.size() == 4
        assert sel.getSeleniumVer().matches('Grid Console v.2.35.0')
        assert sel.getSeleniumCapabilities().toString() == '[Any-phantomjs-Any, MAC-chrome-Any, MAC-firefox-14, MAC-safari-Any]'
    }
}
