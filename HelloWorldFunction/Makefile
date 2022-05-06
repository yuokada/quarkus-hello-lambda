clean:
	./mvnw clean

build:
ifeq ($(NATIVE_BUILD), true)
	./mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker
else
	./mvnw package
endif

build-HelloWorldFunction: clean build
	unzip target/function.zip -d $(ARTIFACTS_DIR)/
