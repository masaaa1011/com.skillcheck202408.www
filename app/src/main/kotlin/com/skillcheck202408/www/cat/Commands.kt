package com.skillcheck202408.www.cat

import kotlin.reflect.KClass
import com.skillcheck202408.www.cat.IResult
import com.skillcheck202408.www.cat.Result
import com.skillcheck202408.www.cat.HelpOptionName
import com.skillcheck202408.www.cat.HelpDescription
import com.skillcheck202408.www.cat.Line
import java.io.File

/**
 * コマンドのinterface
 * ※catしか書かないのであまり意味なし
 */
interface ICommand {
  fun help()
}

/**
 * Catコマンドのinterface
 */
interface ICat: ICommand {
  fun invoke(paths: Array<String>): Array<IResult<Array<Line>>>
  fun invoke(paths: Array<String>, options: Array<String>): Array<IResult<Array<Line>>>
}

/**
 * Catの実装クラス
 */
class Cat(private val context: Context, private val factory: IOptionFactory, private val handler: IOptionHandler) : ICat {
  /**
   * オプションなしの場合のメイン処理
   */
  override fun invoke(paths: Array<String>): Array<IResult<Array<Line>>> { 
     return this.invoke(paths, emptyArray())
  }

  /**
   * メイン処理
   * 
   * 1. 対象のファイルを各行のArrayで取得
   * 2. オプションの適応
   * 3. 標準出力へとファイル内容を出力
   * 
   * HACK: 例外処理は割愛
   * HACK: toTypedArray()が蔓延っているが、もっと減らしたが良い
   */
  override fun invoke(paths: Array<String>, options: Array<String>): Array<IResult<Array<Line>>> { 
    val _options = options.map { factory.create(it) }.toTypedArray()
    val results = paths.map { 
      val file = File(it)
      if (!file.exists() || !file.isFile) throw IllegalArgumentException("no such file.")

      // HACK: handlerにてオプションが0だった時の考慮をしているが、本来的にはダメ（DIで渡されたもの次第では予期せぬ動きをする）
      handler.handle(
        Result(file.readLines().map { Line(it, it) }.toTypedArray()),
        _options
      )
    }

    // HACK: 出力用のクラスは本当は分けてDIした方が良い
    results.toTypedArray().forEach { r ->
      val value = r.value()
      value.forEach { v ->
        println(v.output)
       }
     }

    return results.toTypedArray()
  }

  /**
   * ヘルプ表示
   * 
   * 1. ヘルプの文字数をそろえるために、最大文字数を取得（by GPT）
   * 2. 最大文字数を加味して標準出力へ
   * HACK: 複数のコマンドクラス実装時は、abstractクラスへと移すこと
   */
  override fun help() {
    val maxLength = this.context.options.map { c ->
      val optionName = c.annotations.filterIsInstance<HelpOptionName>().firstOrNull()
      if (optionName != null) {
          "${optionName.shortOption}, ${optionName.longOption}".length
      } else 0
    }.maxOrNull() ?: 0
  
    this.context.options.forEach { c ->
      val optionAnnotation = c.annotations.filterIsInstance<HelpOptionName>().firstOrNull()
      val descriptionAnnotation = c.annotations.filterIsInstance<HelpDescription>().firstOrNull()
      if (optionAnnotation != null && descriptionAnnotation != null) {
          val names = if (optionAnnotation.longOption.isNotEmpty()) {
              "${optionAnnotation.shortOption}, ${optionAnnotation.longOption}"
          } else {
              optionAnnotation.shortOption
          }
          println("  " + names.padEnd(maxLength + 2) + descriptionAnnotation.description)
      }
    }
  }
}
