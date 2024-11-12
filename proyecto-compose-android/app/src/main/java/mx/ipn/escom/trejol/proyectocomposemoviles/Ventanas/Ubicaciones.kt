package mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
//import androidx.compose.ui.tooling.data.EmptyGroup.name
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mx.ipn.escom.trejol.proyectocomposemoviles.InicioSesion
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientAuth
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientCategory
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientDeliveryLocation
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientProduct
import org.json.JSONObject
import retrofit2.HttpException
import mx.ipn.escom.trejol.proyectocomposemoviles.R
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.category.CategoryResult
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.deliveryLocation.Data
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.deliveryLocation.DeliveryLocationResult
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.ProductsResponse


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ubicaciones(lifecycleOwner: LifecycleOwner) {

    val loadingState = remember {mutableStateOf(false)}
    val showDialog = remember { mutableStateOf(false) }

    val alias = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val reference = remember { mutableStateOf("") }
    val deliveryActive = remember { mutableIntStateOf(0) }
    val isEditing = remember { mutableStateOf(false) }

    val dataDeliveryLocation = remember { mutableStateOf<List<Data>>(listOf()) }

    val mContext = LocalContext.current

    LaunchedEffect(Unit){
        lifecycleOwner.lifecycleScope.launch {
            getDeliveryLocation(loadingState, mContext, dataDeliveryLocation)
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

            if(dataDeliveryLocation.value.isEmpty()){
                Text(
                    "No tienes ubicaciones registradas",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
            } else {
                Column() {
                    dataDeliveryLocation.value.forEach {
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
                                    Text(
                                        it.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(it.address)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(it.city)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row() {
                                        Box() {
                                            TextButton(onClick = {showDialog.value = true
                                                alias.value = it.name
                                                address.value = it.address
                                                reference.value = it.city
                                                deliveryActive.value = it.id
                                                isEditing.value = true}) {
                                                Text("Editar")
                                            }
                                        }
                                    }
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
                alias.value = ""
                address.value = ""
                reference.value = ""
                isEditing.value = false
                showDialog.value = true
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Agregar ubicacion",
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
                    value = alias.value,
                    onValueChange = {
                            newText -> alias.value = newText
                    },
                    label = {
                        Text(text = "Alias")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                TextField(
                    value = address.value,
                    onValueChange = {
                            newText -> address.value = newText
                    },
                    label = {
                        Text(text = "Direccion")
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                TextField(
                    value = reference.value,
                    onValueChange = {
                            newText -> reference.value = newText
                    },
                    label = {
                        Text(text = "Referencia")
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
                                if(isEditing.value){
                                    handleBtnEdit(deliveryActive.value, alias.value, address.value, reference.value, mContext, loadingState, showDialog, dataDeliveryLocation)
                                    alias.value = ""
                                    address.value = ""
                                    reference.value = ""
                                    isEditing.value = false
                                } else {
                                    handleBtnAdd(alias.value, address.value, reference.value, mContext, loadingState, showDialog, dataDeliveryLocation)
                                    alias.value = ""
                                    address.value = ""
                                    reference.value = ""
                                }

                            }
                        }) {
                            Text(
                                if(isEditing.value) "Editar" else "Agregar",
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

private suspend fun handleBtnEdit(id: Number, alias: String, address: String, reference: String, mContext: Context, loadingState: MutableState<Boolean>, showDialog: MutableState<Boolean>, dataDeliveryLocation: MutableState<List<Data>>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientDeliveryLocation.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.updateDeliveryLocation("Bearer ${token}", alias, address, reference, "sdf", "sdf", id)

                getDeliveryLocation(loadingState, mContext, dataDeliveryLocation)

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

private suspend fun handleBtnAdd(alias: String, address: String, reference: String, mContext: Context, loadingState: MutableState<Boolean>, showDialog: MutableState<Boolean>, dataDeliveryLocation: MutableState<List<Data>>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientDeliveryLocation.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.addDeliveryLocation("Bearer ${token}", alias, address, reference, "sdf", "sdf")

                getDeliveryLocation(loadingState, mContext, dataDeliveryLocation)

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


private suspend fun getDeliveryLocation(loadingState: MutableState<Boolean>, mContext: Context, dataDeliveryLocation: MutableState<List<Data>>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientDeliveryLocation.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.getDeliveryLocation("Bearer ${token}")

                dataDeliveryLocation.value = response.data

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