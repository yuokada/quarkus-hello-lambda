# AGENTS.md

このドキュメントは開発者向けの補足情報を提供します。

## HelloWorldFunction/Makefile のビルドフラグ

`HelloWorldFunction/Makefile` では、SAM ビルド時に以下の環境変数フラグを使用できます。

### SCRATCH_BUILD

クリーンビルドを実行するかどうかを指定します。

| 値 | 動作 |
|----|------|
| `true` | ビルド前に `./mvnw clean` を実行し、キャッシュを削除してからビルド |
| 未設定 / その他 | クリーンを実行せず、差分ビルド |

**使用例:**

```bash
SCRATCH_BUILD=true sam build
```

### NATIVE_BUILD

GraalVM を使用したネイティブビルドを実行するかどうかを指定します。

| 値 | 動作 |
|----|------|
| `true` | `./mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker` でネイティブバイナリを生成 |
| 未設定 / その他 | `./mvnw package` で標準 JAR をビルド |

**使用例:**

```bash
NATIVE_BUILD=true sam build
```

### フラグの組み合わせ

両方のフラグを組み合わせて使用することも可能です。

```bash
# クリーンビルド + ネイティブビルド
SCRATCH_BUILD=true NATIVE_BUILD=true sam build
```

### 注意事項

- ネイティブビルドには GraalVM と Docker が必要です。Dev Container 環境では GraalVM がプリインストールされています。
- ネイティブビルドは標準ビルドより時間がかかりますが、Lambda のコールドスタート時間を大幅に短縮できます。
