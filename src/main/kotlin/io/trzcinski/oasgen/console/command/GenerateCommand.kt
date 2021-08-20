package io.trzcinski.oasgen.console.command

import io.trzcinski.oasgen.console.Command
import io.trzcinski.oasgen.apidefinition.ApiDefinitionFacade

class GenerateCommand(
    val apiDefinitionFacade: ApiDefinitionFacade
) : Command {
    override val name: String
        get() = "gen"

    override fun run(args: List<String>) {
        val apiDefinition = apiDefinitionFacade.readFromSwaggerRawSource(args[0])
    }
}