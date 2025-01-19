package io.github.stream29.simplemainkts.app

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

// This is the main entry point of the application.
// It uses the `Printer` class from the `:utils` subproject.
fun main() {
    eval(
        "println(\"Hello, world!\")".toScriptSource()
    )
}


fun eval(sourceCode: SourceCode, cacheDir: File? = null): ResultWithDiagnostics<EvaluationResult> =
    withMainKtsCacheDir(cacheDir?.absolutePath ?: "") {
        val scriptDefinition = createJvmCompilationConfigurationFromTemplate<SimpleMainKtsScript>()

        val evaluationEnv = MainKtsEvaluationConfiguration.with {
            jvm {
                baseClassLoader(null)
            }
            constructorArgs(emptyArray<String>())
            enableScriptsInstancesSharing()
        }
        BasicJvmScriptingHost().eval(sourceCode, scriptDefinition, evaluationEnv)
    }

private fun captureOut(body: () -> Unit): String {
    val outStream = ByteArrayOutputStream()
    val prevOut = System.out
    System.setOut(PrintStream(outStream))
    try {
        body()
    } finally {
        System.out.flush()
        System.setOut(prevOut)
    }
    return outStream.toString().trim()
}

private fun <T> withMainKtsCacheDir(value: String?, body: () -> T): T {
    val prevCacheDir = System.getProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
    if (value == null) System.clearProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
    else System.setProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY, value)
    try {
        return body()
    } finally {
        if (prevCacheDir == null) System.clearProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
        else System.setProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY, prevCacheDir)
    }
}