import io.swagger.parser.OpenAPIParser
import io.trzcinski.oasgen.console.ConsoleAdapter
import io.trzcinski.oasgen.console.command.GenerateCommand
import io.trzcinski.oasgen.apidefinition.ApiDefinitionFacade
import io.trzcinski.oasgen.apidefinition.SwaggerApiDefinitionCreator
import io.trzcinski.oasgen.apidefinition.swagger.EndpointAggregator
import io.trzcinski.oasgen.apidefinition.swagger.CRUDAggregator
import io.trzcinski.oasgen.oas.supplier.OASSupplierFactory


fun main(args: Array<String>) {

    val oasFacade = ApiDefinitionFacade(
        SwaggerApiDefinitionCreator(
            OASSupplierFactory(),
            EndpointAggregator(),
            OpenAPIParser(),
            CRUDAggregator("")
        )
    )
    val app = ConsoleAdapter(
        listOf(
            GenerateCommand(oasFacade)
        )
    )

    app.run(args)
}