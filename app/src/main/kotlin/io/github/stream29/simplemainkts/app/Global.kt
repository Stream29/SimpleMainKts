package io.github.stream29.simplemainkts.app

import kotlin.script.experimental.api.constructorArgs
import kotlin.script.experimental.api.enableScriptsInstancesSharing
import kotlin.script.experimental.api.with
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

val scriptDefinition = createJvmCompilationConfigurationFromTemplate<SimpleMainKtsScript>()

val host = BasicJvmScriptingHost()

val evaluationConfig =
    MainKtsEvaluationConfiguration.with {
        jvm {
            baseClassLoader(null)
        }
        constructorArgs(emptyArray<String>())
        enableScriptsInstancesSharing()
    }
