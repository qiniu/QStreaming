build:
	mvn clean install -Pdocker
	cd e2e/k2k && ./test.sh

deploy:
	mvn versions:set -DnewVersion=${TRAVIS_BRANCH}
	mvn clean deploy -P release --settings .travis/maven/settings.xml