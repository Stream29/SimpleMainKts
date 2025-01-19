@file:DependsOn("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")

import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.html
import kotlinx.html.stream.createHTML

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