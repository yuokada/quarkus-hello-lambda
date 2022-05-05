clean:
	rm -rf ./.aws-sam
	cd HelloWorldFunction && ./mvnw clean

build:
	sam build

scratch_build:clean build

package:
	sam package --config-file samconfig.toml --output-template-file deploy-template.yaml

deploy: package
	sam deploy --profile serverless --template-file deploy-template.yaml --config-file samconfig.toml
