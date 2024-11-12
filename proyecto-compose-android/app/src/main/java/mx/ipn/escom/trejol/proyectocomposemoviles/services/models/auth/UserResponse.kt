package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth

data class UserResponse(
    val `data`: List<DataX>,
    val message: String,
    val success: Boolean
)