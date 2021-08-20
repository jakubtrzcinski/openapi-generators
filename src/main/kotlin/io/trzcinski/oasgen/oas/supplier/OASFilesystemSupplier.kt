package io.trzcinski.oasgen.oas.supplier

import java.io.File
import java.lang.Exception

class OASFilesystemSupplier(
    private val file: String
) : OASSupplier {
    override fun get(): String {
        try {
            return File(file).readText()
        } catch (ex: Exception){
            throw OASSupplierException("Could not read OpenApi specification from given file: $file", ex)
        }
    }
}