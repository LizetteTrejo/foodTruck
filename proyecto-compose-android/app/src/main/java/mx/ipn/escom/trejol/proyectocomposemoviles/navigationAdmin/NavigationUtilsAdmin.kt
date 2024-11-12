package mx.ipn.escom.trejol.proyectocomposemoviles.navigationAdmin

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun currentRouteAdmin(navController: NavController): String? =
    navController.currentBackStackEntryAsState().value?.destination?.route