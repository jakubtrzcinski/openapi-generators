package io.trzcinski.oasgen.platformdiscovery

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PlatformDiscoveryFacadeTest {

    @Test
    fun discoveryProjectLanguage() {
        assertEquals(PlatformDiscoveryFacade().discoveryProjectLanguage("/home/jakubtrzcinski/IdeaProjects/openapi-generators"), "kotlin")
    }
}