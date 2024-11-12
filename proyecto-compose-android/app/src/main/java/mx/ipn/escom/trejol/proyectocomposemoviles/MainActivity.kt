package mx.ipn.escom.trejol.proyectocomposemoviles

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientCategory
import mx.ipn.escom.trejol.proyectocomposemoviles.ui.theme.ProyectoComposeMovilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            ProyectoComposeMovilesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaInicio()
                }
            }
        }
    }
}

@Composable

fun PantallaInicio(){
    val mContext = LocalContext.current

    val sharedPref = mContext.getSharedPreferences(
        "mx.ipn.escom.trejol.proyectocomposemoviles",
        Context.MODE_PRIVATE
    )
    val token = sharedPref.getString("token", "")

    //if(token != ""){
//        cambiamos a la activity de inicio
      //  mContext.startActivity(Intent(mContext, HomeScreen::class.java))

//        cerramos la activity
        //ActivityCompat.finishAffinity(mContext as ComponentActivity)
    //}

    Column(
        modifier =Modifier.fillMaxSize(),
        horizontalAlignment=Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
                .border(6.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ft),
                contentDescription = "FoodTruck",
                modifier = Modifier.padding((25.dp))
            )
        }
        Text("Food-Truck",
            color = Color.DarkGray,
            fontSize=40.sp
        )
        Text("Los Ah bb",
            color = Color.DarkGray,
            fontSize=40.sp
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text("¡Bienvenido!",
            color = Color.Black,
            fontSize=30.sp
            )
        Spacer(modifier = Modifier.height(80.dp))
            Row(){
                OutlinedButton(onClick = { mContext.startActivity(Intent(mContext,InicioSesion::class.java)) }) {
                Text("Ingresar", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(modifier = Modifier.width(20.dp))
                OutlinedButton(onClick = { mContext.startActivity(Intent(mContext,CrearCuenta::class.java))}) {
                    Text("Crear Cuenta", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.ExtraBold)
                }
            }
    }

}

@Preview(showBackground = true)
@Composable
fun PantallaPreview(){
    ProyectoComposeMovilesTheme{
        PantallaInicio()
    }
}
