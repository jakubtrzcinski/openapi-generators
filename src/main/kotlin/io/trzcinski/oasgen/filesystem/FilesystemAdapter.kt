package io.trzcinski.oasgen.filesystem

import java.io.File

class FilesystemAdapter {

    fun mkdirs(dir: String){
        File(relativize(dir)).mkdirs()
    }
    fun exists(dir: String) : Boolean{
        return File(relativize(dir)).exists()
    }
    fun save(path: String, content: String){
        println("Creating file: $path")
        val file = File(relativize(path))
        file.parentFile.mkdirs()
        file.writeText(content.trim())
    }

    private fun relativize(path: String) : String {
        if(path.startsWith("/")){
            return System.getProperty("user.dir")+path
        }
        return System.getProperty("user.dir")+"/"+path
    }

    fun walkTopDown(): FileTreeWalk {
        return File(relativize("/")).walkTopDown()

    }
}