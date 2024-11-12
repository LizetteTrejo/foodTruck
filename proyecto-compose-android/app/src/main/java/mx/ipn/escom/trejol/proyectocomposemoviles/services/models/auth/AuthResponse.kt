package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth

data class AuthResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean,
    val token: String
)