# cloud-native-security-labs

## Cloud Native Security Conference とは

Cloud Native Security Conference (クラウドネイティブ・セキュリティ・カンファレンス)は、無料で参加できるデジタルデベロッパーカンファレンスで、IBMとRed Hatのセキュリティエクスパートによる、Kubernetesのセキュリティに関して初心者と専門家の両方の方々のために役立つセッションとハンズオンラボが含まれます。

セッションとハンズオンラボは3つのトラックに分かれています:
- **Application Security** (アプリケーションセキュリティ) - 
Kubernetesまたはマイクロサービスでのコンテナ化されたアプリケーションの保護について学べます。構成、ポリシー、アプリのガバナンス、APIが含まれます
- **Data Security** (データセキュリティ) - 
アプリケーションデータと分析の保護について学べます。プライバシー、データストレージ、暗号化、AI/ML、NISTコントロールが含まれます。
- **DevOps Security** (DevOpsセキュリティ) - 
安全なCI/CDパイプラインの構築について学べます。統合、テスト、脆弱性スキャン、イメージガバナンス、自動化が含まれます。

## 本ワークショップについて

Cloud Native Security ラボは、Kubernetesのセキュリティに関するスキルを習得できる内容になっています。

## Agenda

|   |   |
| - | - |
| [Lab0](workshop/lab-00/README.md) | アカウントのセットアップとクラスタへのアクセス方法 |
| [Lab1](workshop/lab-01/README.md) | LoadBalancerとIngressでService Typeを使用してアプリケーションアクセスを制御するKubernetesのネットワークを構築する |
| [Lab2](workshop/lab-02/README.md) | S3FS-FuseでMongoDBの永続ボリュームを使用して、安全な暗号化オブジェクトストレージを追加する |
| [Lab3](workshop/lab-03/README.md) | Source-to-Imageからイメージへのカスタムビルダーイメージを作成する (S2I) |

## 事前要件

Kubernetesクラスタを用いるため、アクセスできる環境が必要となります: 
- Lab 1: LoadBalancerとIngressを有効にするには、少なくとも2つのワーカーノードを持つ標準クラスターが必要です。また、 `ibmcloud cli` および `kubectl cli` がインストールされたクライアントが必要です。
- Lab 2: PersistentVolumeリソースとPersistentVolumeClaimリソースを追加するには、少なくとも1つのワーカーノードと権限を持つ標準クラスターが必要です。また、`ibmcloud cli`、`kubectl cli` および `helm cli` がインストールされたクライアントが必要です。
- Lab 3: 少なくとも1つのワーカーノードを持つOpenShiftクラスタへの権限が必要です。 また、`ibmcloud cli`、`kubectl cli` および `oc cli` がインストールされているクライアントが必要です。

すべてのラボで、IBM Cloud Shell クライアントを使用できます。Webブラウザから下記のURLでアクセスできます。
https://shell.cloud.ibm.com.

## Markdown lint tool (オプション)

[Markdown lint tool](https://github.com/markdownlint/markdownlint)をインストールするためには、下記のコマンドを実行してください。
```
$ npm install -g markdownlint-cli
```

markdownlintを使用する場合は、下記のコマンドを実行してください。
```
$ markdownlint workshop -c ".markdownlint.json" -o mdl-results.md
```

## Build Gitbook (オプション)

[gitbook-cli](https://github.com/GitbookIO/gitbook-cli)をインストールするためには、下記のコマンドを実行してください。
```
$ npm install -g gitbook-cli
```

gitbook-cliを使用してGitbookファイルを`_book`サブディレクトリにビルドするには、次のコマンドを実行します。
```
$ gitbook build ./workshop
```

次のコマンドを使用すると、Gitbookファイルをローカルで展開できます。
```
$ gitbook serve ./workshop
```
