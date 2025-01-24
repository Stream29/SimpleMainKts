package io.github.stream29.simplemainkts.app

import org.jetbrains.kotlin.mainKts.MainKtsScript
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.time.measureTimedValue

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Parameter missing: script file path")
        return
    }
    measureTimedValue {
        eval(
            sourceCode = File(args[0]).toScriptSource(),
            receiver = "receiver",
            classLoader = Thread.currentThread().contextClassLoader,
            args = arrayOf("arg1", "arg2")
        )
    }.run {
        value.onSuccess {
            println("Script executed successfully in $duration")
            println("return value = ${it.returnValue}")
            ResultWithDiagnostics.Success(it)
        }.onFailure {
            println("Script failed to execute")
            println("Errors: ${it.reports.joinToString("\n")}")
        }
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

