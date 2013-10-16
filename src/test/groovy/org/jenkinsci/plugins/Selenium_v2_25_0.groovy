package org.jenkinsci.plugins

import spock.lang.*
import org.junit.Rule

class Selenium_v2_25_0 extends Specification{
    def sel = new Selenium(Selenium.loadStream(this.class.getResourceAsStream("/grid-2.25.0.html")), SeleniumCapabilityRO.class)

    def 'count'() {
        expect: sel.seleniumCapabilities.size() == 5
    }

    def 'version'(){
        expect: sel.getSeleniumVer().matches('Grid Hub 2.25.0')
    }

    def 'items'(){
        expect: sel.getSeleniumCapabilities().toString() == '[Any-phantomjs-Any, MAC-chrome-Any, MAC-firefox-14, MAC-opera-Any, MAC-safari-Any]'
    }
}