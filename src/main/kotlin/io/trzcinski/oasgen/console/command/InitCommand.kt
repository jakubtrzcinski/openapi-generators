package io.trzcinski.oasgen.console.command

import io.trzcinski.oasgen.console.Command
import io.trzcinski.oasgen.filesystem.FilesystemAdapter
import io.trzcinski.oasgen.platformdiscovery.PlatformDiscoveryFacade
import io.trzcinski.oasgen.templaterenderer.dto.FileTemplate
import io.trzcinski.oasgen.templatesupplier.TemplateSupplier

class InitCommand(
    private val templateSupplier: TemplateSupplier,
    private val filesystemAdapter: FilesystemAdapter,
    private val discoveryFacade: PlatformDiscoveryFacade,
) : Command {
    override val name: String
        get() = "init"

    override fun run(args: List<String>) {
        if(filesystemAdapter.exists("oasgen")){
            println("Project is already initialized")
            return
        }
        filesystemAdapter.mkdirs("oasgen")
        val lang = discoveryFacade.discoveryProjectLanguage()
        println("Detected platform: $lang")
        startingTemplates(lang).forEach {
            filesystemAdapter.save("oasgen/"+it.path, it.content)
        }
    }
    private fun startingTemplates(lang: String) : List<FileTemplate> {
        if(lang == "dart"){
            return listOf(
                templateSupplier.fromResources("dart-retrofit/\${crud.name.snakeCaseLowercase}/dto/\${dto.name.snakeCaseLowercase}.dart.vm"),
                templateSupplier.fromResources("dart-retrofit/\${crud.name.snakeCaseLowercase}/\${crud.name.snakeCaseLowercase}_client.dart.vm"),
            )
        }
        if(lang == "kotlin"){
            return listOf(
                templateSupplier.fromResources("kotlin-retrofit/\${crud.name.snakeCaseLowercase}/dto/\${dto.name.pascalCase}.kt.vm"),
                templateSupplier.fromResources("kotlin-retrofit/\${crud.name.snakeCaseLowercase}/\${crud.name.pascalCase}Client.kt.vm"),
            )
        }
        throw RuntimeException("lang $lang not supported")
    }
}