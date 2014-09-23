package org.jenkinsci.plugins

/**
 * Created by jeremymarshall on 29/08/2014.
 */

class SeleniumStringReader implements ISeleniumCapabilityReader {
    Object capabilities = []

    @Override
    void loadCapabilities(String raw) throws SeleniumException {

        if (raw == '') {
            throw new SeleniumException('No Capabilities')
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
