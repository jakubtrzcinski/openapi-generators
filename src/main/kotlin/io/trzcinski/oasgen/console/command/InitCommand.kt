package io.trzcinski.oasgen.console.command

import io.trzcinski.oasgen.console.Command
import io.trzcinski.oasgen.apidefinition.ApiDefinitionFacade
import io.trzcinski.oasgen.platformdiscovery.PlatformDiscoveryFacade
import io.trzcinski.oasgen.templaterenderer.TemplateRenderer
import io.trzcinski.oasgen.templaterenderer.dto.RenderedFile
import io.trzcinski.oasgen.templatesupplier.TemplateSupplier
import io.trzcinski.oasgen.templatewriter.TemplateWriter
import io.trzcinski.oasgen.utils.prettyPrint
import java.io.File

class InitCommand(
    private val templateSupplier: TemplateSupplier,
    private val discoveryFacade: PlatformDiscoveryFacade,
) : Command {
    override val name: String
        get() = "init"

    override fun run(args: List<String>) {
        val path = System.getProperty("user.dir")+"/oasgen";
        File(path).mkdirs()
        when(discoveryFacade.discoveryProjectLanguage(System.getProperty("user.dir"))){

        }
    }
}