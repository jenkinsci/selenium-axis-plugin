package org.jenkinsci.plugins.selenium

import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript

/**
 * Created by jeremymarshall on 17/12/17.
 */
interface ICapability {
    List<? extends Manual> getCapabilities(String which)
    List<? extends Manual> getRandomCapabilities(String which, Integer count, SecureGroovyScript secureFilter)
}