@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package mx.ipn.escom.trejol.proyectocomposemoviles.Ventanas

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.launch
import mx.ipn.escom.trejol.proyectocomposemoviles.R
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientProduct
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.ProductsResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.ui.theme.ProyectoComposeMovilesTheme
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.coroutineScope
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientCategory
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.category.CategoryResult
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.Data

@Composable
fun Catalogo(lyfecycleOwner: LifecycleOwner, productsCar: MutableState<List<Data>>){
    var productsState = remember { mutableStateOf<ProductsResponse?>(null) }
    var categorieState = remember { mutableStateOf<CategoryResult?>(null) }
    LaunchedEffect(Unit){
        lyfecycleOwner.lifecycleScope.launch {
            val service = RetrofitClientProduct.makeRetrofitService()
            val serviceCategories = RetrofitClientCategory.makeRetrofitService()
            categorieState.value = serviceCategories.getCategories()
            productsState.value = service.getProducts()
            println(productsState.value)
        }
    }

    when {
        productsState.value == null && categorieState.value == null -> {
            Text("Cargando...",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black)
        }
        else -> {
            Column {
                select(categorieState.value, lyfecycleOwner, productsState)
                cat(productsState.value, productsCar)
            }

        }
    }

}

@Composable
fun cat(products: ProductsResponse?, productsCar: MutableState<List<Data>>){

    val mContext = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        products?.data?.forEach {
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
                        Text(it.description)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Precio: $" + it.price.toString())
                        Row() {
                            Box() {
                                TextButton(onClick = {handleSelectProduct(it, mContext, productsCar)}) {
                                    Text("AGREGAR AL CARRITO")
                                }
                            }
                        }
                    }

                }
            }
        }


    }
}

private fun handleSelectProduct(product: Data, mContext: Context, productsCar: MutableState<List<Data>>) {

//    generamos el uuid
    var uuid = java.util.UUID.randomUUID().toString()
    println("UUID: " + uuid)
    product.uuid = uuid

//    agregamos el producto al carrito
    productsCar.value = productsCar.value + product
    Toast.makeText(mContext, "Agregando: " + product.name, Toast.LENGTH_SHORT).show()
}


suspend private fun handleSelectCategory(category: Number, products: MutableState<ProductsResponse?>?){
    coroutineScope {
        launch {
            val service = RetrofitClientProduct.makeRetrofitService()
            val prod = service.getProducts(category)
            products?.value = prod
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun select(categories: CategoryResult?, life: LifecycleOwner, products: MutableState<ProductsResponse?>?){
    println("Categorias: " + categories)
    var isExpanded by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("") }
    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {isExpanded = it}) {
        TextField(value = category, onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            }, colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth())
        ExposedDropdownMenu(expanded = isExpanded, modifier = Modifier
            .menuAnchor()
            .fillMaxWidth(), onDismissRequest = { isExpanded = false }) {
            DropdownMenuItem(text = {Text("Todos")}, onClick = {
                life.lifecycleScope.launch {
                    handleSelectCategory(0, products)
                }
                category = "Todos"
                isExpanded = false
            })
            categories?.data?.forEach {
                DropdownMenuItem(text = {
                    Text(it.name)
                },
                    onClick = {
                        life.lifecycleScope.launch {
                            handleSelectCategory(it.id, products)
                        }
                        category = it.name
                        isExpanded = false
                    })
            }
            
        }
    }
}



