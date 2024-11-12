package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders

data class Deliver(
    val created_at: String,
    val email: String,
    val id: Int,
    val is_admin: Int,
    val last_name: Any,
    val name: String,
    val password: String,
    val remember_token: Any,
    val status: Int,
    val updated_at: String
)