package io.trzcinski.oasgen.templaterenderer

import io.trzcinski.oasgen.apidefinition.dto.ApiModel
import io.trzcinski.oasgen.apidefinition.dto.CRUD
import io.trzcinski.oasgen.templaterenderer.dto.FileTemplate
import io.trzcinski.oasgen.templaterenderer.dto.RenderedFile
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.resource.loader.StringResourceLoader
import java.io.File
import java.io.StringWriter


class TemplateRenderer(
    private val velocityEngine : VelocityEngine = VelocityEngine().also {
        it.setProperty(Velocity.RESOURCE_LOADER, "string")
        it.setProperty("resource.loader.string.class", StringResourceLoader::class.java.name)
        it.init()
    }
) {

    fun render(template: FileTemplate, crud: CRUD) : RenderedFile {
        val context = VelocityContext().also {
            it.put("crud", crud)
            it.put("baseUrl", "http://localhost")
        }
        return RenderedFile(
            render(template.path, context),
            render(template.content, context)
        )
    }

    fun render(template: FileTemplate, crud: CRUD, dto: ApiModel) : RenderedFile {
        val context = VelocityContext().also {
            it.put("dto", dto)
            it.put("crud", crud)
            it.put("baseUrl", "http://localhost")
        }
        return RenderedFile(
            render(template.path, context),
            render(template.content, context)
        )
    }


    private fun render(rawTemplate: String, context: VelocityContext): String {
        StringResourceLoader.getRepository().putStringResource(rawTemplate.hashCode().toString(), rawTemplate);
        val t: Template = velocityEngine.getTemplate(rawTemplate.hashCode().toString())
        val writer = StringWriter()
        t.merge(context, writer)
        return writer.toString()
            .replace(Regex(" +\n"), "\n")
            .replace("\n\n\n","\n")
    }

}