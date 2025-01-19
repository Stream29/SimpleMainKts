package io.github.stream29.simplemainkts.app

import org.jetbrains.kotlin.com.google.common.net.HostSpecifier
import java.io.File
import java.security.MessageDigest
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.ScriptingHostConfiguration
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.compilationCache
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.CompiledScriptJarsCache

val host = BasicJvmScriptingHost()

inline fun <reified T> compileConfig(): ScriptCompilationConfiguration.Builder.() -> Unit =
    {
        implicitReceivers(T::class)
        jvm {
            dependenciesFromClassContext(
                EmptyPackage::class,
                wholeClasspath = true
            )
        }
        hostConfiguration(ScriptingHostConfiguration {
            jvm {
                compilationCache(
                    CompiledScriptJarsCache { script, scriptCompilationConfiguration ->
                        File(cacheLocation, compiledScriptUniqueName(script, scriptCompilationConfiguration))
                    }
                )
            }
        })
    }

fun evaluationConfig(
    receiver: Any,
    classLoader: ClassLoader?,
    args: Array<String>
): ScriptEvaluationConfiguration.Builder.() -> Unit =
    {
        scriptsInstancesSharing(true)
        implicitReceivers(receiver)
        jvm {
            baseClassLoader(classLoader)
        }
        constructorArgs(args)
        enableScriptsInstancesSharing()
    }

@OptIn(ExperimentalStdlibApi::class)
fun compiledScriptUniqueName(
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
    return digestWrapper.digest().joinToString("") { it.toHexString() } + ".jar"
}