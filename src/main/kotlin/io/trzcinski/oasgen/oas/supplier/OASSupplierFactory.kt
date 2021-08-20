package io.trzcinski.oasgen.oas.supplier

class OASSupplierFactory {
    fun fromRawSource(raw: String) : OASSupplier {
        if(raw.startsWith("http")){
            return OASHttpSupplier(raw)
        }
        return OASFilesystemSupplier(raw)
    }
}