package mx.ipn.escom.trejol.proyectocomposemoviles.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import mx.ipn.escom.trejol.proyectocomposemoviles.navigation.Items_menu.*

@Composable
fun NavInferior(
    navController: NavHostController
){
    val menu_item = listOf(
        Pantalla1, Pantalla2, Pantalla3, Pantalla4
    )

    BottomAppBar {
        NavigationBar {
            menu_item.forEach {
                item->
                val selected = currentRoute(navController)==item.ruta
                NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.ruta)},
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title)
                },
                    label = {
                        Text(text = item.title)
                    }
                )
            }
        }
    }
}

