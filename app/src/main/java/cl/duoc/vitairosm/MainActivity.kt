
package cl.duoc.vitairosm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitairosm.ui.theme.VITAIROSMTheme
import cl.duoc.vitairosm.ui.screens.HomeScreen
import cl.duoc.vitairosm.ui.screens.CheckInScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VITAIROSMTheme {

                var ingresoDemo by rememberSaveable {
                    mutableStateOf(false)
                }

                var mostrarRegistro by rememberSaveable {
                    mutableStateOf(false)
                }

                var ultimoAnimo by rememberSaveable {
                    mutableStateOf<String?>(null)
                }

                var ultimaCategoria by rememberSaveable {
                    mutableStateOf<String?>(null)
                }

                var ultimaNota by rememberSaveable {
                    mutableStateOf<String?>(null)
                }

                if (!ingresoDemo) {
                    PantallaBienvenida {
                        ingresoDemo = true
                    }
                } else if (mostrarRegistro) {
                    CheckInScreen(
                        onVolver = {
                            mostrarRegistro = false
                        },
                        onGuardar = { animo, categoria, nota ->
                            ultimoAnimo = animo
                            ultimaCategoria = categoria
                            ultimaNota = nota
                            mostrarRegistro = false
                        }
                    )
                } else {
                    HomeScreen(
                        ultimoAnimo = ultimoAnimo,
                        ultimaCategoria = ultimaCategoria,
                        ultimaNota = ultimaNota,
                        onRegistrar = {
                            mostrarRegistro = true
                        },
                        onVolver = {
                            ingresoDemo = false
                        }
                    )
                }

            }
            }
        }
    }


@Composable
fun PantallaBienvenida(onIngresar: () -> Unit) {

    var acepto by rememberSaveable {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF2F8FB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "VITAIROS-M",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000E2E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tu espacio de orientación y acompañamiento",
                fontSize = 18.sp,
                color = Color(0xFF536477)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Tu bienestar importa",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000E2E)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Registra cómo te sientes, " +
                                "explora recursos y encuentra " +
                                "tu próximo paso.",
                        color = Color(0xFF536477)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "VITAIROS-M ofrece orientación general. " +
                        "No realiza diagnósticos ni reemplaza " +
                        "la atención profesional.",
                fontSize = 13.sp,
                color = Color(0xFF536477)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = acepto,
                    onCheckedChange = { acepto = it }
                )

                Text(
                    text = "He leído y acepto las condiciones " +
                            "de uso de esta demostración.",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onIngresar,
                enabled = acepto,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007F9E)
                )
            ) {
                Text(
                    text = "Ingresar como usuario demo",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

