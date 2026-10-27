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
import cl.duoc.vitairosm.viewmodel.CheckInViewModel

@Composable
fun CheckInScreen(
    viewModel: CheckInViewModel,
    onVolver: () -> Unit
) {
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
        "Sueno",
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
            text = "¿Como te sientes hoy?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000E2E)
        )

        Text(
            text = "Elige la opcion que mejor represente " +
                    "como te sientes en este momento.",
            color = Color(0xFF536477)
        )

        // Seleccion del estado de animo conectada al ViewModel
        estados.forEach { estado ->
            FilterChip(
                selected = viewModel.animo == estado,
                onClick = {
                    viewModel.actualizarAnimo(estado)
                    mostrarError = false
                },
                label = {
                    Text(estado)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (mostrarError) {
            Text(
                text = "Selecciona un estado de animo.",
                color = MaterialTheme.colorScheme.error
            )
        }

        HorizontalDivider()

        Text(
            text = "¿Con que lo relacionas? (opcional)",
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categorias.forEach { opcion ->
                FilterChip(
                    selected = viewModel.categoria == opcion,
                    onClick = {
                        val nuevaCat = if (viewModel.categoria == opcion) "" else opcion
                        viewModel.actualizarCategoria(nuevaCat)
                    },
                    label = { Text(opcion) }
                )
            }
        }

        OutlinedTextField(
            value = viewModel.nota,
            onValueChange = {
                if (it.length <= 200) viewModel.actualizarNota(it)
            },
            label = {
                Text("Nota personal (opcional)")
            },
            placeholder = {
                Text("Escribe algo si lo deseas...")
            },
            supportingText = {
                Text("${viewModel.nota.length}/200")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 4
        )

        Text(
            text = "Utiliza solamente datos ficticios " +
                    "para esta demostracion.",
            fontSize = 12.sp,
            color = Color(0xFF536477)
        )

        Button(
            onClick = {
                if (viewModel.animo.isEmpty()) {
                    mostrarError = true
                } else {
                    viewModel.guardarRegistro {
                        onVolver()
                    }
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