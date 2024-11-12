package mx.ipn.escom.trejol.proyectocomposemoviles.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.Carrito
import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.Pedidos
import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.Catalogo
import mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas.Ubicaciones
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.Data

@Composable
fun NavigationHost(navController: NavHostController){
    val lifecycleOwner = LocalContext.current as ComponentActivity

    var productsCar = remember { mutableStateOf<List<Data>>(listOf()) }

    NavHost(navController = navController,
        startDestination = NavScreen.Catalogo.name,
        ){
            composable(NavScreen.Catalogo.name){
                Catalogo(lifecycleOwner, productsCar)
            }
            composable(NavScreen.Carrito.name){
                Carrito(productsCar)
            }
            composable(NavScreen.Pedidos.name){
                Pedidos(lifecycleOwner)
            }
            composable(NavScreen.Ubicaciones.name){
                Ubicaciones(lifecycleOwner)
            }
        }
}