plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation(libs.bundles.kotlinx.all)
    implementation(libs.bundles.kotlin.scripting)
    implementation(libs.apache.ivy)
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "io.github.stream29.simplemainkts.app.AppKt"
    applicationName = "SimpleMainKts"
}
