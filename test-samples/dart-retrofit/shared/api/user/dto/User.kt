package out.shared.api.user.dto
data class User(
    val id: Int?,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val password: String?,
    val phone: String?,
    val userStatus: Int?,
)