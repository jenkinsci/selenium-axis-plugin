package org.jenkinsci.plugins.hub

import org.jenkinsci.plugins.selenium.ICapabilityReader
import org.jenkinsci.plugins.selenium.Exception

/**
 * Created by jeremymarshall on 29/08/2014.
 */

class StringReader implements ICapabilityReader {
    Object capabilities = []

    @Override
    void loadCapabilities(String raw) throws Exception {

        if (raw == '') {
            throw new Exception('No Capabilities')
        }

        raw.splitEachLine  ('-') { item ->
            capabilities << [ os: item[1], long_name: item[2], short_version: item[3] ]
        }
    }

    @Override
    List<Map> getCapabilities() {
        capabilities
    }

}
