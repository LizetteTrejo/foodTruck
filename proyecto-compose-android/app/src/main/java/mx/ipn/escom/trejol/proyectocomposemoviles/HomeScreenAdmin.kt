package mx.ipn.escom.trejol.proyectocomposemoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import mx.ipn.escom.trejol.proyectocomposemoviles.navigationAdmin.NavigationHostAdmin
import mx.ipn.escom.trejol.proyectocomposemoviles.navigationAdmin.NavInferiorAdmin
import mx.ipn.escom.trejol.proyectocomposemoviles.ui.theme.ProyectoComposeMovilesTheme

class HomeScreenAdmin : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoComposeMovilesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenAdmin()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenAdmin() {
    Text(
        "Entrar",
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.ExtraBold
    )
    val navController = rememberNavController()
    Scaffold (
        bottomBar = {
            NavInferiorAdmin(navController)
        }
    ){
            padding->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){
            NavigationHostAdmin(navController = navController)
        }
    }
}