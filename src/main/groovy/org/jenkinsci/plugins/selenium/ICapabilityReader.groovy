package org.jenkinsci.plugins.selenium

import org.jenkinsci.plugins.selenium.Exception

/**
 * Created by jeremymarshall on 29/08/2014.
 */
interface ICapabilityReader {
    void loadCapabilities (String source) throws Exception
    List<Map> getCapabilities()
}
