package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.vitairosm.R

@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    var acepto by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8FB))
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE1ECF2))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_vitairos),
                    contentDescription = "Logo VITAIROS-M",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Text(
            text = "Tu bienestar importa",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007F9E)
        )
        Text(
            text = "Registra como te sientes, explora recursos y encuentra tu proximo paso.",
            color = Color(0xFF536477)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE1ECF2))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Antes de comenzar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000E2E)
                )
                Text(
                    text = "Esta aplicacion academica ofrece orientacion general. No realiza diagnosticos ni reemplaza la atencion profesional.",
                    color = Color(0xFF536477)
                )
                Text(
                    text = "Usa unicamente datos ficticios. Las cuentas, registros y fotos se guardan en este dispositivo; no se envian a un servidor.",
                    color = Color(0xFF536477)
                )
                Text(
                    text = "El chat utiliza respuestas predefinidas y la agenda simula una solicitud; no contacta a profesionales.",
                    color = Color(0xFF536477)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = acepto, onCheckedChange = { acepto = it })
            Text(
                text = "He leido y comprendo el alcance de esta demostracion.",
                modifier = Modifier.weight(1f),
                color = Color(0xFF000E2E)
            )
        }

        Button(
            onClick = onContinue,
            enabled = acepto,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005B70))
        ) {
            Text("Continuar", fontWeight = FontWeight.Bold)
        }
    }
}