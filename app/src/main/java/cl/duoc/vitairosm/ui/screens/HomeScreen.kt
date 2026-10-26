
package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onVolver: () -> Unit,
    onRegistrar: () -> Unit,
    ultimoAnimo: String?,
    ultimaCategoria: String?,
    ultimaNota: String?
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8FB))
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = "Hola, Usuario Demo",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000E2E)
        )

        Text(
            text = "Tu espacio de bienestar",
            fontSize = 16.sp,
            color = Color(0xFF536477)
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¿Cómo te sientes hoy?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )


                Text(
                    text = if (ultimoAnimo == null) {
                        "Todavía no tienes registros en esta demostración."
                    } else {
                        "Último registro: $ultimoAnimo"
                    }
                )

                if (!ultimaCategoria.isNullOrBlank()) {
                    Text("Categoría: $ultimaCategoria")
                }

                if (!ultimaNota.isNullOrBlank()) {
                    Text("Nota: $ultimaNota")
                }

                Button(
                    onClick = onRegistrar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar mi ánimo")
                }

            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFDDF6F0)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Tu próximo paso",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000E2E)
                )

                Text(
                    text = "Dedica un momento a reconocer " +
                            "cómo te sientes hoy."
                )
            }
        }

        Text(
            text = "VITAIROS-M no reemplaza " +
                    "la atención profesional.",
            fontSize = 13.sp,
            color = Color(0xFF536477)
        )

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a bienvenida")
        }
    }
}
