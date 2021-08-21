package io.trzcinski.oasgen.platformdiscovery

import java.io.File

class PlatformDiscoveryFacade {
    val extensions = mapOf(
        "kt" to "kotlin",
        "java" to "java",
        "ts" to "typescript",
        "dart" to "dart",
    )
    fun discoveryProjectLanguage(path: String): String {
        return File(path).walkTopDown()
            .map { it.extension }
            .groupBy { it }
            .mapValues { it.value.size }
            .toList()
            .sortedBy { (_, v) -> v }
            .toMap()
            .keys
            .toList()
            .reversed()
            .first { extensions.containsKey(it) }
            .let { extensions[it]!! }

    }
}