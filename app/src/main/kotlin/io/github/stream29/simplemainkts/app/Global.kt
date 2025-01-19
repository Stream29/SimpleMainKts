package io.github.stream29.simplemainkts.app

import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.dependencies.DependsOn
import kotlin.script.experimental.dependencies.Repository
import kotlin.script.experimental.host.ScriptingHostConfiguration
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.compilationCache
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.CompiledScriptJarsCache
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

val host = BasicJvmScriptingHost()

val scriptDefinition = createJvmCompilationConfigurationFromTemplate<SimpleMainKtsScript> {
    defaultImports(DependsOn::class, Repository::class, Import::class, CompilerOptions::class)
    implicitReceivers(String::class)
    jvm {
        dependenciesFromClassContext(
            SimpleMainKtsScriptDefinition::class,
            "kotlin-stdlib", "kotlin-reflect", "kotlin-scripting-dependencies",
            wholeClasspath = true
        )
    }

    refineConfiguration {
        onAnnotations(
            DependsOn::class,
            Repository::class,
            Import::class,
            CompilerOptions::class,
            handler = MainKtsConfigurator()
        )
    }
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    hostConfiguration(ScriptingHostConfiguration {
        jvm {
            compilationCache(
                CompiledScriptJarsCache { script, scriptCompilationConfiguration ->
                    File(
                        cacheLocation,
                        compiledScriptUniqueName(script, scriptCompilationConfiguration) + ".jar"
                    )
                }
            )
        }
    })
}

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
