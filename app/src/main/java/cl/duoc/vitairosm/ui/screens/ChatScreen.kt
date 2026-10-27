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

data class MensajeChat(val texto: String, val esUsuario: Boolean)

@Composable
fun ChatScreen(
    onVolver: () -> Unit
) {
    // mensaje inicial con un saludo del asistente virtual
    var mensajes by remember {
        mutableStateOf(
            listOf(
                MensajeChat("Hola. Este chat de demostración ofrece respuestas predefinidas de orientación general. Elige una pregunta para comenzar.", false)
            )
        )
    }

    // preguntas predefinidas para que el usuario elija rapidamente
    val opcionesPreguntas = listOf(
        "¿Cómo puedo manejar el estrés?",
        "Tengo problemas para dormir",
        "¿Que hago si me siento triste?",
        "Consejos para mantener la calma"
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
            // Cabecera
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chat de orientación",
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000E2E)
                )
                TextButton(onClick = onVolver) {
                    Text("Volver")
                }
            }

            // Lista de mensajes del chat
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(mensajes) { mensaje ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (mensaje.esUsuario) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (mensaje.esUsuario) Color(0xFF007F9E) else Color.White,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = mensaje.texto,
                                modifier = Modifier.padding(14.dp),
                                color = if (mensaje.esUsuario) Color.White else Color(0xFF000E2E),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Seccion de opciones predefinidas
            Text(
                text = "Preguntas sugeridas:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF536477)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                opcionesPreguntas.forEach { pregunta ->
                    Button(
                        onClick = {
                            // preguntas y respuestas automaticas para el usuario
                            val respuestaAsistente = obtenerRespuestaPredefinida(pregunta)
                            mensajes = mensajes + MensajeChat(pregunta, true) + MensajeChat(respuestaAsistente, false)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F1F5))
                    ) {
                        Text(text = pregunta, color = Color(0xFF000E2E))
                    }
                }
            }
        }
    }
}

// Funcion que retorna respuestas predefinidas segun la seleccion
fun obtenerRespuestaPredefinida(pregunta: String): String {
    return when (pregunta) {
        "¿Cómo puedo manejar el estrés?" -> "Intenta respirar hondo cuatro segundos, mantener cuatro y exhalar lentamente. Pausas breves ayudan mucho."
        "Tengo problemas para dormir" -> "Evita pantallas una hora antes de acostarte y mantén tu habitacion oscura y fresca."
        "¿Que hago si me siento triste?" -> "Es valido sentirse asi. Conversa con alguien de confianza o escribe lo que sientes en tu registro."
        "Consejos para mantener la calma" -> "Concentrate en el momento presente, estira tus brazos y bebe un vaso con agua."
        else -> "Recuerda que cuentas con un espacio seguro para tu bienestar."
    }
}