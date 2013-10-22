selenium-axis-plugin

This plugin creates an axis based on the Selenium grid capabilities.

You can then develop one job and run it against any or all of the Selenium capabilities it finds

It does this by creating three environment variables to pass to the build phase. 

<ul>
<li> LABEL_BROWSER for the browser (e.g. chrome, firefox)</li>
<li> LABEL_PLATFORM for the platform (e.g. LINUX, VISTA)</li>
<li> LABEL_VERSION for the browser version
</ul>

You can use any label you want for the axis, so long as all the axes are unique

This is an example test in Perl which can be run as a build step

```perl

use Test::More tests=>6;
use_ok 'Selenium::Remote::Driver';

ok $ENV{TEST_BROWSER};
ok $ENV{TEST_VERSION};
ok $ENV{TEST_PLATFORM};

my $driver = new_ok('Selenium::Remote::Driver' => [ 
  browser_name=>$ENV{TEST_BROWSER}, 
  version=>$ENV{TEST_VERSION}, 
  platformbrowser=>$ENV{TEST_PLATFORM},  
  
  proxy=>{proxyType=> 'direct'}]);
  
ok $driver->get('http://www.google.com');
is $driver->get_title(), 'Google';
$driver->quit();

```

TODO

<ul>
<li><del>It is not happy if you don't select a configuration.</del></li>


<li><del>Probably should use an Any-Any-Any configuration</del></li>


<li><del>Create a second axis to use whatever Selenium capabilities are available when a build starts</del></li>


<li><del>Convert the config.jelly to config.groovy</del></li>


<li><del>Write some tests</del></li>


<li><del>Submit it to Jenkins</del></li>

</ul>

