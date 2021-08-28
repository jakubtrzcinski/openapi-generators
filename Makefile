build:
	gradle build
	mv build/libs/openapi-generators-1.0-SNAPSHOT.jar oasgen-bin.jar


e2e:




test-kotlin:
	cp oasgen-bin.jar e2e/kotlin/oasgen-bin.jar
	(cd e2e/kotlin; java -jar oasgen-bin.jar init)
	(cd e2e/kotlin; java -jar oasgen-bin.jar gen https://petstore.swagger.io/v2/swagger.json src/main/kotlin/io/trzcinski/test)
	(cd e2e/kotlin; ./gradlew build)

test-java:
	cp oasgen-bin.jar e2e/java/oasgen-bin.jar
	(cd e2e/java; java -jar oasgen-bin.jar init)
	(cd e2e/java; java -jar oasgen-bin.jar gen https://petstore.swagger.io/v2/swagger.json src/main/java/io/trzcinski/test)
	(cd e2e/java; ./gradlew build)


test-dart:
	cp oasgen-bin.jar e2e/dart/oasgen-bin.jar
	(cd e2e/dart; java -jar oasgen-bin.jar init)
	(cd e2e/dart; java -jar oasgen-bin.jar gen https://petstore.swagger.io/v2/swagger.json lib/api)
	(cd e2e/dart; flutter build web)
