package mx.ipn.escom.trejol.proyectocomposemoviles.navigationAdmin

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import mx.ipn.escom.trejol.proyectocomposemoviles.navigationAdmin.Items_menu_Admin.*

@Composable
fun NavInferiorAdmin(
    navController: NavHostController
){
    val menu_item_admin = listOf(
        Pantalla1Admin, Pantalla2Admin, Pantalla3Admin, Pantalla4Admin, Pantalla5Admin
    )

    BottomAppBar {
        NavigationBar {
            menu_item_admin.forEach {
                    item->
                val selected = currentRouteAdmin(navController) ==item.ruta
                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigate(item.ruta)},
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title)
                    },
                    label = {
                        Text(text = item.title, modifier = Modifier.size(30.dp))
                    }
                )
            }
        }
    }
}
