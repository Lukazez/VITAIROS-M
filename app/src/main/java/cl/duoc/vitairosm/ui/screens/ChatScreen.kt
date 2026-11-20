package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MensajeChat(val texto: String, val esUsuario: Boolean)

@Composable
fun ChatScreen(
    onVolver: () -> Unit
) {
    val mensajeInicial = MensajeChat(
        "Hola. Este chat de demostracion ofrece respuestas predefinidas de orientacion general. Elige una consulta abajo para comenzar.",
        false
    )

    var mensajes by remember { mutableStateOf(listOf(mensajeInicial)) }
    val listState = rememberLazyListState()

    val opcionesPreguntas = listOf(
        "¿Como puedo manejar el estres?",
        "Tengo problemas para dormir",
        "¿Que hago si me siento triste?",
        "Consejos para mantener la calma",
        "Me siento sobrepasado con los estudios"
    )

    LaunchedEffect(mensajes.size) {
        if (mensajes.isNotEmpty()) {
            listState.animateScrollToItem(mensajes.lastIndex)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8FB))
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Chat de orientacion",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000E2E)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF1F9D7A), CircleShape)
                        )
                        Text(
                            text = "Asistente virtual activo",
                            fontSize = 12.sp,
                            color = Color(0xFF536477)
                        )
                    }
                }

                if (mensajes.size > 1) {
                    TextButton(onClick = { mensajes = listOf(mensajeInicial) }) {
                        Text("Limpiar", color = Color(0xFF536477), fontSize = 13.sp)
                    }
                }

                TextButton(onClick = onVolver) {
                    Text("Volver", color = Color(0xFF007F9E), fontWeight = FontWeight.SemiBold)
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(mensajes) { mensaje ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (mensaje.esUsuario) Alignment.End else Alignment.Start
                    ) {
                        Text(
                            text = if (mensaje.esUsuario) "Tu" else "Asistente VITAIROS-M",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF536477),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (mensaje.esUsuario) 16.dp else 4.dp,
                                bottomEnd = if (mensaje.esUsuario) 4.dp else 16.dp
                            ),
                            color = if (mensaje.esUsuario) Color(0xFF005B70) else Color.White,
                            border = if (mensaje.esUsuario) null else BorderStroke(1.dp, Color(0xFFE1ECF2)),
                            modifier = Modifier.widthIn(max = 290.dp)
                        ) {
                            Text(
                                text = mensaje.texto,
                                modifier = Modifier.padding(14.dp),
                                color = if (mensaje.esUsuario) Color.White else Color(0xFF000E2E),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE1ECF2))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Selecciona una consulta rapida:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000E2E)
                    )

                    opcionesPreguntas.forEach { pregunta ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val respuestaAsistente = obtenerRespuestaPredefinida(pregunta)
                                    mensajes = mensajes + MensajeChat(pregunta, true) + MensajeChat(respuestaAsistente, false)
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF2F8FB),
                            border = BorderStroke(1.dp, Color(0xFFD6E5EC))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = pregunta,
                                    color = Color(0xFF000E2E),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "Enviar",
                                    color = Color(0xFF007F9E),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun obtenerRespuestaPredefinida(pregunta: String): String {
    return when (pregunta) {
        "¿Como puedo manejar el estres?" -> "Intenta respirar hondo cuatro segundos, mantener cuatro y exhalar lentamente. Pausas breves ayudan mucho."
        "Tengo problemas para dormir" -> "Evita pantallas una hora antes de acostarte y manten tu habitacion oscura y fresca."
        "¿Que hago si me siento triste?" -> "Es valido sentirse asi. Conversa con alguien de confianza o escribe lo que sientes en tu registro."
        "Consejos para mantener la calma" -> "Concentrate en el momento presente, estira tus brazos y bebe un vaso con agua."
        "Me siento sobrepasado con los estudios" -> "Divide tus tareas en bloques de 25 minutos con descansos de 5 minutos. Prioriza una entrega a la vez."
        else -> "Recuerda que cuentas con un espacio seguro para tu bienestar."
    }
}