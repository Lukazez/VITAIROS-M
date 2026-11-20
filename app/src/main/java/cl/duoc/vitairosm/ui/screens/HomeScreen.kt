package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitairosm.R
import cl.duoc.vitairosm.model.RegistroEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nombreUsuario: String = "Usuario Demo",
    fotoPerfil: (@Composable () -> Unit)? = null,
    recordatorio: (@Composable () -> Unit)? = null,
    mensajeError: String? = null,
    borrando: Boolean = false,
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

    val categoriasDisponibles = listOf("Todas") + historialRegistros
        .map { it.categoria }
        .distinct()
        .filter { it.isNotBlank() }

    LaunchedEffect(categoriasDisponibles) {
        if (categoriaSeleccionada !in categoriasDisponibles) {
            categoriaSeleccionada = "Todas"
        }
    }

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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE1ECF2))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo_vitairos),
                        contentDescription = "Logo VITAIROS-M",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Text(
                text = "Hola, $nombreUsuario",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000E2E)
            )

            Text(
                text = "Tu espacio de bienestar",
                fontSize = 15.sp,
                color = Color(0xFF536477)
            )

            fotoPerfil?.invoke()

            if (!mensajeError.isNullOrBlank()) {
                Text(
                    text = mensajeError,
                    fontSize = 13.sp,
                    color = Color(0xFFBA1A1A)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
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

            recordatorio?.invoke()

            Card(
                modifier = Modifier.fillMaxWidth(),
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
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000E2E)
                    )

                    Text(
                        text = if (ultimoRegistro == null) {
                            "Todavia no tienes registros en esta demostracion."
                        } else {
                            "Ultimo registro: ${ultimoRegistro.animo}"
                        },
                        color = Color(0xFF536477)
                    )

                    if (!ultimoRegistro?.categoria.isNullOrBlank()) {
                        Text(
                            text = "Categoria: ${ultimoRegistro.categoria}",
                            color = Color(0xFF536477)
                        )
                    }

                    if (!ultimoRegistro?.nota.isNullOrBlank()) {
                        Text(
                            text = "Nota: ${ultimoRegistro.nota}",
                            color = Color(0xFF536477)
                        )
                    }

                    Button(
                        onClick = onRegistrar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005B70))
                    ) {
                        Text("Registrar mi animo", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onIrAlChat,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F9E))
                    ) {
                        Text("Abrir chat de orientacion", fontSize = 14.sp)
                    }

                    Button(
                        onClick = onIrARecursos,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F9E))
                    ) {
                        Text("Ver biblioteca de recursos", fontSize = 14.sp)
                    }

                    Button(
                        onClick = onIrAProfesionales,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F9E))
                    ) {
                        Text("Ver catalogo de profesionales", fontSize = 14.sp)
                    }
                }
            }

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

                                TextButton(
                                    enabled = !borrando,
                                    onClick = {
                                        registroIdAEliminar = registro.id
                                        mostrarDialogo = true
                                    }
                                ) {
                                    Text("Borrar", color = Color(0xFFBA1A1A))
                                }
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver a bienvenida")
            }
        }

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
                        Text("Eliminar", color = Color(0xFFBA1A1A))
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