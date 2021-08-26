package out.io.trzcinski.test.pet.dto

import out.io.trzcinski.test.commons.dto.Category
import out.io.trzcinski.test.commons.dto.Tag

data class Pet(
    val id: Int?,
    val category: Category?,
    val name: String,
    val photoUrls: List<String>,
    val tags: List<Tag>?,
    val status: String?,
)