
package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckInScreen(
    onVolver: () -> Unit,
    onGuardar: (String, String, String) -> Unit
) {
    var animo by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var categoria by rememberSaveable {
        mutableStateOf("")
    }

    var nota by rememberSaveable {
        mutableStateOf("")
    }

    var mostrarError by rememberSaveable {
        mutableStateOf(false)
    }

    val estados = listOf(
        "Muy desanimado",
        "Desanimado",
        "Neutral",
        "Bien",
        "Muy bien"
    )

    val categorias = listOf(
        "Estudios",
        "Trabajo",
        "Relaciones",
        "Familia",
        "Sueño",
        "Bienestar",
        "Otro"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8FB))
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        TextButton(onClick = onVolver) {
            Text("Volver")
        }

        Text(
            text = "¿Cómo te sientes hoy?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000E2E)
        )

        Text(
            text = "Elige la opción que mejor represente " +
                    "cómo te sientes en este momento.",
            color = Color(0xFF536477)
        )

        // Selección del estado de ánimo
        estados.forEach { estado ->
            FilterChip(
                selected = animo == estado,
                onClick = {
                    animo = estado
                    mostrarError = false
                },
                label = {
                    Text(estado)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Mensaje de validación
        if (mostrarError) {
            Text(
                text = "Selecciona un estado de ánimo.",
                color = MaterialTheme.colorScheme.error
            )
        }

        HorizontalDivider()

        Text(
            text = "¿Con qué lo relacionas? (opcional)",
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categorias.forEach { opcion ->
                FilterChip(
                    selected = categoria == opcion,
                    onClick = {
                        categoria =
                            if (categoria == opcion) ""
                            else opcion
                    },
                    label = { Text(opcion) }
                )
            }
        }

        // Nota personal opcional
        OutlinedTextField(
            value = nota,
            onValueChange = {
                if (it.length <= 200) nota = it
            },
            label = {
                Text("Nota personal (opcional)")
            },
            placeholder = {
                Text("Escribe algo si lo deseas...")
            },
            supportingText = {
                Text("${nota.length}/200")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 4
        )

        Text(
            text = "Utiliza solamente datos ficticios " +
                    "para esta demostración.",
            fontSize = 12.sp,
            color = Color(0xFF536477)
        )

        Button(
            onClick = {
                val seleccionado = animo

                if (seleccionado == null) {
                    mostrarError = true
                } else {
                    onGuardar(
                        seleccionado,
                        categoria,
                        nota
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF007F9E)
            )
        ) {
            Text("Guardar registro")
        }
    }
}
