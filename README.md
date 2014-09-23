selenium-axis-plugin

This plugin creates an axis based on the Selenium grid capabilities.
It will also build against the SauceLabs Selenium capability at the same time.

So you can have an axis with both capabilities

It does this by creating four environment variables to pass to the build phase. 

<ul>
<li> LABEL_URL for either the local Selenium or SauceLabs</li>
<li> LABEL_BROWSER for the browser (e.g. chrome, firefox)</li>
<li> LABEL_PLATFORM for the platform (e.g. LINUX, VISTA)</li>
<li> LABEL_VERSION for the browser version
</ul>

You can use any label you want for the axis, so long as all the axes are unique

This is an example test in Perl which can be run as a build step

```perl

use Test::More tests=>8;
use_ok 'Selenium::Remote::Driver';

ok $ENV{TEST_BROWSER}, "Browser name set";
ok $ENV{TEST_PLATFORM}, "Platform name set";
ok $ENV{TEST_VERSION}, "Version name set";
ok $ENV{TEST_URL}, "URL set";

my $browser  = $ENV{TEST_BROWSER};
my $platform = $ENV{TEST_PLATFORM};
my $version = $ENV{TEST_VERSION};
my $url = $ENV{TEST_URL};

$url =~ s|^http://||;

my $parms = [remote_server_addr=>$url ];

push @$parms, (platform => $platform) unless $platform eq 'Any';
push @$parms, (browser_name => $browser) unless $browser eq 'Any';
push @$parms, (version => $version) unless $version eq 'Any';

push @$parms, (extra_capabilities => {name => $ENV{BUILD_TAG}||$0 });

use Data::Dumper;
print Dumper ($parms);

my $driver = new_ok('Selenium::Remote::Driver' => $parms);

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

