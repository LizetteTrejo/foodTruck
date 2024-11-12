package mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders

data class Data(
    val cost: Int,
    val created_at: Any,
    val date_cancel: Any,
    val date_delivery: Any,
    val date_delivery_end: Any,
    val deliver_id: Int,
    val delivery_location_id: Int,
    val id: Int,
    val order_number: String,
    val status: Int,
    val status_text: String,
    val total_products: Int,
    val total_shipping: Int,
    val type_delivery: Int,
    val type_pay: String,
    val updated_at: Any,
    val user_id: Int
)