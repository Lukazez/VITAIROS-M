package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitairosm.model.RegistroEntity

// aqui armamos la pantalla principal con el resumen, los botones y el historial
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nombreUsuario: String,
    fotoPerfil: @Composable () -> Unit,
    onVolver: () -> Unit,
    onRegistrar: () -> Unit,
    onIrAlChat: () -> Unit,
    onIrARecursos: () -> Unit,
    onIrAProfesionales: () -> Unit,
    historialRegistros: List<RegistroEntity>,
    onBorrarRegistro: (Int) -> Unit
) {
    val ultimoRegistro = historialRegistros.firstOrNull()

    var categoriaSeleccionada by remember { mutableStateOf("Todas") }

    val categoriasDisponibles = listOf("Todas") + historialRegistros.map { it.categoria }.distinct().filter { it.isNotBlank() }

    val historialFiltrado = if (categoriaSeleccionada == "Todas") {
        historialRegistros
    } else {
        historialRegistros.filter { it.categoria == categoriaSeleccionada }
    }

    val totalRegistros = historialRegistros.size
    val conteoAnimos = historialRegistros.groupingBy { it.animo }.eachCount()

    var mostrarDialogo by remember { mutableStateOf(false) }
    var registroIdAEliminar by remember { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F8FB))
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // bienvenida al usuario
            Text(
                text = "Hola, $nombreUsuario",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000E2E)
            )

            Text(
                text = "Tu espacio de bienestar",
                fontSize = 16.sp,
                color = Color(0xFF536477)
            )

            fotoPerfil()

            // tarjeta con las estadisticas y resumen general
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F1F5)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Resumen de bienestar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000E2E)
                    )
                    Text(
                        text = "Total de registros: $totalRegistros",
                        fontSize = 14.sp,
                        color = Color(0xFF536477)
                    )

                    if (conteoAnimos.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Frecuencia por animo:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFF000E2E)
                        )
                        conteoAnimos.forEach { (animo, cantidad) ->
                            Text(
                                text = "- $animo: $cantidad veces",
                                fontSize = 13.sp,
                                color = Color(0xFF536477)
                            )
                        }
                    }
                }
            }

            // tarjeta con los botones para navegar a las demas funciones
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
                        text = "¿Como te sientes hoy?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (ultimoRegistro == null) {
                            "Todavia no tienes registros en esta demostracion."
                        } else {
                            "Ultimo registro: ${ultimoRegistro.animo}"
                        }
                    )

                    if (!ultimoRegistro?.categoria.isNullOrBlank()) {
                        Text("Categoria: ${ultimoRegistro?.categoria}")
                    }

                    if (!ultimoRegistro?.nota.isNullOrBlank()) {
                        Text("Nota: ${ultimoRegistro?.nota}")
                    }

                    // boton para ir al formulario de registro
                    Button(
                        onClick = onRegistrar,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005B70))
                    ) {
                        Text("Registrar mi animo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    // boton para abrir el chat
                    Button(
                        onClick = onIrAlChat,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F9E))
                    ) {
                        Text("Abrir chat de orientacion", fontSize = 15.sp)
                    }

                    // boton para ver la biblioteca
                    Button(
                        onClick = onIrARecursos,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F9E))
                    ) {
                        Text("Ver biblioteca de recursos", fontSize = 15.sp)
                    }

                    // boton para ver profesionales
                    Button(
                        onClick = onIrAProfesionales,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F9E))
                    ) {
                        Text("Ver catalogo de profesionales", fontSize = 15.sp)
                    }
                }
            }

            // seccion del historial donde se muestran los registros guardados y los filtros
            if (historialRegistros.isNotEmpty()) {
                Text(
                    text = "Historial de registros",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000E2E)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoriasDisponibles.forEach { categoria ->
                        FilterChip(
                            selected = categoriaSeleccionada == categoria,
                            onClick = { categoriaSeleccionada = categoria },
                            label = { Text(categoria) }
                        )
                    }
                }

                if (historialFiltrado.isEmpty()) {
                    Text(
                        text = "No hay registros para esta categoria.",
                        fontSize = 14.sp,
                        color = Color(0xFF536477)
                    )
                } else {
                    historialFiltrado.forEach { registro ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Animo: ${registro.animo}",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF007F9E)
                                    )
                                    if (registro.categoria.isNotBlank()) {
                                        Text(
                                            text = "Categoria: ${registro.categoria}",
                                            color = Color(0xFF536477)
                                        )
                                    }
                                    if (registro.nota.isNotBlank()) {
                                        Text(
                                            text = "Nota: ${registro.nota}",
                                            color = Color(0xFF536477)
                                        )
                                    }
                                }

                                // boton para borrar un registro
                                TextButton(
                                    onClick = {
                                        registroIdAEliminar = registro.id
                                        mostrarDialogo = true
                                    }
                                ) {
                                    Text("Borrar", color = Color.Red)
                                }
                            }
                        }
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
                        text = "Tu proximo paso",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000E2E)
                    )

                    Text(
                        text = "Dedica un momento a reconocer como te sientes hoy."
                    )
                }
            }

            Text(
                text = "VITAIROS-M no reemplaza la atencion profesional.",
                fontSize = 13.sp,
                color = Color(0xFF536477)
            )

            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar sesión")
            }
        }

        // ventana para confirmar si se desea borrar un registro
        if (mostrarDialogo && registroIdAEliminar != null) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("Eliminar registro") },
                text = { Text("¿Estas seguro de que deseas eliminar este registro del historial?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            registroIdAEliminar?.let { id ->
                                onBorrarRegistro(id)
                            }
                            mostrarDialogo = false
                            registroIdAEliminar = null
                        }
                    ) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogo = false
                            registroIdAEliminar = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
