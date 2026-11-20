package cl.duoc.vitairosm.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.vitairosm.R
import cl.duoc.vitairosm.viewmodel.CheckInValidation
import cl.duoc.vitairosm.viewmodel.CheckInViewModel

private fun colorIndicadorAnimo(estado: String): Color {
    return when (estado.lowercase()) {
        "muy desanimado" -> Color(0xFFD96B6B)
        "desanimado" -> Color(0xFFE09F5A)
        "neutral" -> Color(0xFF6B8E9B)
        "bien" -> Color(0xFF007F9E)
        "muy bien" -> Color(0xFF1F9D7A)
        else -> Color(0xFF005B70)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckInScreen(viewModel: CheckInViewModel, onVolver: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8FB))
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(onClick = onVolver, enabled = !viewModel.guardando) {
            Text("Volver", color = Color(0xFF007F9E))
        }

        Text(
            text = "Registro de emociones",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000E2E)
        )
        Text(
            text = "Elige como te sientes hoy. La categoria y la nota son opcionales.",
            color = Color(0xFF536477)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE1ECF2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Selecciona tu estado",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF000E2E)
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CheckInValidation.animos.forEach { estado ->
                        val seleccionado = viewModel.animo == estado
                        val tono = colorIndicadorAnimo(estado)

                        Surface(
                            modifier = Modifier
                                .alpha(if (viewModel.guardando) 0.6f else 1f)
                                .clickable(enabled = !viewModel.guardando) {
                                    viewModel.actualizarAnimo(estado)
                                },
                            shape = RoundedCornerShape(14.dp),
                            color = if (seleccionado) tono.copy(alpha = 0.14f) else Color(0xFFF2F8FB),
                            border = BorderStroke(
                                width = if (seleccionado) 2.dp else 1.dp,
                                color = if (seleccionado) tono else Color(0xFFD6E5EC)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(tono, CircleShape)
                                )
                                Text(
                                    text = estado,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                                    color = Color(0xFF000E2E)
                                )
                                if (seleccionado) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_check),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = tono
                                    )
                                }
                            }
                        }
                    }
                }

                FormFeedback(viewModel.errorAnimo)

                HorizontalDivider(color = Color(0xFFE1ECF2))

                Text(
                    text = "¿Con que lo relacionas?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF000E2E)
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CheckInValidation.categorias.forEach { opcion ->
                        val seleccionado = viewModel.categoria == opcion
                        Surface(
                            modifier = Modifier
                                .alpha(if (viewModel.guardando) 0.6f else 1f)
                                .clickable(enabled = !viewModel.guardando) {
                                    viewModel.actualizarCategoria(if (seleccionado) "" else opcion)
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (seleccionado) Color(0xFF007F9E) else Color(0xFFF2F8FB),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (seleccionado) Color(0xFF007F9E) else Color(0xFFD0E1E8)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (seleccionado) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_check),
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp),
                                        tint = Color.White
                                    )
                                }
                                Text(
                                    text = opcion,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                                    color = if (seleccionado) Color.White else Color(0xFF000E2E)
                                )
                            }
                        }
                    }
                }

                FormFeedback(viewModel.errorCategoria)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE1ECF2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.nota,
                    onValueChange = viewModel::actualizarNota,
                    enabled = !viewModel.guardando,
                    isError = viewModel.errorNota != null,
                    label = { Text("Nota personal (opcional)") },
                    leadingIcon = { Icon(painterResource(R.drawable.ic_note), null) },
                    supportingText = {
                        Text(viewModel.errorNota ?: "${viewModel.nota.length}/${CheckInValidation.MAX_NOTE}")
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 6
                )

                FormFeedback(viewModel.mensajeError)

                Text(
                    text = "Usa datos ficticios en esta demostracion.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF536477)
                )

                Button(
                    onClick = { viewModel.guardarRegistro(onVolver) },
                    enabled = !viewModel.guardando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F9E))
                ) {
                    if (viewModel.guardando) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(painterResource(R.drawable.ic_check), null)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(if (viewModel.guardando) "Guardando..." else "Guardar registro")
                }
            }
        }
    }
}