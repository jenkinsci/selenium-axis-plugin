package org.jenkinsci.plugins

import org.junit.Rule
import spock.lang.Specification

class NullSeleniumGrid extends Specification{

    def 'noServer'(){
        when:
        def sel = new Selenium(Selenium.load("null://"), SeleniumCapability.class)
        then:
        thrown(SeleniumException)
    }
}