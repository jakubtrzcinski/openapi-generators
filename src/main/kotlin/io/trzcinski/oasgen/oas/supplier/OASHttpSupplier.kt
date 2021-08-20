package io.trzcinski.oasgen.oas.supplier

import java.lang.Exception
import java.net.URL

class OASHttpSupplier(
    private val url: String
) : OASSupplier {
    override fun get(): String {
        try {
            return URL(url).readText()
        } catch (ex: Exception){
            throw OASSupplierException("Could not read OpenApi specification from given url: $url", ex)
        }
    }
}