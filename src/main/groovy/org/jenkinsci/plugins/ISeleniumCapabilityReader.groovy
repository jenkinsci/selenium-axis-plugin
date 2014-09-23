package org.jenkinsci.plugins

/**
 * Created by jeremymarshall on 29/08/2014.
 */
interface ISeleniumCapabilityReader {
    void loadCapabilities (String source) throws SeleniumException
    List<Map> getCapabilities()
}
