package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth

data class Data(
    val created_at: String,
    val email: String,
    val id: Int,
    val is_admin: Int,
    val last_name: Any,
    val name: String,
    val status: Int,
    val updated_at: String
)