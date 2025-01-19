package io.github.stream29.simplemainkts.app

import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

val scriptDefinition = createJvmCompilationConfigurationFromTemplate<SimpleMainKtsScript>()

val host = BasicJvmScriptingHost()

val evaluationConfig =
    MainKtsEvaluationConfiguration {
        scriptsInstancesSharing(true)
        implicitReceivers("hello")
        refineConfigurationBeforeEvaluate(::configureConstructorArgsFromMainArgs)
        jvm {
            baseClassLoader(null)
        }
        constructorArgs(emptyArray<String>())
        enableScriptsInstancesSharing()
    }
