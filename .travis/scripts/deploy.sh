#!/bin/bash
mvn versions:set -DnewVersion=${TRAVIS_TAG}
mvn clean deploy -P release --settings .travis/maven/settings.xml