build:
	mvn clean install -Pdocker
	cd e2e/k2k && ./test.sh

deploy:
	mvn clean deploy -P release --settings .travis/maven/settings.xml