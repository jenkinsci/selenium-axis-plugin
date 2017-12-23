package org.jenkinsci.plugins.SeleniumAxis
/*
This Groovy script is used to produce the global configuration option.

Jenkins uses a set of tag libraries to provide uniformity in forms.
To determine where this tag is defined, first check the namespace URI,
and then look under $JENKINS/views/. For example, section() is defined
in $JENKINS/views/lib/form/section.jelly.

It's also often useful to just check other similar scripts to see what
tags they use. Views are always organized according to its owner class,
so it should be straightforward to find them.
*/
namespace(lib.FormTagLib).with {
    entry(title: _("Name"), field:"name") {
        textbox( default:"label")
    }

    optionalBlock(field:"slOverride", inline:true, title: 'Override SauceLabs User Details') {
        entry(title: 'Username', field: 'slName', description: 'Username in Saucelabs') {
            textbox()
        }
        entry(title: 'Password', field: 'slPassword', description: 'Password in Saucelabs') {
            password()
        }
    }


    block{
        entry(field:"seleniumCapabilities") {
            hetero_list( name: "seleniumCapabilities",
                      hasHeader: true,
                      descriptors:descriptor.axisItemTypes(),
                      items:      instance? instance.getSeleniumCapabilities():descriptor.loadDefaultItems())
        }
    }
}
