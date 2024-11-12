package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders

data class Product(
    val id: Int,
    val name: String,
    val order_id: Int,
    val price: Int,
    val product_id: Int
)