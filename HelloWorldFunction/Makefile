clean:
	./mvnw clean
build:
	./mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker

build-HelloWorldFunction: clean build
	unzip target/function.zip -d $(ARTIFACTS_DIR)/
