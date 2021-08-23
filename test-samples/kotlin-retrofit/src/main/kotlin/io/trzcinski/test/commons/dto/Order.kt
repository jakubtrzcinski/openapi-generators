package out.io.trzcinski.test.commons.dto
data class Order(
    val id: Int?,
    val petId: Int?,
    val quantity: Int?,
    val shipDate: org.joda.time.DateTime?,
    val status: String?,
    val complete: Boolean?,
)