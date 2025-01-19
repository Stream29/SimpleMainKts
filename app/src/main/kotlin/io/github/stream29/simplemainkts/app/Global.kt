package io.github.stream29.simplemainkts.app

import org.jetbrains.kotlin.mainKts.CompilerOptions
import org.jetbrains.kotlin.mainKts.Import
import org.jetbrains.kotlin.mainKts.MainKtsEvaluationConfiguration
import org.jetbrains.kotlin.mainKts.MainKtsScript
import org.jetbrains.kotlin.mainKts.MainKtsScriptDefinition
import java.io.File
import java.security.MessageDigest
import kotlin.script.experimental.api.*
import kotlin.script.experimental.dependencies.DependsOn
import kotlin.script.experimental.dependencies.Repository
import kotlin.script.experimental.host.ScriptingHostConfiguration
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.compilationCache
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvm.mainArguments
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.CompiledScriptJarsCache
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

val host = BasicJvmScriptingHost()

val compileConfig = createJvmCompilationConfigurationFromTemplate<MainKtsScript> {
    defaultImports(DependsOn::class, Repository::class, Import::class, CompilerOptions::class)
    implicitReceivers(String::class)
    jvm {
        dependenciesFromClassContext(
            MainKtsScriptDefinition::class,
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
    MainKtsEvaluationConfiguration.with(fun ScriptEvaluationConfiguration.Builder.() {
        scriptsInstancesSharing(true)
        implicitReceivers("hello")
        refineConfigurationBeforeEvaluate {
            val mainArgs = it.evaluationConfiguration[ScriptEvaluationConfiguration.jvm.mainArguments]
            val res =
                if (it.evaluationConfiguration[ScriptEvaluationConfiguration.constructorArgs] == null && mainArgs != null) {
                    it.evaluationConfiguration.with {
                        constructorArgs(mainArgs)
                    }
                } else it.evaluationConfiguration
            res.asSuccess()
        }
        jvm {
            baseClassLoader(null)
        }
        constructorArgs(emptyArray<String>())
        enableScriptsInstancesSharing()
    })

@OptIn(ExperimentalStdlibApi::class)
internal fun compiledScriptUniqueName(
    script: SourceCode,
    scriptCompilationConfiguration: ScriptCompilationConfiguration
): String {
    val digestWrapper = MessageDigest.getInstance("MD5")
    digestWrapper.update(script.text.toByteArray())
    scriptCompilationConfiguration.notTransientData.entries
        .sortedBy { it.key.name }
        .forEach {
            digestWrapper.update(it.key.name.toByteArray())
            digestWrapper.update(it.value.toString().toByteArray())
        }
    return digestWrapper.digest().joinToString("") { it.toHexString() }
}