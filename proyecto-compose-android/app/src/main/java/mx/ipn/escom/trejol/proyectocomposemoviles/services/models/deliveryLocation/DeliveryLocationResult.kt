package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.deliveryLocation

data class DeliveryLocationResult(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)