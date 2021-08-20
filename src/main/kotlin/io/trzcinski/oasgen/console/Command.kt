package io.trzcinski.oasgen.console


interface Command{
    val name: String

    fun run(args: List<String>)
}