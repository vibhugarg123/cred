test:
	mvn test
skip-tests:
	mvn clean install -DskipTests=true
format:
	mvn spotless:apply
clean:
	mvn clean