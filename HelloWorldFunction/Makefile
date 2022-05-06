build:
ifeq ($(SCRATCH_BUILD), true)
	./mvnw clean
endif

ifeq ($(NATIVE_BUILD), true)
	./mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker
else
	./mvnw package
endif

build-HelloWorldFunction: build
	unzip target/function.zip -d $(ARTIFACTS_DIR)/
