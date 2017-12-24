package org.jenkinsci.plugins

import org.jenkinsci.plugins.hub.Selenium
import org.jenkinsci.plugins.selenium.Manual
import org.jenkinsci.plugins.hub.StringReader
import org.jenkinsci.plugins.selenium.Exception
import spock.lang.Specification

class NullSeleniumGrid extends Specification {

    def 'noServer'() {
        when:
        def reader = new StringReader()
        reader.loadCapabilities('')
        new Selenium(reader, Manual)
        then:
        thrown(Exception)
    }
}
