package io.github.stream29.simplemainkts.app

import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource

// This is the main entry point of the application.
// It uses the `Printer` class from the `:utils` subproject.
fun main() {
    evalString(
        """
            println(this)
            """.trimIndent()
    ).onSuccess {
        println("Script executed successfully")
        println("return value = ${it.returnValue}")
        ResultWithDiagnostics.Success(it)
    }.onFailure {
        println("Script failed to execute")
        println("Errors: ${it.reports}")
    }
}

fun evalString(script: String): ResultWithDiagnostics<EvaluationResult> =
    eval(script.toScriptSource())

fun eval(sourceCode: SourceCode, cacheDir: File? = null): ResultWithDiagnostics<EvaluationResult> =
    host.eval(sourceCode, scriptDefinition, evaluationConfig)

