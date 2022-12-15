package io.trzcinski.oasgen.console.command

import io.trzcinski.oasgen.apidefinition.ApiDefinitionFacade
import io.trzcinski.oasgen.console.Command
import io.trzcinski.oasgen.filesystem.FilesystemAdapter
import io.trzcinski.oasgen.templaterenderer.TemplateRenderer
import io.trzcinski.oasgen.templaterenderer.dto.RenderedFile
import io.trzcinski.oasgen.templatesupplier.TemplateSupplier

class GenerateCommand(
    private val apiDefinitionFacade: ApiDefinitionFacade,
    private val templateSupplier: TemplateSupplier,
    private val filesystemAdapter: FilesystemAdapter,
    private val templateRenderer: TemplateRenderer,
) : Command {
    override val name: String
        get() = "gen"

    override fun run(args: List<String>) {
        if(args.size != 2){
            println("gen required 2 arguments")
            println("Usage:")
            println("  oasgen.sh {path-to-swagger-json} {save-base-path}")
            return
        }
        val apiDefinition = apiDefinitionFacade.readFromSwaggerRawSource(args[0])

        val renderedClasses = mutableListOf<RenderedFile>()

        val clients = templateSupplier.getClients()
        val apiModels = templateSupplier.getApiModels()
        if(clients.isEmpty()){
           println("Could not resolve any client template")
        }
        if(apiModels.isEmpty()){
            println("Could not resolve any api model template")
        }
        for (clientTemplate in clients) {
            for (crud in apiDefinition.cruds) {
                if(crud.endpoints.isNotEmpty()) {
                    try {
                        renderedClasses.add(
                            templateRenderer.render(clientTemplate, crud, args[1])
                        )
                    } catch (ex: Exception){}
                }
                for (apiModel in crud.apiModels) {
                    for (apiModelTemplate in apiModels) {
                        try {
                            renderedClasses.add(
                                templateRenderer.render(apiModelTemplate, crud, apiModel, args[1])
                            )
                        } catch (ex: Exception){}
                    }
                }
            }
        }
        renderedClasses.forEach {
            filesystemAdapter.save(args[1]+"/"+it.path, it.content)
        }
    }
}
