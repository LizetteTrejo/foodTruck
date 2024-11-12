package mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientOrders
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders.Data
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders.OrderDetailResult
import retrofit2.HttpException

@Composable
fun Pedidos(lifecycleOwner: LifecycleOwner){

    val mContext = LocalContext.current

    val orders = remember {
        mutableStateOf(listOf<Data>())
    }
    val orderDetail = remember {
        mutableStateOf(OrderDetailResult(null, false))
    }

    val showDialog = remember { mutableStateOf(false) }

    val sharedPref = mContext.getSharedPreferences(
        "mx.ipn.escom.trejol.proyectocomposemoviles",
        Context.MODE_PRIVATE
    )

    val token = sharedPref.getString("token", "")

    LaunchedEffect(Unit){
        lifecycleOwner.lifecycleScope.launch {
            if (token != null) {
                fetchOrders(token, orders)
            }
        }
    }


    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            orders.value.forEach {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable(
                            enabled = true,
                            onClick = {
                                print("hola")
                                lifecycleOwner.lifecycleScope.launch {
                                    if (token != null) {
                                        fetchOrderDetail(token, it.id, orderDetail, showDialog)
                                    }
                                }
                            }
                        ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)

                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Orden: ${it.order_number}",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Fecha: ${it.created_at}",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Total porductos : ${it.total_products}",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Total $: ${it.cost}",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Status : ${it.status_text}",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row {

                            if (it.status == 0) {

                                OutlinedButton(
                                    onClick = {

                                        lifecycleOwner.lifecycleScope.launch {
                                            if (token != null) {
                                                changeStatus(token, it.id, mContext, orders)
                                            }
                                        }

                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(8.dp)

                                ) {
                                    Text(
                                        text = "Cancelar",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row {


                                OutlinedButton(
                                    onClick = {

                                        lifecycleOwner.lifecycleScope.launch {
                                            if (token != null) {
                                               fetchOrderDetail(token, it.id, orderDetail, showDialog)
                                            }
                                        }

                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(8.dp)

                                ) {
                                    Text(
                                        text = "Ver detalle",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.Blue
                                    )
                                }

                        }

                        if(showDialog.value){
                            Dialog(onDismissRequest = { showDialog.value = false }) {

                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .background(Color.Gray)
                                ) {
                                    Text(
                                        text = "Detalle de la orden",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Fecha: ${orderDetail.value.data?.created_at}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Total porductos : ${orderDetail.value.data?.total_products}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Total $: ${orderDetail.value.data?.cost}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Status : ${orderDetail.value.data?.status_text}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Productos : ",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    orderDetail.value.data?.products?.forEach {
                                        Text(
                                            text = "Nombre: ${it.name}",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "Precio: ${it.price}",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
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


suspend private fun fetchOrders(token: String, orders: MutableState<List<Data>>) {
    coroutineScope {
        launch {
            val service = RetrofitClientOrders.makeRetrofitService()

            try {

                val prod = service.getOrders("Bearer $token")
                orders.value = prod.data

            } catch (e: HttpException) {
                println("Error: ${e.message}")
            }

        }
    }
}

suspend private fun changeStatus(token: String, orderId: Int, mContext: Context, orders: MutableState<List<Data>>){
    coroutineScope {
        launch {
            val service = RetrofitClientOrders.makeRetrofitService()

            try {

                val prod = service.changeStatus("Bearer $token", -1, orderId)
                Toast.makeText(
                    mContext,
                    prod.message,
                    Toast.LENGTH_SHORT
                ).show()

                fetchOrders(token, orders)
            } catch (e: HttpException) {
                println("Error: ${e.message}")
            }

        }
    }
}


suspend private fun fetchOrderDetail(token: String, orderId: Int, orderDetail: MutableState<OrderDetailResult>, showDialog: MutableState<Boolean>) {
    coroutineScope {
        launch {
            val service = RetrofitClientOrders.makeRetrofitService()

            try {

                val prod = service.getOrderDetail("Bearer $token", orderId)
                orderDetail.value = prod
                showDialog.value = true

            } catch (e: HttpException) {
                println("Error: ${e.message}")
            }

        }
    }
}
