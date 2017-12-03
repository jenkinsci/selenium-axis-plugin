package org.jenkinsci.plugins

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.util.regex.Matcher

/**
 * Created by jeremymarshall on 29/08/2014.
 */
class SeleniumHubCapabilityReader implements ISeleniumCapabilityReader {
    List<Map> capabilities = []
    @Override
    void loadCapabilities(String source) throws SeleniumException {
        try {
            Document document = rawRead(source)

            //selenium with Jenkins 2.25
            Elements cap = document.select('fieldset > img')
            cap.addAll(document.select('fieldset > a'))

            //selenium 2.33.0, 2.35.0
            cap.addAll(document.select('p > img'))
            cap.addAll(document.select('p > a'))

            for (Element capability : cap) {
                capabilities << convert(capability.attr('title'))
            }
        } catch (ex) {
            throw new SeleniumException("Didn't find a server at ${source}")
        }
    }

    @Override
    List<Map> getCapabilities() {
        capabilities
    }

    org.jsoup.nodes.Document rawRead(String source) {
        Jsoup.connect("${source}/grid/console").get()
    }

    private Map convert(String titleAttr) {

        Map ret = [:]
        Matcher m = (titleAttr =~ /(platform|browserName|version)=(\w+)/)

        while (m.find()) {
            if (m.group(1) == ("platform")) {
                ret << ['os': m.group(2)]
            } else if (m.group(1) == ("browserName")) {
                ret << ['api_name': m.group(2)]
            } else if (m.group(1) == ('version')) {
                ret << ['short_version': m.group(2)]
            }
        }
        ret
    }
}
