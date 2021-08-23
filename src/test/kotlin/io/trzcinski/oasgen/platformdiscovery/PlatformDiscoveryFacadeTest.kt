package io.trzcinski.oasgen.platformdiscovery

import io.trzcinski.oasgen.filesystem.FilesystemAdapter
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PlatformDiscoveryFacadeTest {

    @Test
    fun discoveryProjectLanguage() {
        assertEquals(PlatformDiscoveryFacade(FilesystemAdapter()).discoveryProjectLanguage(), "kotlin")
    }
}