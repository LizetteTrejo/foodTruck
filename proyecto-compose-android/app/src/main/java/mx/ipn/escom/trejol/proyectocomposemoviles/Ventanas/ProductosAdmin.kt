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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientCategory
import mx.ipn.escom.trejol.proyectocomposemoviles.services.RetrofitClientProduct
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.category.CategoryResult
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.ProductsResponse
import org.json.JSONObject
import retrofit2.HttpException

@Composable
fun ProductosAdmin(lifecycleOwner: LifecycleOwner) {
    val loadingState = remember { mutableStateOf(false) }
    val isEditing = remember { mutableStateOf(false) }
    val showDialogDelete = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val productActive = remember { mutableIntStateOf(0) }

    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("0") }
    var categorieState = remember { mutableStateOf<CategoryResult?>(null) }
    var productState = remember { mutableStateOf<ProductsResponse?>(null) }
    var isExpanded by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("") }
    val categoryId = remember { mutableIntStateOf(0) }

    val mContext = LocalContext.current

    LaunchedEffect(Unit){
        lifecycleOwner.lifecycleScope.launch {
            getProducts(loadingState, mContext, productState)
            getCategories(loadingState, mContext, categorieState)
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

            if(productState.value == null){
                Text(
                    "No tienes productos registradas",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
            } else {
                Column() {
                    productState.value!!.data.forEach {
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
                                    Text("Precio: " + it.price.toString() )
                                    Row() {
                                        Box() {
                                            TextButton(onClick = {showDialog.value = true
                                                name.value = it.name
                                                description.value = it.description
                                                price.value = it.price.toString()
                                                productActive.value = it.id
                                                isEditing.value = true}) {
                                                Text("Editar")
                                            }
                                        }
                                        Box() {
                                            TextButton(onClick = {showDialogDelete.value = true
                                                productActive.value = it.id}) {
                                                Text("Eliminar")
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
                name.value = ""
                description.value = ""
                price.value = "0"
                isEditing.value = false
                showDialog.value = true
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Agregar Producto",
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
                    value = description.value,
                    onValueChange = {
                            newText -> description.value = newText
                    },
                    label = {
                        Text(text = "Descripcion")
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                TextField(
                    value = price.value,
                    onValueChange = {
                            newText -> price.value = newText.filter { it.isDigit() }
                    },

                    label = {
                        Text(text = "Price")
                    }
                )
                Text(text = "Categoria", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {isExpanded = it}) {
                    TextField(value = category, onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        }, colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth())
                    ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                        categorieState.value?.data?.forEach {
                            DropdownMenuItem(text = {
                                Text(it.name)
                            },
                                onClick = {
                                    category = it.name
                                    categoryId.value = it.id
                                })
                        }

                    }
                }

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
                                if (isEditing.value){
                                    handleBtnEdit(productActive.value, name.value, description.value, price.value, categoryId.value, mContext, loadingState, showDialog, productState)
                                    name.value = ""
                                    description.value = ""
                                    price.value = "0"
                                }else{
                                    handleBtnAdd(name.value, description.value, price.value, categoryId.value, mContext, loadingState, showDialog, productState)
                                    name.value = ""
                                    description.value = ""
                                    price.value = "0"
                                }
                            }
                        }) {
                            Text(
                                if (isEditing.value) "Editar" else "Agregar",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }


                }
            }
        }
    }

    if(showDialogDelete.value) {
        Dialog(onDismissRequest = {
            showDialogDelete.value = false
        }) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                    .padding(16.dp)

            ) {

                Text(
                    "¿Estas seguro de eliminar este producto?",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    if (loadingState.value) {

                        CircularProgressIndicator(
                            color = Color.Black
                        )

                    } else {
                        Row(){
                            OutlinedButton(onClick = {
                                lifecycleOwner.lifecycleScope.launch {
                                    handleBtnDelete(productActive.value, mContext, loadingState, showDialogDelete, productState)
                                }
                            }) {
                                Text(
                                    "Eliminar",
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedButton(onClick = {
                                showDialogDelete.value = false
                            }) {
                                Text(
                                    "Cancelar",
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
}

private suspend fun handleBtnDelete(id: Number, mContext: Context, loadingState: MutableState<Boolean>, showDialog: MutableState<Boolean>, dataProduct: MutableState<ProductsResponse?>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientProduct.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.changeStatusProduct("Bearer ${token}", id)

                getProducts(loadingState, mContext, dataProduct)

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


private suspend fun handleBtnEdit(id: Number, name: String, description: String, price:String, idCategoria: Number, mContext: Context, loadingState: MutableState<Boolean>, showDialog: MutableState<Boolean>, dataProducts: MutableState<ProductsResponse?>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientProduct.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.updateProduct("Bearer ${token}", name, description, price.toDouble(), idCategoria, id)

                getProducts(loadingState, mContext, dataProducts)

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



private suspend fun handleBtnAdd(name: String, description: String, price: String, categoryId: Number, mContext: Context, loadingState: MutableState<Boolean>, showDialog: MutableState<Boolean>, dataProduct: MutableState<ProductsResponse?>) {
    loadingState.value = true
    println(name)
    println(description)
    println(price)
    println(categoryId)
    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientProduct.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.addProduct("Bearer ${token}", name, description, price.toDouble(), categoryId)

                getProducts(loadingState, mContext, dataProduct)

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





private suspend fun getCategories(loadingState: MutableState<Boolean>, mContext: Context, dataCategory: MutableState<CategoryResult?>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientCategory.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.getCategories()

                dataCategory.value = response

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

private suspend fun getProducts(loadingState: MutableState<Boolean>, mContext: Context, dataProduct: MutableState<ProductsResponse?>) {
    loadingState.value = true

    coroutineScope {
        // Lanzar el trabajo en un contexto coroutine
        launch {
            val service = RetrofitClientProduct.makeRetrofitService()

            try {
                val sharedPref = mContext.getSharedPreferences(
                    "mx.ipn.escom.trejol.proyectocomposemoviles",
                    Context.MODE_PRIVATE
                )
                val token = sharedPref.getString("token", "")
                val response = service.getProducts()

                dataProduct.value = response

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