package projects.mayurmhm.mynotes.model

// Holds user request related information
data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)