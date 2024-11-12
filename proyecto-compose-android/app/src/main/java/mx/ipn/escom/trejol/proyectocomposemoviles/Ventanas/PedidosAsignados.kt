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
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientOrders
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.UserResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders.Order
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders.OrderDetailResult
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders.OrderResponse
import org.json.JSONObject
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosAsignados(lifecycleOwner: LifecycleOwner) {
    val loadingState = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    var orderState = remember { mutableStateOf<OrderResponse?>(null) }
    val detailOrder = remember { mutableStateOf<OrderDetailResult?>(null)}

    val mContext = LocalContext.current

    LaunchedEffect(Unit){
        lifecycleOwner.lifecycleScope.launch {
            getOrders(loadingState, mContext, orderState)
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

            if(orderState.value == null){
                Text(
                    "No hay pedidos asignados",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
            } else {
                Column() {
                    orderState.value!!.data.forEach {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable {
                                    showDialog.value = true
                                    lifecycleOwner.lifecycleScope.launch {
                                        getDetailOrder(loadingState, mContext, detailOrder, it.id)
                                    }
                                },
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
                                            "Numero de orden: " + it.order_number,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                        )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("Tipo de Pago: " + if(it.type_delivery == 1) "Efectivo" else "Tarjeta")
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("Numero de productos: " + it.total_products.toString())
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("Total: $" + it.cost.toString())
                                }

                            }
                        }
                    }


                }
            }

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
                Text(
                    "Numero de orden: " + detailOrder.value?.data?.order_number,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Tipo de pago: " + if(detailOrder.value?.data?.type_delivery == 1) "Efectivo" else "Tarjeta", color = Color.Black)
                Spacer(modifier = Modifier.height(10.dp))
                detailOrder.value?.data?.products?.forEach{
                    Row {
                        Text(it.name + "  $" + it.price.toString() + ", ", color = Color.Black)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Total: $" + detailOrder.value?.data?.cost.toString() , color = Color.Black, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

private suspend fun getOrders(loadingState: MutableState<Boolean>, mContext: Context, dataOrders: MutableState<OrderResponse?>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientOrders.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.getOrders("Bearer $token", "true")

                dataOrders.value = response

                loadingState.value = false

            } catch (e: HttpException) {
                loadingState.value = false
            }

        }
    }
}

private suspend fun getDetailOrder(loadingState: MutableState<Boolean>, mContext: Context, dataOrders: MutableState<OrderDetailResult?>, orderActive: Int) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientOrders.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.getOrderDetail("Bearer $token", orderActive)

                dataOrders.value = response

                loadingState.value = false

            } catch (e: HttpException) {
                loadingState.value = false
            }

        }
    }
}

