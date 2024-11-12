package mx.ipn.escom.trejol.proyectocomposemoviles.navigationAdmin

import mx.ipn.escom.trejol.proyectocomposemoviles.R

sealed class Items_menu_Admin(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    object Pantalla1Admin: Items_menu_Admin(
        R.drawable.usuariosadmin,
        "Usuarios", NavScreenAdmin.UsuariosAdmin.name)
    object Pantalla2Admin: Items_menu_Admin(
        R.drawable.cart,
        "Productos", NavScreenAdmin.ProductosAdmin.name)
    object Pantalla3Admin: Items_menu_Admin(
        R.drawable.categoriasadmin,
        "Categorias", NavScreenAdmin.CategoriasAdmin.name)
    object Pantalla4Admin: Items_menu_Admin(
        R.drawable.pedidos,
        "Pedidos", NavScreenAdmin.PedidosAdmin.name)
    object Pantalla5Admin: Items_menu_Admin(
        R.drawable.pedidos,
        "Pedidos Asignados", NavScreenAdmin.PedidosAsignados.name)
}
