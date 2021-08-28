package io.trzcinski.oasgen.filesystem

import java.io.File

class FilesystemAdapter(
    val root: String
) {

    fun mkdirs(dir: String){
        File(relativize(dir)).mkdirs()
    }
    fun exists(dir: String) : Boolean{
        return File(relativize(dir)).exists()
    }
    fun save(path: String, content: String){
        println("Creating file: "+relativize(path))
        val file = File(relativize(path))
        file.parentFile.mkdirs()
        file.writeText(content.trim())
    }

    private fun relativize(path: String) : String {
        if(path.startsWith("/")){
            return root+path
        }
        return root+"/"+path
    }

    fun walkTopDown(): FileTreeWalk {
        return File(relativize("/")).walkTopDown()

    }

    fun delete(s: String) {
        File(relativize(s)).deleteRecursively()
    }
}