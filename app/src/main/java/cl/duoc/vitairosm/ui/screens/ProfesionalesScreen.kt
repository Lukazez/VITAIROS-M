package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Profesional(val nombre: String, val especialidad: String, val disponibilidad: String)

@Composable
fun ProfesionalesScreen(
    onVolver: () -> Unit
) {
    val listaProfesionales = listOf(
        Profesional("Dra. Maria Gomez", "Psicologia Clinica", "Lunes a Viernes - 10:00 hrs"),
        Profesional("Lic. Carlos Perez", "Orientacion Emocional", "Martes y Jueves - 15:00 hrs"),
        Profesional("Dra. Ana Fernandez", "Bienestar y Mindfulness", "Lunes, Miercoles y Viernes - 11:00 hrs")
    )

    var profesionalSeleccionado by remember { mutableStateOf<Profesional?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

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
                    text = "Catalogo de profesionales",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000E2E)
                )
                TextButton(onClick = onVolver) {
                    Text("Volver")
                }
            }

            Text(
                text = "Selecciona un especialista para simular el agendamiento de una cita de apoyo.",
                fontSize = 14.sp,
                color = Color(0xFF536477)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaProfesionales) { profesional ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = profesional.nombre,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF000E2E)
                            )
                            Text(
                                text = "Especialidad: ${profesional.especialidad}",
                                fontSize = 14.sp,
                                color = Color(0xFF007F9E),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Disponibilidad: ${profesional.disponibilidad}",
                                fontSize = 13.sp,
                                color = Color(0xFF536477)
                            )
                            Button(
                                onClick = {
                                    profesionalSeleccionado = profesional
                                    mostrarConfirmacion = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Agendar cita")
                            }
                        }
                    }
                }
            }
        }

        if (mostrarConfirmacion && profesionalSeleccionado != null) {
            AlertDialog(
                onDismissRequest = { mostrarConfirmacion = false },
                title = { Text("Cita agendada con exito") },
                text = { Text("Has solicitado una cita con ${profesionalSeleccionado?.nombre} (${profesionalSeleccionado?.especialidad}). Te contactaremos pronto.") },
                confirmButton = {
                    TextButton(
                        onClick = { mostrarConfirmacion = false }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}