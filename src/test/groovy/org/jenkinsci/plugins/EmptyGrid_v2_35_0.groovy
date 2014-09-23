package org.jenkinsci.plugins

import org.jsoup.Jsoup
import spock.lang.Specification

class EmptyGrid_v2_35_0 extends Specification {

    def 'Tests'() {
        SeleniumHubCapabilityReader.metaClass.rawRead = {
            String s -> Jsoup.parse(this.class.getResourceAsStream(s), 'UTF-8', '')
        }

        when:
        def reader = new SeleniumHubCapabilityReader()
        reader.loadCapabilities('/empty-grid-2.35.0.html')
        def sel = new Selenium(reader, SeleniumCapability)

        then:
        assert  sel.seleniumCapabilities.size() == 0
        assert sel.seleniumCapabilities.toString() == ''
    }
}
