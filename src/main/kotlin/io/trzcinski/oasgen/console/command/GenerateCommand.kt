package io.trzcinski.oasgen.console.command

import io.trzcinski.oasgen.console.Command
import io.trzcinski.oasgen.apidefinition.ApiDefinitionFacade
import io.trzcinski.oasgen.templaterenderer.TemplateRenderer
import io.trzcinski.oasgen.templaterenderer.dto.RenderedFile
import io.trzcinski.oasgen.templatesupplier.TemplateSupplier
import io.trzcinski.oasgen.templatewriter.TemplateWriter
import io.trzcinski.oasgen.utils.prettyPrint
import java.io.File

class GenerateCommand(
    private val apiDefinitionFacade: ApiDefinitionFacade,
    private val templateSupplier: TemplateSupplier,
    private val templateWriter: TemplateWriter,
    private val templateRenderer: TemplateRenderer,
) : Command {
    override val name: String
        get() = "gen"

    override fun run(args: List<String>) {
        val apiDefinition = apiDefinitionFacade.readFromSwaggerRawSource(args[0])

        val renderedClasses = mutableListOf<RenderedFile>()

        val clients = templateSupplier.getClients()
        val apiModels = templateSupplier.getApiModels()
        if(clients.isEmpty()){
           print("Could not resolve any client template")
        }
        if(apiModels.isEmpty()){
           print("Could not resolve any api model template")
        }
        for (clientTemplate in clients) {
            for (crud in apiDefinition.cruds) {
                if(crud.endpoints.isNotEmpty()) {
                    renderedClasses.add(
                        templateRenderer.render(clientTemplate, crud)
                    )
                }
                for (apiModel in crud.apiModels) {
                    for (apiModelTemplate in apiModels) {
                        renderedClasses.add(
                            templateRenderer.render(apiModelTemplate, crud, apiModel)
                        )
                    }
                }
            }
        }
        renderedClasses.forEach {
            val path = System.getProperty("user.dir")+"/"+args[1]+"/"+it.path;
            File(path).parentFile.mkdirs()
            println("Creating: "+args[1]+"/"+it.path)
            File(path).writeText(it.content)
        }
    }
}