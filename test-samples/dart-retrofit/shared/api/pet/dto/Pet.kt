package out.shared.api.pet.dto

import out.shared.api.commons.dto.Category
import out.shared.api.commons.dto.Tag

data class Pet(
    val id: Int?,
    val category: Category?,
    val name: String,
    val photoUrls: String,
    val tags: Tag?,
    val status: String?,
)