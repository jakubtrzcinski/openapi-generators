import io.swagger.parser.OpenAPIParser
import io.trzcinski.oasgen.apidefinition.ApiDefinitionFacade
import io.trzcinski.oasgen.apidefinition.SwaggerApiDefinitionCreator
import io.trzcinski.oasgen.apidefinition.swagger.CRUDAggregator
import io.trzcinski.oasgen.apidefinition.swagger.EndpointAggregator
import io.trzcinski.oasgen.console.ConsoleAdapter
import io.trzcinski.oasgen.console.command.GenerateCommand
import io.trzcinski.oasgen.console.command.InitCommand
import io.trzcinski.oasgen.filesystem.FilesystemAdapter
import io.trzcinski.oasgen.oas.supplier.OASSupplierFactory
import io.trzcinski.oasgen.platformdiscovery.PlatformDiscoveryFacade
import io.trzcinski.oasgen.templaterenderer.TemplateRenderer
import io.trzcinski.oasgen.templatesupplier.TemplateSupplier

fun main(args: Array<String>) {

    val oasFacade = ApiDefinitionFacade(
        SwaggerApiDefinitionCreator(
            OASSupplierFactory(),
            EndpointAggregator(),
            OpenAPIParser(),
            CRUDAggregator("")
        )
    )
    val templateSupplier = TemplateSupplier()
    val filesystemAdapter = FilesystemAdapter()
    val app = ConsoleAdapter(
        listOf(
            GenerateCommand(
                oasFacade,
                templateSupplier,
                filesystemAdapter,
                TemplateRenderer()
            ),
            InitCommand(
                templateSupplier,
                filesystemAdapter,
                PlatformDiscoveryFacade(filesystemAdapter)
            )
        )
    )

    app.run(args)
}