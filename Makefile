clean:
	rm -rf ./.aws-sam
	cd HelloWorldFunction && ./mvnw clean

build:
	sam build --cached --parallel

scratch_build:clean build

package:
	sam package --config-file samconfig.toml --output-template-file deploy-template.yaml

deploy: package
	sam deploy --profile serverless --template-file deploy-template.yaml --config-file samconfig.toml

delete:
	sam delete --config-file samconfig.toml

test:
	cd HelloWorldFunction && ./mvnw test

validate:
	sam validate --lint

invoke:
	sam local invoke HelloWorldFunction --event events/event.json