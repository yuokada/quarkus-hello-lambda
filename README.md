# sam-hello-lambda

This project contains source code and supporting files for a serverless application that you can deploy with the SAM CLI. It includes the following files and folders.

- HelloWorldFunction/src/main - Code for the application's Lambda function.
- events - Invocation events that you can use to invoke the function.
- HelloWorldFunction/src/test - Unit tests for the application code. 
- template.yaml - A template that defines the application's AWS resources.

The application uses several AWS resources, including Lambda functions and an API Gateway API. These resources are defined in the `template.yaml` file in this project. You can update the template to add AWS resources through the same deployment process that updates your application code.

If you prefer to use an integrated development environment (IDE) to build and test your application, you can use the AWS Toolkit.  
The AWS Toolkit is an open source plug-in for popular IDEs that uses the SAM CLI to build and deploy serverless applications on AWS. The AWS Toolkit also adds a simplified step-through debugging experience for Lambda function code. See the following links to get started.

* [CLion](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [GoLand](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [IntelliJ](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [WebStorm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [Rider](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [PhpStorm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [PyCharm](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [RubyMine](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [DataGrip](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)
* [VS Code](https://docs.aws.amazon.com/toolkit-for-vscode/latest/userguide/welcome.html)
* [Visual Studio](https://docs.aws.amazon.com/toolkit-for-visual-studio/latest/user-guide/welcome.html)

## Deploy the sample application

The Serverless Application Model Command Line Interface (SAM CLI) is an extension of the AWS CLI that adds functionality for building and testing Lambda applications. It uses Docker to run your functions in an Amazon Linux environment that matches Lambda. It can also emulate your application's build environment and API.

To use the SAM CLI, you need the following tools.

* SAM CLI - [Install the SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
* Java 17 (JDK 17) - [Install the Java 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
* Maven 3.9+ - [Install Maven](https://maven.apache.org/install.html)
* Docker - [Install Docker community edition](https://hub.docker.com/search/?type=edition&offering=community)

To build and deploy your application for the first time, run the following in your shell:

```bash
sam build
sam deploy --guided
```

The first command will build the source of your application. The second command will package and deploy your application to AWS, with a series of prompts:

* **Stack Name**: The name of the stack to deploy to CloudFormation. This should be unique to your account and region, and a good starting point would be something matching your project name.
* **AWS Region**: The AWS region you want to deploy your app to.
* **Confirm changes before deploy**: If set to yes, any change sets will be shown to you before execution for manual review. If set to no, the AWS SAM CLI will automatically deploy application changes.
* **Allow SAM CLI IAM role creation**: Many AWS SAM templates, including this example, create AWS IAM roles required for the AWS Lambda function(s) included to access AWS services. By default, these are scoped down to minimum required permissions. To deploy an AWS CloudFormation stack which creates or modifies IAM roles, the `CAPABILITY_IAM` value for `capabilities` must be provided. If permission isn't provided through this prompt, to deploy this example you must explicitly pass `--capabilities CAPABILITY_IAM` to the `sam deploy` command.
* **Save arguments to samconfig.toml**: If set to yes, your choices will be saved to a configuration file inside the project, so that in the future you can just re-run `sam deploy` without parameters to deploy changes to your application.

You can find your API Gateway Endpoint URL in the output values displayed after deployment.

## Use the SAM CLI to build and test locally

Build your application with the `sam build` command.

```bash
sam-hello-lambda$ sam build
```

The SAM CLI installs dependencies defined in `HelloWorldFunction/pom.xml`, creates a deployment package, and saves it in the `.aws-sam/build` folder.

Test a single function by invoking it directly with a test event. An event is a JSON document that represents the input that the function receives from the event source. Test events are included in the `events` folder in this project.

Run functions locally and invoke them with the `sam local invoke` command.

```bash
sam-hello-lambda$ sam local invoke HelloWorldFunction --event events/event.json
```

The SAM CLI can also emulate your application's API. Use the `sam local start-api` to run the API locally on port 3000.

```bash
sam-hello-lambda$ sam local start-api
sam-hello-lambda$ curl http://localhost:3000/
```

The SAM CLI reads the application template to determine the API's routes and the functions that they invoke. The `Events` property on each function's definition includes the route and method for each path.

```yaml
      Events:
        HelloWorld:
          Type: Api
          Properties:
            Path: /hello
            Method: get
```

## Add a resource to your application
The application template uses AWS Serverless Application Model (AWS SAM) to define application resources. AWS SAM is an extension of AWS CloudFormation with a simpler syntax for configuring common serverless application resources such as functions, triggers, and APIs. For resources not included in [the SAM specification](https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md), you can use standard [AWS CloudFormation](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html) resource types.

## Fetch, tail, and filter Lambda function logs

To simplify troubleshooting, SAM CLI has a command called `sam logs`. `sam logs` lets you fetch logs generated by your deployed Lambda function from the command line. In addition to printing the logs on the terminal, this command has several nifty features to help you quickly find the bug.

`NOTE`: This command works for all AWS Lambda functions; not just the ones you deploy using SAM.

```bash
sam-hello-lambda$ sam logs -n HelloWorldFunction --stack-name sam-hello-lambda --tail
```

You can find more information and examples about filtering Lambda function logs in the [SAM CLI Documentation](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-logging.html).

## Unit tests

Tests are defined in the `HelloWorldFunction/src/test` folder in this project.

```bash
sam-hello-lambda$ cd HelloWorldFunction
HelloWorldFunction$ mvn test
```

## Cleanup

To delete the sample application that you created, use the AWS CLI. Assuming you used your project name for the stack name, you can run the following:

```bash
aws cloudformation delete-stack --stack-name sam-hello-lambda
```

## Resources

See the [AWS SAM developer guide](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html) for an introduction to SAM specification, the SAM CLI, and serverless application concepts.

Next, you can use AWS Serverless Application Repository to deploy ready to use Apps that go beyond hello world samples and learn how authors developed their applications: [AWS Serverless Application Repository main page](https://aws.amazon.com/serverless/serverlessrepo/)

## 開発クイックスタート

- 前提ツール
  - Java 17 (JDK 17)
  - Maven 3.9+
  - Docker
  - AWS SAM CLI

- 基本コマンド
  - ビルド: `make build` または `sam build`
  - ローカル実行（イベントファイル）: `sam local invoke HelloWorldFunction --event events/event.json`
  - ローカル API: `sam local start-api`
  - テスト: `cd HelloWorldFunction && ./mvnw test`
  - パッケージ: `cd HelloWorldFunction && ./mvnw package`

## よく使うコマンド

- SAM/Make
  - `make clean` / `make build` / `make package`
  - デプロイ: `make deploy`（AWS プロファイル `serverless` を使用）
  - スタック削除: `make delete`

- ローカル実行
  - 単発実行（イベントファイル）: `sam local invoke HelloWorldFunction --event events/event.json`
  - API エミュレーション: `sam local start-api`

- Maven（アプリ配下）
  - テスト: `cd HelloWorldFunction && ./mvnw test`
  - パッケージ: `cd HelloWorldFunction && ./mvnw package`
  - ネイティブ: `cd HelloWorldFunction && ./mvnw package -Pnative -Dquarkus.native.container-build=true`

## ローカルイベント最小例

- 最小入力（`InputEvent`）
  - `{"name":"Randy"}`
- パイプでの実行例
  - `echo '{"name":"Randy"}' | sam local invoke HelloWorldFunction`
- 備考
  - `events/event.json` は API Gateway 互換の例です。最小入力での検証には上記 JSON を推奨します。

## 開発モードの注意

- `./mvnw quarkus:dev` は便利ですが、Amazon Lambda Binding は dev モードと完全互換ではありません。
- 実運用に近い確認は `sam local invoke` / `sam local start-api` を推奨します。

## CI のビルド

- CI は JDK 17 + Maven で以下を実行します。
  - `cd HelloWorldFunction && ./mvnw -B verify`
  - `cd HelloWorldFunction && ./mvnw -B package`

## Lambda 実行環境の最適化

### アーキテクチャ (Architectures: arm64)

ARM64 (AWS Graviton2) プロセッサを選択した理由:

- **コスト**: x86_64 比で約20%の価格削減
- **パフォーマンス**: Java 17 は ARM64 で最適化されており、同等以上のパフォーマンスを発揮
- **消費電力**: 省電力設計によりサステナビリティに貢献

### メモリ (MemorySize: 256)

256MB を選択した理由:

- Java 17 + Quarkus のシンプルな Hello World アプリケーションに適切なサイズ
- Lambda はメモリ設定に比例して CPU リソースも割り当てられるため、起動時間とのバランスを考慮
- 128MB では起動時間が長くなり、512MB 以上ではコスト増加に見合う効果が薄い

| メモリ設定 | 想定起動時間 (コールドスタート) | 備考 |
|------------|-------------------------------|------|
| 128MB | 〜5-8秒 | メモリ不足でGCが頻発する可能性 |
| 256MB | 〜2-4秒 | 推奨設定 |
| 512MB | 〜1-2秒 | コスト増 |

### タイムアウト (Timeout: 15)

15秒を選択した理由:

- コールドスタート時の初期化時間を考慮 (Java Lambda は初回起動に数秒必要)
- アプリケーション処理自体は軽量 (〜100ms以下)
- 15秒以内に応答がない場合は異常とみなし早期エラー返却

### 計測の推奨

実際のパフォーマンス計測には以下のコマンドを使用:

```bash
# ローカル実行での計測
sam local invoke HelloWorldFunction --event events/event.json

# デプロイ後の計測
sam logs -n HelloWorldFunction --stack-name sam-hello-lambda --tail
```

CloudWatch Logs でコールドスタート / ウォームスタートの Duration を確認し、必要に応じてメモリ設定を調整してください。
