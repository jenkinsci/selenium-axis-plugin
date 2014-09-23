package org.jenkinsci.plugins

import groovy.json.JsonSlurper

/**
 * Created by jeremymarshall on 29/08/2014.
 */

class SauceLabsCapabilityReader implements ISeleniumCapabilityReader {
    Object capabilities

    @Override
    void loadCapabilities(String url) throws SeleniumException {
        try {
            def payload = rawRead(url)
            def slurper = new JsonSlurper()
            capabilities = slurper.parseText(payload)
        } catch (IllegalArgumentException e) {
            throw new SeleniumException( e.message )
        }
    }

    @Override
    List<Map> getCapabilities() {
        capabilities
    }

    String rawRead( String url) {
        new URL(url).text
    }
}
