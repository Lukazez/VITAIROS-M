package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Recurso(val titulo: String, val descripcion: String, val categoria: String)

@Composable
fun RecursosScreen(
    onVolver: () -> Unit
) {
    val listaRecursos = listOf(
        Recurso("Respiracion consciente", "Dedica 5 minutos al dia a inhalar profundamente y exhalar con calma para reducir la ansiedad.", "Ansiedad"),
        Recurso("Higiene del sueno", "Evita pantallas una hora antes de dormir y manten un horario constante para descansar mejor.", "Descanso"),
        Recurso("Pausas activas", "Estira tus brazos y piernas cada dos horas si pasas mucho tiempo estudiando o trabajando frente al computador.", "Bienestar"),
        Recurso("Expresa lo que sientes", "Escribir tus emociones en un registro o hablar con alguien de confianza ayuda a liberar carga mental.", "Emocional")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8FB))
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Biblioteca de recursos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000E2E)
                )
                TextButton(onClick = onVolver) {
                    Text("Volver")
                }
            }

            Text(
                text = "Guias y consejos rapidos para tu bienestar mental",
                fontSize = 14.sp,
                color = Color(0xFF536477)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaRecursos) { recurso ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE8F1F5)
                            ) {
                                Text(
                                    text = recurso.categoria,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = Color(0xFF007F9E),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = recurso.titulo,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF000E2E)
                            )
                            Text(
                                text = recurso.descripcion,
                                fontSize = 14.sp,
                                color = Color(0xFF536477)
                            )
                        }
                    }
                }
            }
        }
    }
}