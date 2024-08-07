# メモ

## 1. devcontainer起動後に実施する操作
~~~sh
# oracleの21をインストール
sdk install java 21.0.4-oracle

# gradleでビルドする
make build
~~~

## 2. 実行用メモ
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

## 3. サンプルアプリについて

1. kotolinで実装をしています（初めてkotlin書きました）
2. 全体的に例外処理は割愛をしています
3. クラス構成をポイントとして書いています
4. Catの機能拡張をしやすいように、AnnotationとDIで拡張性を持たせています