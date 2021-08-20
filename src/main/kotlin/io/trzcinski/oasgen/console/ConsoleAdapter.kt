package io.trzcinski.oasgen.console

class ConsoleAdapter(
    val commands: List<Command>
) {
    fun run(args: Array<String>){
        for (command in commands) {
            if(command.name == args[0]){
                command.run(args.slice(IntRange(1, args.size-1)))
            }
        }
    }
}