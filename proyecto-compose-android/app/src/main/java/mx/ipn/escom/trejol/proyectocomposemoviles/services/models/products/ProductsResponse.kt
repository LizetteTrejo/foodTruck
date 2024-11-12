package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products

data class ProductsResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)