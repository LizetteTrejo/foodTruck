package mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas

import android.content.Intent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mx.ipn.escom.trejol.proyectocomposemoviles.Pago
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.Data

@Composable
fun Carrito(productsCar: MutableState<List<Data>>){
    val mContext = LocalContext.current

//    hacemos el total
    val total = productsCar.value!!.sumOf { it.price }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            productsCar.value!!.forEach {
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
                            Text(it.description.toString())
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Precio: $" + it.price.toString() )
                            Row() {

                                Box() {
                                    TextButton(onClick = {
//                                        eliminamos el producto
                                        println("Eliminando producto")
                                        val uuid = it.uuid
                                        productsCar.value = productsCar.value.filter { it.uuid != uuid }
                                    }) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }

                    }
                }
            }


            if(productsCar.value.size > 0) {

                Column {
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "Total productos: " + productsCar.value.size.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Total: $" + total.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    Box() {
                        OutlinedButton(onClick = {
//                            mandamos productos al pago

                            val arrayListProducts = ArrayList<Any>()

                            productsCar.value.forEach {
                                arrayListProducts.add(it)
                            }

                            val intent = Intent(mContext, Pago::class.java)
                            intent.putExtra("products",arrayListProducts)
                            mContext.startActivity(intent)
                        }) {
                            Text("PAGAR",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black)
                        }

                    }
                }

            } else {
                Box() {

                        Text("No hay productos",  style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black)

                }
            }
        }
    }
}