package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders

data class DataX(
    val cost: Int,
    val created_at: String,
    val date_cancel: Any,
    val date_delivery: Any,
    val date_delivery_end: Any,
    val deliver: Deliver,
    val deliver_id: Int,
    val delivery_location: DeliveryLocation,
    val delivery_location_id: Int,
    val id: Int,
    val order_number: String,
    val products: List<Product>,
    val status: Int,
    val status_text: String,
    val total_products: Int,
    val type_delivery: Int,
    val type_pay: Any,
    val updated_at: String,
    val user: User,
    val user_id: Int
)