.PHONY: build
build:
	./gradlew build -x test
	# ./gradlew build -x test --stacktrace --info --debug

run:
	# 通常のcatのパターン	
	./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example1.txt"
	# 2ファイル出力パターン
	# ./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example1.txt /workspaces/com.skillcheck202408.www/.test/example2.txt"

	# --number-nonblankのオプションを付与
	# ./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example1.txt --number-nonblank"
	# --number-nonblankのエイリアスを付与
	# ./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example1.txt -b"

	# --show-endsのオプションを付与
	# ./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example1.txt --show-ends"
	# --show-endsのエイリアスを付与
	# ./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example1.txt -E"

	# helpを表示
	# ./gradlew run --stacktrace --args="--help"

	# 無効なオプションを渡された
	# ./gradlew run --stacktrace --args="/workspaces/com.skillcheck202408.www/.test/example1.txt --invalid"
.PHONY: test
test:
	./gradlew test