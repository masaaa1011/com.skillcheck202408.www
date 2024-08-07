package com.skillcheck202408.www.cat

import com.skillcheck202408.www.cat.IResult
import com.skillcheck202408.www.cat.Result
import com.skillcheck202408.www.cat.Line
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.collections.mapOf

/** オプション名称 */
// HACK: 本Annotationを使用してコマンドのオプションを定義している
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HelpOptionName(val shortOption: String, val longOption: String) {}

/** ヘルプ時のディスクリプション */
// HACK: 本Annotationを使用してヘルプコマンドの説明書きを定義している
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HelpDescription(val description: String) {}

/** 
 * オプションをどう扱うか？のハンドラーのinterface
 */
interface IOptionHandler {
  fun handle(result: IResult<Array<Line>>, options: Array<IOption>): IResult<Array<Line>>
}

/** 
 * オプションをどう扱うか？のハンドラーの実装クラス
 * HACK: 各オプションをaggregateしている
 */
class OptionHandler: IOptionHandler {
  override fun handle(result: IResult<Array<Line>>, options: Array<IOption>): IResult<Array<Line>> {
    if(options.isEmpty()) return result
    return options.fold(result) { acc, opt -> opt.apply(acc) }
  }
}

/** オプションの振る舞いを持つクラス */
interface IOption {
  fun apply(value: IResult<Array<Line>>): IResult<Array<Line>>
}

/**
 * 不正なパラメーターを渡された際の振る舞いを持つ
 */
@HelpOptionName("", "")
@HelpDescription("")
class InvalidOption(private val errorMessage: String): IOption {
  override fun apply(result: IResult<Array<Line>>): IResult<Array<Line>> {
    throw IllegalArgumentException(errorMessage)
  }
}

/**
 * 行Noを追加する振る舞いを持つ
 * ※空行にはNoを付与しない
 */
@HelpOptionName("-b", "--number-nonblank")
@HelpDescription("number nonempty output lines, overrides -n")
class NumberNonBlankOption : IOption {
  override fun apply(result: IResult<Array<Line>>): IResult<Array<Line>> {
    val r =
            result.value().withIndex().map { (index, value) ->
              when {
                value.original.isEmpty() -> Line(value.original, value.output)
                else -> Line(value.original, "${index + 1} ${value.output}")
              }
            }

    return Result(r.toTypedArray())
  }
}

/**
 * 行末に$を付与する振る舞いを持つ
 */
@HelpOptionName("-E", "--show-ends")
@HelpDescription("display $ at end of each line")
class ShowEndsOption : IOption {
  override fun apply(result: IResult<Array<Line>>): IResult<Array<Line>> {
    return Result((result.value().map { Line(it.original, "${it.output}$") }).toTypedArray())
  }
}

/**
 * CatのContext用の値オブジェクト
 * ※Catにてサポートするオプションを定義している
 */
data class Context(val options: Array<KClass<out IOption>>) {}

/**
 * オプションのFactory用interface
 */
interface IOptionFactory {
  fun create(option: String): IOption
}

/**
 * オプションのFactory用実装クラス
 */
class OptionFactory(private val context: Context) : IOptionFactory {
  private val options: Map<KClass<out IOption>, Array<String>> = context.options.associateWith { kClass -> 
    val annotation = kClass.findAnnotation<HelpOptionName>()
    when {
      annotation != null -> {
        val options = mutableListOf<String>()
        if(annotation.shortOption.isNotEmpty()) options.add(annotation.shortOption)
        if(annotation.longOption.isNotEmpty()) options.add(annotation.longOption)
        options.toTypedArray()
      }
      else -> emptyArray()
    }
  }

  override fun create(option: String): IOption {
    for ((kClass, optionArray) in options) {
      if (optionArray.contains(option)) {
          val ctor = kClass.primaryConstructor
          if (ctor != null && ctor.parameters.isEmpty())
            return ctor.call()
      }
    }
    return InvalidOption("cat: unrecognized option '${option}'")
  }
}
