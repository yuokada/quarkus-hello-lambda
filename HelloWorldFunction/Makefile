clean:
	./mvnw clean
build:
	./mvnw package

build-HelloWorldFunction: clean build
	unzip target/function.zip -d $(ARTIFACTS_DIR)/
