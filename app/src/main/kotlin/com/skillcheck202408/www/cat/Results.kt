package com.skillcheck202408.www.cat

/**
 * コマンド結果のinterface
 */
interface IResult<T> {
  fun value(): T
}

/**
 * 各行を表す値オブジェクト
 * HACK: originalとoutputで分けているのは、Catのオプションの条件式に、原文を使用するため分けている
 * 例）各行の空行以外にナンバリングをする -> originalの行を使用して判定する必要がある
 */
data class Line(val original: String, var output: String)

/**
 * コマンド結果の実装値オブジェクト
 */
data class Result(val value: Array<Line>): IResult<Array<Line>> {
    override fun value(): Array<Line> { 
      return value;
    }
}