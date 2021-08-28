package io.trzcinski.oasgen

import io.swagger.parser.OpenAPIParser
import io.trzcinski.oasgen.apidefinition.ApiDefinitionFacade
import io.trzcinski.oasgen.apidefinition.SwaggerApiDefinitionCreator
import io.trzcinski.oasgen.apidefinition.swagger.CRUDAggregator
import io.trzcinski.oasgen.apidefinition.swagger.EndpointAggregator
import io.trzcinski.oasgen.apidefinition.swagger.mapper.DtoMapper
import io.trzcinski.oasgen.apidefinition.swagger.mapper.PathMapper
import io.trzcinski.oasgen.console.ConsoleAdapter
import io.trzcinski.oasgen.console.command.GenerateCommand
import io.trzcinski.oasgen.console.command.InitCommand
import io.trzcinski.oasgen.filesystem.FilesystemAdapter
import io.trzcinski.oasgen.oas.supplier.OASSupplierFactory
import io.trzcinski.oasgen.platformdiscovery.PlatformDiscoveryFacade
import io.trzcinski.oasgen.templaterenderer.TemplateRenderer
import io.trzcinski.oasgen.templatesupplier.TemplateSupplier

fun main(args: Array<String>) {
    init(System.getProperty("user.dir")).run(args)
}

fun init(path: String) : ConsoleAdapter{
    val dtoMapper = DtoMapper()
    val oasFacade = ApiDefinitionFacade(
        SwaggerApiDefinitionCreator(
            OASSupplierFactory(),
            EndpointAggregator(PathMapper(dtoMapper), dtoMapper),
            OpenAPIParser(),
            CRUDAggregator()
        )
    )
    val filesystemAdapter = FilesystemAdapter(path)
    val templateSupplier = TemplateSupplier(filesystemAdapter)
    return ConsoleAdapter(
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
}