build:
	mvn clean install
    mvn cobertura:cobertura

travis-deploy:
	mvn versions:set -DnewVersion=${TRAVIS_TAG}
	mvn clean deploy -P release --settings .travis/settings.xml