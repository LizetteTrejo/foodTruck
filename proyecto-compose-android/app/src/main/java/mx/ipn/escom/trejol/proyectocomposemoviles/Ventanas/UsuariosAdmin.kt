@file:OptIn(ExperimentalMaterial3Api::class)

package mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mx.ipn.escom.trejol.proyectocomposemoviles.R
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientAuth
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientCategory
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.AuthResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.UserResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.category.CategoryResult
import org.json.JSONObject
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosAdmin(lifecycleOwner: LifecycleOwner) {
    val loadingState = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val last_name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var userState = remember { mutableStateOf<UserResponse?>(null) }

    val mContext = LocalContext.current

    LaunchedEffect(Unit){
        lifecycleOwner.lifecycleScope.launch {
            getUsers(loadingState, mContext, userState)
        }
    }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){

            if(userState.value == null){
                Text(
                    "No hay usuarios registrados",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
            } else {
                Column() {
                    userState.value!!.data.forEach {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable { },
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Row(){
                                        Text(
                                            it.name +  " " + it.last_name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("Usuario: " + it.email)
                                }

                            }
                        }
                    }


                }
            }

        }
        //    creamos un boton flotante para agregar una nueva ubicacion, lo situamos en la parte inferior derecha
        FloatingActionButton(
            onClick = {
                name.value = ""
                last_name.value = ""
                email.value = ""
                password.value = ""
                showDialog.value = true
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Crear Cliente",
                tint = Color.White
            )
        }
    }

//    creamos un dialog para agregar una nueva ubicacion
    if(showDialog.value){
        Dialog(onDismissRequest = {
            showDialog.value = false
        }) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                    .padding(16.dp)

            ) {

                TextField(
                    value = name.value,
                    onValueChange = {
                            newText -> name.value = newText
                    },
                    label = {
                        Text(text = "Nombre")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                TextField(
                    value = last_name.value,
                    onValueChange = {
                            newText -> last_name.value = newText
                    },
                    label = {
                        Text(text = "Apellidos")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                TextField(
                    value = email.value,
                    onValueChange = {
                            newText -> email.value = newText.filter { it.isDigit() }
                    },
                    label = {
                        Text(text = "Usuario")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                TextField(
                    value = password.value,
                    onValueChange = {
                            newText -> password.value = newText
                    },
                    label = {
                        Text(text = "Contraseña")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){

                    if(loadingState.value) {

                        CircularProgressIndicator(
                            color = Color.Black
                        )

                    } else {
                        OutlinedButton(onClick = {
                            lifecycleOwner.lifecycleScope.launch {
                                handleBtnAdd(name.value, last_name.value, email.value, password.value, mContext, loadingState, showDialog, userState)
                                name.value = ""
                                last_name.value = ""
                                email.value = ""
                                password.value = ""

                            }
                        }) {
                            Text(
                                "Crear",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }


                }
            }
        }
    }
}

private suspend fun getUsers(loadingState: MutableState<Boolean>, mContext: Context, dataUsers: MutableState<UserResponse?>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientAuth.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.getUsers()

                dataUsers.value = response

                loadingState.value = false

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

private suspend fun handleBtnAdd(name: String, lastName: String, email: String, password:String, mContext: Context, loadingState: MutableState<Boolean>, showDialog: MutableState<Boolean>, dataCategory: MutableState<UserResponse?>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientAuth.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.signUp(name, email,password, lastName)

                getUsers(loadingState, mContext, dataCategory)

                Toast.makeText(mContext, response.message, Toast.LENGTH_SHORT).show()

                loadingState.value = false
                showDialog.value = false

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