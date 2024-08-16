# メモ

## 0. 初めに
~~~txt
本ソースはDevcontainerで開発をしています
起動にはDocker環境が必要となります

以下起動にに必要なものです
1. VSCode
2. Docker Runtime
~~~

## 1. devcontainerの起動
1. VSCodeに拡張機能：ms-vscode-remote.remote-containers をインストールする
2. Ctrl + Shift + P でコマンドパレットを開く
3. "Dev Container: Rebuild and Reopen in Container" を選択


## 2. devcontainer起動後に実施する操作
~~~sh
# oracleの21をインストール
sdk install java 21.0.4-oracle

# gradleでビルドする
make build
~~~

## 3. 実行用メモ
~~~txt
Makefileに実行用のコマンドをメモしています
make runのコマンド配下に各実行パターンを記載しています
実行したい操作をコメントアウトし、実行をお願いいたします
~~~

~~~sh
# アプリケーションを実行する
make run

# 以下Makefileのサンプルです
# 実行したいコマンドをコメントアウトし、make runの実行をお願いいたします
# run:
	# 通常のcatのパターン
	# ./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example.txt"
	# --number-nonblankのオプションを付与
	# ./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example.txt --number-nonblank"
    # ...その他のパターン
~~~

## 4. 工夫した点など

1. kotolinで実装をしています（初めてkotlin書きました）
2. 時間の都合上、例外処理は割愛をしています
3. クラス設計を実装のポイントとしてします
    1. Commandsに実処理を実装しています
	2. Optionsに各オプションの実処理を実装しています
	3. AOPを使用して、オプションの名称、Help時のDescriptionを実装しています
4. 新たにオプションを実装する際にコストが低くなるよう、AnnotationとDIで拡張性を持たせています
    1. AOPを使用し、新オプションの実装を楽にしています
	2. IOptionsインターフェースを継承した実装クラスを作成 → Annotationの付与、でオプション実装ができます
	3. エントリポイントにてCatへのcontextに実装クラスの情報を渡すことによってオプションを有効化できます
5. 低レイヤー処理の実装はGPTを使用しています（時間短縮の目的）
6. devcontainerを使用しているので、docker環境があればすぐに動かすことができます
