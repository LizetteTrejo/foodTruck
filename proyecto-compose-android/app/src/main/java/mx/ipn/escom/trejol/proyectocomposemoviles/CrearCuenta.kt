package mx.ipn.escom.trejol.proyectocomposemoviles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.google.gson.JsonParser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientAuth
import mx.ipn.escom.trejol.proyectocomposemoviles.ui.theme.ProyectoComposeMovilesTheme
import org.json.JSONObject
import retrofit2.HttpException

class CrearCuenta : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoComposeMovilesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Registro()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registro() {

    val loadingState = remember {
        mutableStateOf(false)
    }

    val lifecycleOwner = LocalContext.current as ComponentActivity

    val mContext = LocalContext.current
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text("Crea tu cuenta",
            color = Color.DarkGray,
            fontSize=40.sp
        )
        Spacer(modifier = Modifier.height(80.dp))

        var text2 by remember {
            mutableStateOf("")
        }
        var text3 by remember {
            mutableStateOf("")
        }
        var password by rememberPasswordState()

        Spacer(modifier = Modifier.height(40.dp))
        TextField(value = text2, onValueChange = {
                newText -> text2 = newText
        },
            label = {
                Text(text = "E-mail")
            },
            leadingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email icon"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(40.dp))
        TextField(value = text3, onValueChange = {
                newText -> text3 = newText
        },
            label = {
                Text(text = "Nombre")
            },
            leadingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Phone icon"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(40.dp))
        TextField(value = password, onValueChange = {
            password = it
        },
            label = {
                Text(text = "Contraseña")
            },
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Password icon"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(40.dp))

        if(loadingState.value) {
            CircularProgressIndicator(
                color = Color.Black
            )
        } else {
            Row(){
                OutlinedButton(onClick = {
                    lifecycleOwner.lifecycleScope.launch {
                        handleSignIn(text2, text3, password, mContext, loadingState)
                    }
                }) {
                    Text(
                        "Crear Cuenta",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }


    }

}

private suspend fun handleSignIn(email: String, name: String, password: String, mContext: Context, loadingState: MutableState<Boolean>) {
    loadingState.value = true

//    validamos que no esten vacios
    if(email.isEmpty() || password.isEmpty() || name.isEmpty()) {
//hacemos un Toast
        Toast.makeText(mContext, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show()
        loadingState.value = false
        return
    }

    // Utiliza el contexto del coroutineScope adecuadamente
    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientAuth.makeRetrofitService()

            try {
                val response = service.signUp(name, email, password)

                Toast.makeText(mContext, response.message, Toast.LENGTH_SHORT).show()
                loadingState.value = false
                mContext.startActivity(Intent(mContext,InicioSesion::class.java))

//                terminamos el activity
                (mContext as ComponentActivity).finish()


            } catch (e: HttpException) {
                var jsonResponse = e.response()?.errorBody()?.string()

                val Json = JSONObject(jsonResponse);
                val message = Json.getString("message")

                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

                loadingState.value = false
            }

        }
    }
}

