package io.trzcinski.oasgen.oas.supplier

import java.lang.RuntimeException

class OASSupplierException(message: String?, cause: Throwable?) : RuntimeException(message, cause)