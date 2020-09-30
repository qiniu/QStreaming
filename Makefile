build:
	mvn clean install
    mvn cobertura:cobertura

travis-deploy:
	gpg --import .travis/private-signing-key.gpg
	mvn versions:set -DnewVersion=${TRAVIS_TAG}
	mvn clean deploy -P release --settings .travis/settings.xml