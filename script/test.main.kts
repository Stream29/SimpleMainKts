@file:DependsOn("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")
@file:DependsOn("io.github.stream29:langchain4kt-core-jvm:1.0.0")

import io.github.stream29.langchain4kt.core.message.Message
import io.github.stream29.langchain4kt.core.message.MessageSender
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.html
import kotlinx.html.stream.createHTML

println(Message(MessageSender.User, "hello, model"))

val addressee = args.firstOrNull() ?: "World"

print(createHTML().html {
    body {
        h1 { +"Hello, $addressee!" }
    }
})

var classloader = object {}::class.java.classLoader
while (classloader != null) {
    println(classloader)
    classloader = classloader.parent
}