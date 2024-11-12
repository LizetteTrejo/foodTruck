package mx.ipn.escom.trejol.proyectocomposemoviles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientDeliveryLocation
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientOrders
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.Data
import mx.ipn.escom.trejol.proyectocomposemoviles.ui.theme.ProyectoComposeMovilesTheme
import org.json.JSONObject
import retrofit2.HttpException

class Pago : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        if (intent.hasExtra("products")) {
            // Recupera el array de productos
            val productsList = intent.getParcelableArrayListExtra<Data>("products")

            if (productsList != null) {
                println("total: ${productsList.size}")

                // Convierte la lista a un array si es necesario
                val productsArray = productsList.toTypedArray()

                println("total: ${productsArray.size}")

                setContent {
                    ProyectoComposeMovilesTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            RealizarPedido(productsArray)
                        }
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealizarPedido(productsArray: Array<Data>) {
    val mContext = LocalContext.current
    val loadingState = remember {mutableStateOf(false)}
    val options = listOf("Efectivo", " Tarjeta")
    var seletedOption by remember { mutableStateOf(options[0]) }
    val lifecycleOwner = LocalContext.current as ComponentActivity
    val dataDeliveryLocation = remember { mutableStateOf<List<mx.ipn.escom.trejol.proyectocomposemoviles.services.models.deliveryLocation.Data>>(listOf()) }
    var isExpanded by remember { mutableStateOf(false) }
    var delivery by remember { mutableStateOf("") }
    val deliveryId = remember { mutableIntStateOf(0)}
    val selectType = remember { mutableStateOf(0) }
    LaunchedEffect(Unit){
        lifecycleOwner.lifecycleScope.launch {
            getDeliveryLocation(loadingState, mContext, dataDeliveryLocation)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
    ) {
            Text(
                text = "¿Cómo deseas pagar?",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        Spacer(modifier = Modifier.height(30.dp))
        options.forEach { option ->
            Row(

            modifier = Modifier.fillMaxWidth()
                .height(56.dp)
                .selectable(
                    selected = seletedOption == option,
                    onClick = {
                        seletedOption = option
                        if (option == "Efectivo") {
                            selectType.value = 1
                        } else {
                            selectType.value = 2
                        }
                    }

                )
            ){
            RadioButton(selected = seletedOption == option, onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Black,
                    unselectedColor = Color.White
                ))
            Text(text = option,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black)
            }

        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "¿En dónde será la entrega?",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(40.dp))
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {isExpanded = it}) {
            TextField(value = delivery, onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }, colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth())
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                dataDeliveryLocation.value.forEach {
                    DropdownMenuItem(text = {
                        Text(it.name)
                    },
                        onClick = {
                            delivery = it.name
                            deliveryId.value = it.id
                            isExpanded = false
                        })
                }

            }
        }


        Spacer(modifier = Modifier.height(40.dp))
        Row(){
            OutlinedButton(onClick = {
                lifecycleOwner.lifecycleScope.launch {
                    val sharedPref = mContext.getSharedPreferences(
                        "mx.ipn.escom.trejol.proyectocomposemoviles",
                        Context.MODE_PRIVATE
                    )

                    val token = sharedPref.getString("token", "")
                    if (token != null) {
                        print(token)
                        handlePayment("Bearer ${token}", productsArray, deliveryId.value, selectType.value, mContext, lifecycleOwner)
                    }

                }

            }) {
                Text("Pagar", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.ExtraBold)
            }

        }

    }

3
}

private suspend fun getDeliveryLocation(loadingState: MutableState<Boolean>, mContext: Context, dataDeliveryLocation: MutableState<List<mx.ipn.escom.trejol.proyectocomposemoviles.services.models.deliveryLocation.Data>>) {
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

suspend private fun handlePayment(token: String, productsArray: Array<Data>, deliveryId: Int, selectType: Int, mContext: Context, lifecycleOwner: ComponentActivity) {


    coroutineScope {
        launch {
            val service = RetrofitClientOrders.makeRetrofitService()

            try {
//              convertimos el prodctsArray en json
                val jsonProducts = productsArray.map {
                    it.id
                }
                println("token: ${token}")
                val prod = service.createOrder(token, jsonProducts.toString(), deliveryId, selectType)
                Toast.makeText(mContext, prod.message, Toast.LENGTH_SHORT).show()
                mContext.startActivity(Intent(mContext,PagoRealizado::class.java))

//                    matamos la actividad
                lifecycleOwner.finish()
            } catch (e: HttpException) {
                println("Error: ${e.message}")
            }

        }
    }
}