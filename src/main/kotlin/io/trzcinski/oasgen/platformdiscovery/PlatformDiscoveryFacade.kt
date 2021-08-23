package io.trzcinski.oasgen.platformdiscovery

import io.trzcinski.oasgen.filesystem.FilesystemAdapter

class PlatformDiscoveryFacade(
    private val filesystemAdapter: FilesystemAdapter
    ) {
    private val extensions = mapOf(
        "kt" to "kotlin",
        "java" to "java",
        "ts" to "typescript",
        "dart" to "dart",
    )
    fun discoveryProjectLanguage(): String {
        return filesystemAdapter.walkTopDown()
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