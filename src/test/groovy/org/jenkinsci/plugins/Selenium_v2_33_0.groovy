package org.jenkinsci.plugins

import org.junit.Rule
import spock.lang.Specification

class Selenium_v2_33_0 extends Specification{


    def 'tests'() {
        when:
        def sel = new Selenium(Selenium.load("/grid-2.33.0.html"), SeleniumCapability.class)

        then:
        assert sel.seleniumCapabilities.size() == 4
        assert sel.getSeleniumVer().matches('Grid Console v.2.33.0')
        assert sel.getSeleniumCapabilities().toString() == '[Any-phantomjs-Any, LINUX-chrome-25, LINUX-firefox-Any, LINUX-opera-12]'
    }
}
