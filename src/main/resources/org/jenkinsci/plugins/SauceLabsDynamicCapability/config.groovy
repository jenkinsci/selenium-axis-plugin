package org.jenkinsci.plugins.SauceLabsDynamicCapability.Container
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

    entry(title: 'Axis Points to Create', field: 'number', description: 'Number of random combinations required') {
        number(default: 3)
    }

    optionalBlock(field:"advanced", inline:true, title: 'Advanced') {
        entry(title: 'Selection Criteria', field: 'criteria', description: 'Criteria for selecting capabilities') {
            select( default: 'latest')
        }
        entry(title: 'Filter', field: 'filter', description: 'Restrict capabilities with a groovy expression') {
            expandableTextbox()
        }
        validateButton( title: 'Test', method: 'rebuild', with:'number,criteria,filter')
    }

    //entry(title: 'Capabilities', field: 'sauceLabsCapabilities', description: 'These values will be re-evaluated during a build') {
    //    textarea( style: 'width:100%', height: '20em', readonly: true, default:descriptor.loadDefaultItems())
    //}
}