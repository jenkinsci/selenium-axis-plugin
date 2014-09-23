package org.jenkinsci.plugins

import spock.lang.Specification

class LevenshtienSpec extends Specification {

    def 'Same'() {

        when:
        def diff = Levenshtien.distance('the quick brown fox jumps over the lazy dog',
                'the quick brown fox jumps over the lazy dog')

        then:
        assert diff == 0
    }

    def 'different'() {

        when:
        @SuppressWarnings('LineLength')
        def diff = Levenshtien.distance('The quick brown fox jumps over the lazy dog',
                'the quick brown fox jumps over the lazy dog')

        then:
        assert diff == 1
    }

}
