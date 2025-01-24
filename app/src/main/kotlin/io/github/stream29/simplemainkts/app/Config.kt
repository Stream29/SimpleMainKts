package io.github.stream29.simplemainkts.app

import java.io.File

val cacheLocation = File("cache").apply { if(!exists()) mkdirs() }