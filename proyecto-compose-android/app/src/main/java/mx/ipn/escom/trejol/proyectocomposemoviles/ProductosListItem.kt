package mx.ipn.escom.trejol.proyectocomposemoviles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import mx.ipn.escom.trejol.proyectocomposemoviles.datos.Productos

@Composable
fun ProductosListItem(producto: Productos){
    Row{
        Column{
            Text(text = producto.title, style = typography.bodyLarge)
            Text(text = "View Detail", style = typography.bodySmall)
        }
    }
}