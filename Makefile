build:
	mvn clean install -Pdocker
	cd stream-it/k2k && ./test.sh

deploy:
	mvn clean deploy -P release --settings .travis/maven/settings.xml