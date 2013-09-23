package org.jenkinsci.plugins.SeleniumDynamicCapability
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
def f = namespace(lib.FormTagLib)
/*
block(){
    entry(title:_("Selenium Dynamic Capabilities")) {
        hetero_list( name:       "seleniumCapabilities",
            hasHeader: false,
            addCaption: false,
            deleteCaption: false,
            descriptors:descriptor.complexAxisItemTypes(),
            items:      instance?instance.getComplexAxisItems():descriptor.loadDefaultItems())
    }
}
*/

f.entry(title: "Value", field: "value") {
    f.textbox()

    f.nested {
        table {
            f.entry(title: "Capabilities") {
                f.repeatableProperty(field: "seleniumCapabilities")
            }
        }
    }
}
