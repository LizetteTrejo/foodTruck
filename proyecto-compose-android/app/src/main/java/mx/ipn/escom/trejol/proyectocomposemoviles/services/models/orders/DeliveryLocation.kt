package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders

data class DeliveryLocation(
    val address: String,
    val city: String,
    val cp: String,
    val created_at: String,
    val id: Int,
    val is_default: Int,
    val name: String,
    val state: String,
    val status: Int,
    val updated_at: String,
    val user_id: Int
)