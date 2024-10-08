/*
 * This source file was generated by the Gradle 'init' task
 */
package com.skillcheck202408.www

import com.skillcheck202408.www.cat.Cat
import com.skillcheck202408.www.cat.NumberNonBlankOption
import com.skillcheck202408.www.cat.ShowEndsOption
import com.skillcheck202408.www.cat.OptionFactory
import com.skillcheck202408.www.cat.OptionHandler
import com.skillcheck202408.www.cat.Context
import com.skillcheck202408.www.cat.IOption
import kotlin.reflect.KClass

class App {
  val greeting: String
    get() {
      return "Hello World!"
    }
}

fun main(args: Array<String>) {
  try {
    val context = Context(
      arrayOf(NumberNonBlankOption::class, ShowEndsOption::class)
    )
    val cat = Cat(
      context,
      OptionFactory(context),
      OptionHandler()
    )

    val (options, paths) = args.partition { it.startsWith("-") }

    when {
      options.contains("--help") -> cat.help()
      options.isEmpty() -> cat.invoke(paths.toTypedArray())
      else -> cat.invoke(paths.toTypedArray(), options.toTypedArray())
    }
  }
  catch(e: IllegalArgumentException) {
    println(e.message)
    println("Try 'cat --help' for more information.")
  }
  catch(e: Exception) {
    println(e.message)
    println("Try 'cat --help' for more information.")
  }
}
