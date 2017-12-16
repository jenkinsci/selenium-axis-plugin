package org.jenkinsci.plugins.hub

import org.jenkinsci.complex.axes.ItemList
import org.jenkinsci.plugins.selenium.ICapabilityReader

class Selenium {

    ItemList<? extends Capability> seleniumCapabilities = new ItemList<? extends Capability>()
    ItemList<? extends Capability> seleniumLatest = new ItemList<? extends Capability>()
    ItemList<? extends Capability> seleniumSelected = new ItemList<? extends Capability>()

    String seleniumVer
    List<String> browsers = []
    List<String> platforms = []
    List<String> versions = []

    Selenium(ICapabilityReader reader, Class<? extends Capability> clazz  ) {

        Map<String, String> latestMap = [:]

        reader.capabilities.each {
            Capability n = clazz.newInstance(it.api_name, it.os, it.short_version ?: 'Any', 'SEL')
            if (seleniumCapabilities.contains(n)) {
                seleniumCapabilities.get(seleniumCapabilities.indexOf(n)).incr()
                if ( it.api_name == 'internet explorer' ) {
                    latestMap["${it.api_name}-${it.os}-${it.short_version}"] = n
                } else if ( it.short_version != 'beta'
                        && latestMap["${it.api_name}-${it.os}"].browserVersion < it.short_version ) {
                    latestMap["${it.api_name}-${it.os}"] = n

                }
            } else {
                seleniumCapabilities.add(n)
                if ( it.api_name == 'internet explorer' ) {
                    latestMap["${it.api_name}-${it.os}-${it.short_version}"] = n
                } else if ( it.short_version != 'beta' ) {
                    latestMap["${it.api_name}-${it.os}"] = n
                }
            }
        }
        seleniumLatest = new ItemList<? extends Capability>(latestMap.values())

        seleniumLatest.each {
            if (['internet explorer', 'chrome', 'safari', 'firefox', 'microsoftedge'].contains(it.browserName)) {
                seleniumSelected << it
            }
        }

        Collections.sort(seleniumCapabilities)

        seleniumCapabilities.each { aSel ->
            //seleniumCapabilityDescriptor.add(new SeleniumCapability.SeleniumCapabilityDescriptor(aSel))

            if (aSel.browserName && !browsers.contains(aSel.browserName)) {
                browsers << aSel.browserName
            }
            if (aSel.platformName && !platforms.contains(aSel.platformName)) {
                platforms << aSel.platformName
            }
            if (aSel.browserVersion && !versions.contains(aSel.browserVersion)) {
                versions << aSel.browserVersion
            }
        }
        Collections.sort(browsers)
        Collections.sort(platforms)
        Collections.sort(versions)
    }

    List<? extends Capability> getSeleniumCapability() {
        seleniumCapabilities
    }

    //SeleniumCapability random() {
    //    seleniumCapabilities.get(randomizer.nextInt(seleniumCapabilities.size()))
    //}
}