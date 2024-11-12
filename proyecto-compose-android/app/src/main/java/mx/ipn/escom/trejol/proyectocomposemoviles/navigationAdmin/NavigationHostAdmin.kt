package mx.ipn.escom.trejol.proyectocomposemoviles.navigationAdmin

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.CategoriasAdmin
import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.PedidosAdmin
import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.PedidosAsignados
import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.ProductosAdmin
import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.UsuariosAdmin
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.Data

@Composable
fun NavigationHostAdmin(navController: NavHostController){
    val lifecycleOwner = LocalContext.current as ComponentActivity

    var productsCar = remember { mutableStateOf<List<Data>>(listOf()) }

    NavHost(navController = navController,
        startDestination = NavScreenAdmin.UsuariosAdmin.name,
    ){
        composable(NavScreenAdmin.UsuariosAdmin.name){
            UsuariosAdmin(lifecycleOwner)
        }
        composable(NavScreenAdmin.ProductosAdmin.name){
            ProductosAdmin(lifecycleOwner)
        }
        composable(NavScreenAdmin.CategoriasAdmin.name){
            CategoriasAdmin(lifecycleOwner)
        }
        composable(NavScreenAdmin.PedidosAdmin.name){
            PedidosAdmin(lifecycleOwner)
        }
        composable(NavScreenAdmin.PedidosAsignados.name){
            PedidosAsignados(lifecycleOwner)
        }
    }
}