package io.trzcinski.oasgen.templatesupplier

import com.google.common.io.Resources
import io.trzcinski.oasgen.templaterenderer.dto.FileTemplate
import java.io.File

class TemplateSupplier {

    fun getApiModels() : List<FileTemplate> {
        return findInDirMatching(System.getProperty("user.dir"), Regex("dto")).map {
            FileTemplate(
                it.path
                    .replace(".vm", "")
                    .replace("/oasgen/", "")
                    .replace(System.getProperty("user.dir"), ""),
                File(it.path).readText()
            )
        }
    }
    fun getClients() : List<FileTemplate> {
        return findInDirMatching(System.getProperty("user.dir"), Regex("crud")).map {
            FileTemplate(
                it.path
                    .replace(".vm", "")
                    .replace("/oasgen/", "")
                    .replace(System.getProperty("user.dir"), ""),
                File(it.path).readText()
            )
        }
    }
    fun findInDirMatching(dir: String, matching: Regex) : List<File> {
        return File(dir+"/oasgen").walk().filter { matching.containsMatchIn(it.name) }.filter { it.isFile }.toList()
    }

    fun fromResources(name: String): FileTemplate {
        return FileTemplate(
            name.substring(name.indexOf("/")+1, name.length),
            Resources.getResource("templates/$name").readText()
        )
    }
}