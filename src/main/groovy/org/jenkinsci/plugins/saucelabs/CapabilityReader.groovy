package org.jenkinsci.plugins.saucelabs

import groovy.json.JsonSlurper
import org.jenkinsci.plugins.selenium.Exception
import org.jenkinsci.plugins.selenium.ICapabilityReader

/**
 * Created by jeremymarshall on 29/08/2014.
 */

class CapabilityReader implements ICapabilityReader {
    Object capabilities

    @Override
    void loadCapabilities(String url) throws Exception {
        try {
            String payload = rawRead(url)
            JsonSlurper slurper = new JsonSlurper()
            capabilities = slurper.parseText(payload)
        } catch (IllegalArgumentException e) {
            throw new Exception( e.message )
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
