@file:DependsOn("io.github.stream29:streamlin-jvm:2.5.0")
@file:DependsOn("io.github.stream29:langchain4kt-core-jvm:1.7.0")
@file:DependsOn("io.github.stream29:langchain4kt-api-openai-jvm:1.7.0")
@file:DependsOn("io.ktor:ktor-client-cio-jvm:3.0.0")
@file:DependsOn("io.ktor:ktor-client-logging-jvm:3.0.0")
@file:DependsOn("io.ktor:ktor-client-content-negotiation-jvm:3.0.0")
@file:DependsOn("io.ktor:ktor-client-auth-jvm:3.0.0")

import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import io.github.stream29.langchain4kt.api.openai.OpenAiChaiApiProvider
import io.github.stream29.langchain4kt.api.openai.OpenAiGenerationConfig
import io.github.stream29.langchain4kt.core.generateFrom
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.runBlocking

println(io.github.stream29.simplemainkts.app.host)

val clientConfig = OpenAIConfig(
    token = System.getenv("ALIBABA_QWEN_API_KEY")!!,
    host = OpenAIHost(baseUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1/completions"),
    engine = CIO.create(),
    logging = LoggingConfig(logLevel = LogLevel.None),
)
val generationConfig = OpenAiGenerationConfig(model = "qwen-turbo")
val chatApiProvider = OpenAiChaiApiProvider(
    clientConfig,
    generationConfig
)
runBlocking {
    val response = chatApiProvider.generateFrom("hello")
    println(response)
}