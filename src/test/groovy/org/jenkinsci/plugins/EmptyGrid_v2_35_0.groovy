package org.jenkinsci.plugins

import org.junit.Rule
import spock.lang.Specification

class EmptyGrid_v2_35_0 extends Specification{
    def sel = new Selenium(Selenium.load("/empty-grid-2.35.0.html"), SeleniumCapability.class)

    def 'count'() {
        expect: sel.seleniumCapabilities.size() == 0
    }

    def 'version'(){
        expect: sel.getSeleniumVer().matches('Grid Console v.2.35.0')
    }

    def 'items'(){
        expect: sel.getSeleniumCapabilities().toString() == '[]'
    }
}