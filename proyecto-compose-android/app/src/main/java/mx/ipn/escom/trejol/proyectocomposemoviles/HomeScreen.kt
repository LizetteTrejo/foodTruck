package mx.ipn.escom.trejol.proyectocomposemoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import mx.ipn.escom.trejol.proyectocomposemoviles.navigation.NavInferior
import mx.ipn.escom.trejol.proyectocomposemoviles.navigation.NavigationHost
import mx.ipn.escom.trejol.proyectocomposemoviles.ui.theme.ProyectoComposeMovilesTheme


class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoComposeMovilesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen1()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen1() {
    Text(
        "Entrar",
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.ExtraBold
    )
    val navController = rememberNavController()
    Scaffold (
        bottomBar = {
            NavInferior(navController)
        }
    ){
        padding->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){
            NavigationHost(navController = navController)
        }
    }
}
