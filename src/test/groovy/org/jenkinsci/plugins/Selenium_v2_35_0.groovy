package org.jenkinsci.plugins

import org.junit.Rule
import spock.lang.Specification

class Selenium_v2_35_0 extends Specification{
    def sel = new Selenium(Selenium.load("/grid-2.35.0.html"), SeleniumCapability.class)

    def 'count'() {
        expect: sel.seleniumCapabilities.size() == 4
    }

    def 'version'(){
        expect: sel.getSeleniumVer().matches('Grid Console v.2.35.0')
    }

    def 'items'(){
        expect: sel.getSeleniumCapabilities().toString() == '[Any-phantomjs-Any, MAC-chrome-Any, MAC-firefox-14, MAC-safari-Any]'
    }
}