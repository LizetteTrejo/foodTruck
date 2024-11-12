package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders

data class OrderResponse(
    val `data`: List<Data>,
    val success: Boolean
)