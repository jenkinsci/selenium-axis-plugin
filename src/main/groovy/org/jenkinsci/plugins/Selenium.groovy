package org.jenkinsci.plugins

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class Selenium {

    def seleniumCapabilities = new ArrayList<SeleniumCapability>()

    def seleniumVer
    def browsers = new ArrayList<String>()
    def platforms = new ArrayList<String>()
    def versions = new ArrayList<String>()

    Selenium( String url) {
        def document = Jsoup.connect("${url}/grid/console").get()

        def ver = document.select("h2").first()
        if (!ver)
            ver = document.select("h1").first()

        seleniumVer = ver.text()
        //throw new Exception("Not a Valid Selenium URL")

        //selenium with Jenkins 2.28?
        Elements capabilities = document.select("fieldset > img");
        capabilities.addAll(document.select("fieldset > a"));

        //selenium 2.33.0
        capabilities.addAll(document.select("p > img"));
        capabilities.addAll(document.select("p > a"));

        for (Element capability : capabilities) {
            SeleniumCapability n = new SeleniumCapability(capability.attr("title"))
            if (seleniumCapabilities.contains(n))
                seleniumCapabilities.get(seleniumCapabilities.indexOf(n)).incr()
            else
                seleniumCapabilities.add(n)
        }
        Collections.sort(seleniumCapabilities)

        seleniumCapabilities.each() { a_sel ->
            //seleniumCapabilityDescriptor.add(new SeleniumCapability.SeleniumCapabilityDescriptor(a_sel))

            if (!browsers.contains(a_sel.browserName))
                browsers.add(a_sel.browserName)
            if (!platforms.contains(a_sel.platformName))
                platforms.add(a_sel.platformName)
            if (!versions.contains(a_sel.browserVersion))
                versions.add(a_sel.browserVersion)
        }
        Collections.sort(browsers)
        Collections.sort(platforms)
        Collections.sort(versions)
    }

    //@Override
    void dump() {
        println seleniumVer
        println "Browser/platform"
        seleniumCapabilities.each { println it.toString2() }
        println "platform"
        platforms.each { println it }
        println "browsers"
        browsers.each { println it }
        println "versions"
        versions.each { println it }
    }

    String combinationFilter(){

        def ret = new StringBuilder()
        def join = ''

        seleniumCapabilities.each { ret.append( join + it.combinationFilter() ); join = ' || '}
        return ret.toString();
    }
}