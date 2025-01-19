package io.github.stream29.simplemainkts.app

import org.jetbrains.kotlin.mainKts.MainKtsScript
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource

// This is the main entry point of the application.
// It uses the `Printer` class from the `:utils` subproject.
fun main() {
    eval(
        sourceCode = File("script/test.main.kts").toScriptSource(),
        receiver = "receiver",
        classLoader = Thread.currentThread().contextClassLoader,
        args = arrayOf("arg1", "arg2")
    ).onSuccess {
        println("Script executed successfully")
        println("return value = ${it.returnValue}")
        ResultWithDiagnostics.Success(it)
    }.onFailure {
        println("Script failed to execute")
        println("Errors: ${it.reports.joinToString("\n")}")
    }
}

inline fun <reified T : Any> eval(
    sourceCode: SourceCode,
    receiver: T,
    classLoader: ClassLoader? = null,
    args: Array<String> = emptyArray()
): ResultWithDiagnostics<EvaluationResult> =
    host.evalWithTemplate<MainKtsScript>(
        sourceCode,
        compileConfig<T>(),
        evaluationConfig(receiver, classLoader, args)
    )

