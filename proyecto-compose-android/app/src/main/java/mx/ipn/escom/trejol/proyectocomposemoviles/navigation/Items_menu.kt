package mx.ipn.escom.trejol.proyectocomposemoviles.navigation

import mx.ipn.escom.trejol.proyectocomposemoviles.R

sealed class Items_menu(
    val icon: Int,
    val title: String,
    val ruta: String
){
    object Pantalla1: Items_menu(
        R.drawable.catalogo,
        "Catálogo", NavScreen.Catalogo.name)
    object Pantalla2: Items_menu(
        R.drawable.cart,
        "Carrito", NavScreen.Carrito.name)
    object Pantalla3: Items_menu(
        R.drawable.pedidos,
        "Pedidos", NavScreen.Pedidos.name)
    object Pantalla4: Items_menu(
        R.drawable.ubicaciones,
        "Ubicaciones", NavScreen.Ubicaciones.name)
}
