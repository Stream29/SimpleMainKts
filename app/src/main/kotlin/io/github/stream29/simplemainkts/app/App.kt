package io.github.stream29.simplemainkts.app

import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource

// This is the main entry point of the application.
// It uses the `Printer` class from the `:utils` subproject.
fun main() {
    eval(
        File("script/test.main.kts").toScriptSource()
    ).onSuccess {
        println("Script executed successfully")
        println("return value = ${it.returnValue}")
        ResultWithDiagnostics.Success(it)
    }.onFailure {
        println("Script failed to execute")
        println("Errors: ${it.reports.joinToString("\n")}")
    }
}

fun eval(sourceCode: SourceCode): ResultWithDiagnostics<EvaluationResult> =
    host.eval(sourceCode, compileConfig, evaluationConfig)

